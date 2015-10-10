/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.gen.extension.svn;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.seasar.extension.jdbc.gen.event.GenDdlEvent;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.wc.admin.ISVNAdminAreaFactorySelector;
import org.tmatesoft.svn.core.internal.wc.admin.SVNAdminAreaFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCopyClient;
import org.tmatesoft.svn.core.wc.SVNCopySource;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatus;
import org.tmatesoft.svn.core.wc.SVNStatusClient;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNWCClient;

/**
 * Gen-Ddlタスクによって生成されるディレクトリやファイルをSubversionで管理するための抽象クラスです。
 * <p>
 * 新しいバージョンの元で生成されるファイルと同名のファイルが前のバージョンディレクトリに存在する場合は、 リビジョンを追跡できるようにするため
 * <code>svn copy</copy>操作で前のバージョンのファイルを新しいバージョンディレクトリにコピーします。
 * </p>
 * 
 * @author koichik
 */
public class GenDdlSvnProcessor implements GenDdlListener {

    /** SVNクライアントマネージャです。 */
    protected SVNClientManager clientManager = SVNClientManager.newInstance();

    /** SVNワーキングコピークライアントです。 */
    protected SVNWCClient wcClient = clientManager.getWCClient();

    /** SVNステータスクライアントです。 */
    protected SVNStatusClient statusClient = clientManager.getStatusClient();

    /** SVNコピークライアントです。 */
    protected SVNCopyClient copyClient = clientManager.getCopyClient();

    /** 現在のバージョンディレクトリです。 */
    protected File currentVersionDir;

    /** 次 (実際の生成対象) のバージョンディレクトリです。 */
    protected File nextVersionDir;

    /** マイグレーションのルートディレクトリです。 */
    protected File migrateDir;

    /** マイグレーションディレクトリおよび前のバージョンディレクトリが共にSubversionで管理されていれば{@literal true}です。 */
    protected boolean underSvn;

    static {
        SVNAdminAreaFactory.setUpgradeEnabled(false);
    }

    /**
     * インスタンスを構築します。
     * 
     */
    public GenDdlSvnProcessor() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param version
     *            SVNのバージョン
     */
    public GenDdlSvnProcessor(final int version) {
        SVNAdminAreaFactory.setSelector(new ISVNAdminAreaFactorySelector() {

            @SuppressWarnings("unchecked")
            public Collection getEnabledFactories(File path,
                    Collection factories, boolean writeAccess)
                    throws SVNException {
                final Set enabledFactories = new TreeSet();
                for (final Iterator it = factories.iterator(); it.hasNext();) {
                    final SVNAdminAreaFactory factory = (SVNAdminAreaFactory) it
                            .next();
                    if (version == factory.getSupportedVersion()) {
                        enabledFactories.add(factory);
                    }
                }
                return enabledFactories;
            }
        });
    }

    public void preCreateNextVersionDir(final GenDdlEvent event) {
    }

    /**
     * マイグレーションディレクトリと現在のバージョンディレクトリが共にSubversionで管理されていれば、
     * 作成された次のバージョンディレクトリをSubversionの管理下に加えます。
     * 
     * @param event
     *            イベント
     */
    public void postCreateNextVersionDir(final GenDdlEvent event) {
        try {
            currentVersionDir = event.getCurrentVersionDir().asFile();
            nextVersionDir = event.getNextVersionDir().asFile();
            migrateDir = nextVersionDir.getParentFile();
            underSvn = underSvn(migrateDir) && underSvn(currentVersionDir);
            if (!underSvn) {
                return;
            }

            // svn add nextVersionDir
            wcClient.doAdd(nextVersionDir, false, false, false, null, false,
                    false);
        } catch (final SVNException e) {
            throw new SvnRuntimeException(e);
        }
    }

    /**
     * 次のバージョンディレクトリが削除される前にSubversionの管理下から外します。
     * 
     * @param event
     *            イベント
     */
    public void preRemoveNextVersionDir(final GenDdlEvent event) {
        try {
            if (!underSvn) {
                return;
            }
            // svn delete nextVersionDir
            wcClient.doDelete(nextVersionDir, true, false, false);
        } catch (final SVNException e) {
            throw new SvnRuntimeException(e);
        }
    }

    public void postRemoveNextVersionDir(final GenDdlEvent event) {
    }

    /**
     * 次のバージョンディレクトリに生成されるファイルと同名のファイルが現在のバージョンディレクトリに存在する場合は、
     * リビジョンを追跡できるようにするため
     * <code>svn copy</copy>操作で現在のバージョンディレクトリのファイルを次のバージョンディレクトリにコピーします。
     * 
     * @param event
     *            イベント
     */
    public void preCreateTargetFile(final GenDdlEvent event) {
        try {
            if (!underSvn) {
                return;
            }
            final File currentFile = new File(currentVersionDir, event
                    .getTargetFile());
            final File parentFile = currentFile.getParentFile();
            if (!currentFile.exists() || currentFile.isDirectory()
                    || !underSvn(currentFile) || !underSvn(parentFile)) {
                return;
            }

            // svn copy currentVersionDir/targetFile nextVersionDir/targetFile
            final SVNCopySource source = new SVNCopySource(SVNRevision.WORKING,
                    SVNRevision.WORKING, currentFile);
            final File nextFile = new File(nextVersionDir, event
                    .getTargetFile());
            copyClient.doCopy(new SVNCopySource[] { source }, nextFile, false,
                    false, true);
        } catch (final SVNException e) {
            throw new SvnRuntimeException(e);
        }
    }

    /**
     * 次のバージョンディレクトリ以下に作成されたディレクトリをSubversionの管理下に加えます。
     * 
     * @param event
     *            イベント
     */
    public void postCreateTargetFile(final GenDdlEvent event) {
        try {
            if (!underSvn) {
                return;
            }
            final File createdFile = new File(nextVersionDir, event
                    .getTargetFile());
            if (!createdFile.isDirectory()) {
                return;
            }

            // svn add nextVersionDir/targetFile
            wcClient
                    .doAdd(createdFile, false, false, false, null, false, false);
        } catch (final SVNException e) {
            throw new SvnRuntimeException(e);
        }
    }

    /**
     * ファイルがSubversionで管理されていれば{@literal true}を返します。
     * 
     * @param file
     *            ファイル
     * @return ファイルがSubversionで管理されていれば{@literal true}
     * @throws SVNException
     *             Subversion の操作中に例外が発生した場合
     */
    protected boolean underSvn(final File file) {
        try {
            final SVNStatus status = statusClient.doStatus(file, false);
            return status != null
                    && status.getContentsStatus() != SVNStatusType.STATUS_UNVERSIONED;
        } catch (final SVNException e) {
            return false;
        }
    }

}
