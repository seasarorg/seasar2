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
package org.seasar.extension.jdbc.gen.internal.version.wrapper;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.gen.event.GenDdlEvent;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectory;
import org.seasar.extension.jdbc.gen.version.ManagedFile;

/**
 * {@link ManagedFile}のラッパーです。
 * 
 * @author taedium
 */
public class ManagedFileWrapper implements ManagedFile {

    /** 親の{@link ManagedFile} */
    protected ManagedFile parent;

    /** ラップの対象 */
    protected ManagedFile target;

    /** リスナー */
    protected GenDdlListener genDdlListener;

    /** 現バージョンに対応するディレクトリ */
    protected DdlVersionDirectory currentVersionDir;

    /** 次バージョンに対応するディレクトリ */
    protected DdlVersionDirectory nextVersionDir;

    /**
     * インスタンスを構築します。
     * 
     * @param target
     *            ラップの対象
     * @param genDdlListener
     *            リスナー
     * @param currentVersionDir
     *            現バージョンに対応するディレクトリ
     * @param nextVersionDir
     *            次バージョンに対応するディレクトリ
     */
    protected ManagedFileWrapper(ManagedFile target,
            GenDdlListener genDdlListener,
            DdlVersionDirectory currentVersionDir,
            DdlVersionDirectory nextVersionDir) {
        this(null, target, genDdlListener, currentVersionDir, nextVersionDir);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param parent
     *            親の{@link ManagedFile}、このインスタンスがバージョンディレクトリの場合{@code null}
     * @param target
     *            ラップの対象
     * @param genDdlListener
     *            リスナー
     * @param currentVersionDir
     *            現バージョンに対応するディレクトリ
     * @param nextVersionDir
     *            次バージョンに対応するディレクトリ
     */
    protected ManagedFileWrapper(ManagedFile parent, ManagedFile target,
            GenDdlListener genDdlListener,
            DdlVersionDirectory currentVersionDir,
            DdlVersionDirectory nextVersionDir) {
        if (target == null) {
            throw new NullPointerException("target");
        }
        if (genDdlListener == null) {
            throw new NullPointerException("genDdlListener");
        }
        if (currentVersionDir == null) {
            throw new NullPointerException("currentVersionDir");
        }
        if (nextVersionDir == null) {
            throw new NullPointerException("nextVersionDir");
        }
        this.parent = parent;
        this.target = target;
        this.genDdlListener = genDdlListener;
        this.currentVersionDir = currentVersionDir;
        this.nextVersionDir = nextVersionDir;
    }

    public File asFile() {
        return getManagedFile().asFile();
    }

    public ManagedFile createChild(String relativePath) {
        ManagedFile file = getManagedFile().createChild(relativePath);
        return wrap(file);
    }

    public boolean delete() {
        return getManagedFile().delete();
    }

    public boolean createNewFile() {
        if (getManagedFile().exists()) {
            return false;
        }
        mkdirs(parent);
        GenDdlEvent event = new GenDdlEvent(this, currentVersionDir,
                nextVersionDir, getManagedFile().getRelativePath());
        genDdlListener.preCreateTargetFile(event);
        boolean made = getManagedFile().createNewFile();
        if (made) {
            genDdlListener.postCreateTargetFile(event);
        }
        return made;
    }

    public ManagedFile getParent() {
        return parent;
    }

    public boolean mkdir() {
        if (getManagedFile().exists()) {
            return false;
        }
        GenDdlEvent event = new GenDdlEvent(this, currentVersionDir,
                nextVersionDir, getManagedFile().getRelativePath());
        genDdlListener.preCreateTargetFile(event);
        boolean made = getManagedFile().mkdir();
        if (made) {
            genDdlListener.postCreateTargetFile(event);
        }
        return made;
    }

    public boolean mkdirs() {
        return mkdirs(this);
    }

    /**
     * ディレクトリを生成します。存在していないが必要な親ディレクトリも一緒に作成されます。
     * 
     * @param file
     *            ファイル
     * @return 必要なすべての親ディレクトリを含めてディレクトリが生成された場合は{@code true}、そうでない場合は{@code
     *         false}
     */
    protected boolean mkdirs(ManagedFile file) {
        ManagedFile parent = file.getParent();
        if (parent == null) {
            File parentFile = file.asFile().getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    return false;
                }
            }
        } else {
            if (!parent.exists()) {
                if (!mkdirs(parent)) {
                    return false;
                }
            }
        }
        return file.mkdir();
    }

    public boolean exists() {
        return getManagedFile().exists();
    }

    public String getName() {
        return getManagedFile().getName();
    }

    public String getRelativePath() {
        return getManagedFile().getRelativePath();
    }

    public boolean isDirectory() {
        return getManagedFile().isDirectory();
    }

    public List<File> listAllFiles() {
        return getManagedFile().listAllFiles();
    }

    public List<ManagedFile> listManagedFiles() {
        List<ManagedFile> list = new ArrayList<ManagedFile>();
        for (ManagedFile file : getManagedFile().listManagedFiles()) {
            list.add(wrap(file));
        }
        return list;
    }

    public List<ManagedFile> listManagedFiles(FilenameFilter filter) {
        List<ManagedFile> list = new ArrayList<ManagedFile>();
        for (ManagedFile file : getManagedFile().listManagedFiles(filter)) {
            list.add(wrap(file));
        }
        return list;
    }

    public boolean hasChild() {
        return getManagedFile().hasChild();
    }

    /**
     * バージョン管理されたファイルを返します。
     * 
     * @return バージョン管理されたファイル
     */
    protected ManagedFile getManagedFile() {
        return target;
    }

    /**
     * バージョン管理されたファイルをラップします。
     * 
     * @param target
     *            ラップの対象
     * @return ラップされたバージョン管理されたファイル
     */
    protected ManagedFile wrap(ManagedFile target) {
        return new ManagedFileWrapper(this, target, genDdlListener,
                currentVersionDir, nextVersionDir);
    }

}
