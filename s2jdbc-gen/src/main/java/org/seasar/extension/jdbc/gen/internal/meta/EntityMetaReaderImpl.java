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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.Entity;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.internal.exception.DocletUnavailableRuntimeException;
import org.seasar.extension.jdbc.gen.internal.exception.EntityClassNotFoundRuntimeException;
import org.seasar.extension.jdbc.gen.internal.util.FileUtil;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.ClassTraversal;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;

import com.sun.javadoc.Doclet;

/**
 * {@link EntityMetaReader}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityMetaReaderImpl implements EntityMetaReader {

    /** ロガー */
    protected static Logger logger = Logger
            .getLogger(EntityMetaReaderImpl.class);

    /** {@link Doclet}が使用可能な場合{@code true} */
    protected static boolean docletAvailable;
    static {
        try {
            Class.forName("com.sun.javadoc.Doclet"); // tools.jar
            docletAvailable = true;
        } catch (final Throwable ignore) {
        }
    }

    /** ルートディレクトリ */
    protected File classpathDir;

    /** 読み取り対象とするパッケージ名 */
    protected String packageName;

    /** エンティティメタデータのファクトリ */
    protected EntityMetaFactory entityMetaFactory;

    /** 読み取り対象とするエンティティクラス名のパターン */
    protected Pattern shortClassNamePattern;

    /** 読み取り非対象とするエンティティクラス名のパターン */
    protected Pattern ignoreShortClassNamePattern;

    /** コメントを読む場合 {@code true} */
    protected boolean readComment;

    /**
     * javaファイルが存在するディレクトリのリスト、{@code useComment}が{@code true}の場合{@code null}
     * であってはならない
     */
    protected List<File> javaFileSrcDirList = new ArrayList<File>();

    /**
     * javaファイルのエンコーディング、{@code useComment}が{@code true}の場合{@code null}
     * であってはならない
     */
    protected String javaFileEncoding;

    /**
     * インタスタンスを構築します。
     * 
     * @param classpathDir
     *            ルートディレクトリ
     * @param packageName
     *            パッケージ名、パッケージ名を指定しない場合は{@code null}
     * @param entityMetaFactory
     *            エンティティメタデータのファクトリ
     * @param shortClassNamePattern
     *            対象とするエンティティクラス名の正規表現
     * @param ignoreShortClassNamePattern
     *            対象としないエンティティクラス名の正規表現
     * @param readComment
     *            エンティティのコメントを使用する場合 {@code true}
     * @param javaFileSrcDirList
     *            javaファイルが存在するディレクトリのリスト、{@code readComment}が{@code true}の場合
     *            {@code null}であってはならない
     * @param javaFileEncoding
     *            javaファイルのエンコーディング、{@code readComment}が{@code true}の場合{@code
     *            null}であってはならない
     */
    public EntityMetaReaderImpl(File classpathDir, String packageName,
            EntityMetaFactory entityMetaFactory, String shortClassNamePattern,
            String ignoreShortClassNamePattern, boolean readComment,
            List<File> javaFileSrcDirList, String javaFileEncoding) {
        if (classpathDir == null) {
            throw new NullPointerException("classpathDir");
        }
        if (entityMetaFactory == null) {
            throw new NullPointerException("entityMetaFactory");
        }
        if (shortClassNamePattern == null) {
            throw new NullPointerException("shortClassNamePattern");
        }
        if (ignoreShortClassNamePattern == null) {
            throw new NullPointerException("ignoreShortClassNamePattern");
        }
        if (readComment) {
            if (javaFileSrcDirList == null) {
                throw new NullPointerException("javaFileSrcDirList");
            }
            if (javaFileSrcDirList.isEmpty()) {
                throw new IllegalArgumentException("javaFileSrcDirList");
            }
            if (javaFileEncoding == null) {
                throw new NullPointerException("javaFileEncoding");
            }
        }
        this.classpathDir = classpathDir;
        this.packageName = packageName;
        this.entityMetaFactory = entityMetaFactory;
        this.shortClassNamePattern = Pattern.compile(shortClassNamePattern);
        this.ignoreShortClassNamePattern = Pattern
                .compile(ignoreShortClassNamePattern);
        this.readComment = readComment;
        if (javaFileSrcDirList != null) {
            this.javaFileSrcDirList.addAll(javaFileSrcDirList);
        }
        this.javaFileEncoding = javaFileEncoding;
    }

    public List<EntityMeta> read() {
        final List<EntityMeta> entityMetaList = new ArrayList<EntityMeta>();

        ClassTraversal.forEach(classpathDir, new ClassHandler() {

            public void processClass(String packageName, String shortClassName) {
                if (isTargetPackage(packageName)
                        && isTargetClass(shortClassName)) {
                    String className = ClassUtil.concatName(packageName,
                            shortClassName);
                    Class<?> clazz = ClassUtil.forName(className);
                    if (clazz.isAnnotationPresent(Entity.class)) {
                        EntityMeta entityMeta = entityMetaFactory
                                .getEntityMeta(clazz);
                        entityMetaList.add(entityMeta);
                    }
                }
            }
        });

        if (entityMetaList.isEmpty()) {
            throw new EntityClassNotFoundRuntimeException(classpathDir,
                    packageName, shortClassNamePattern.pattern(),
                    ignoreShortClassNamePattern.pattern());
        }

        if (readComment) {
            readComment(entityMetaList);
        }

        return entityMetaList;
    }

    /**
     * 読み取りの対象パッケージの場合{@code true}を返します。
     * 
     * @param packageName
     *            パッケージ名
     * @return 読み取りの対象パッケージの場合{@code true}
     */
    protected boolean isTargetPackage(String packageName) {
        if (packageName == null) {
            return true;
        }
        if (packageName.equals(this.packageName)) {
            return true;
        }
        if (packageName.startsWith(this.packageName + ".")) {
            return true;
        }
        return false;
    }

    /**
     * 読み取りの対象クラスの場合{@code true}を返します。
     * 
     * @param shortClassName
     *            クラスの単純名
     * @return 読み取りの対象クラスの場合{@code true}
     */
    protected boolean isTargetClass(String shortClassName) {
        if (!shortClassNamePattern.matcher(shortClassName).matches()) {
            return false;
        }
        if (ignoreShortClassNamePattern.matcher(shortClassName).matches()) {
            return false;
        }
        return true;
    }

    /**
     * コメントを読みコメントをメタデータに設定します。
     * 
     * @param entityMetaList
     *            エンティティメタデータのリスト
     */
    protected void readComment(List<EntityMeta> entityMetaList) {
        if (!docletAvailable) {
            throw new DocletUnavailableRuntimeException();
        }
        String[] args = createDocletArgs();
        StringBuilder buf = new StringBuilder();
        for (String arg : args) {
            buf.append(arg).append(" ");
        }
        logger.log("DS2JDBCGen0019", new Object[] { buf.toString() });

        CommentDocletContext.setEntityMetaList(entityMetaList);
        try {
            com.sun.tools.javadoc.Main.execute(args);
        } finally {
            CommentDocletContext.setEntityMetaList(null);
        }
    }

    /**
     * {@link Doclet}の引数の配列を作成します。
     * 
     * @return {@link Doclet}の引数の配列
     */
    protected String[] createDocletArgs() {
        StringBuilder srcDirListBuf = new StringBuilder();
        for (File dir : javaFileSrcDirList) {
            srcDirListBuf.append(FileUtil.getCanonicalPath(dir));
            srcDirListBuf.append(File.pathSeparator);
        }
        srcDirListBuf.setLength(srcDirListBuf.length()
                - File.pathSeparator.length());

        List<String> args = new ArrayList<String>();
        args.add("-doclet");
        args.add(CommentDoclet.class.getName());
        args.add("-sourcepath");
        args.add(srcDirListBuf.toString());
        args.add("-encoding");
        args.add(javaFileEncoding);
        args.add("-subpackages");
        args.add(packageName);
        if (logger.isDebugEnabled()) {
            args.add("-verbose");
        }
        return args.toArray(new String[args.size()]);
    }

    public boolean isFiltered() {
        return !shortClassNamePattern.pattern().equals(".*")
                || !ignoreShortClassNamePattern.pattern().equals("");
    }

}
