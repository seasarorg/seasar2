/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.env;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.InputStreamReaderUtil;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.ReaderUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.TextUtil;
import org.seasar.framework.util.URLUtil;

/**
 * 環境名を保持するクラスです。
 * <p>
 * 環境名とは、SMART deployの動作モード (HOT/COOL/WARM) 等を実行時に切り替えるために使われる名前です。
 * 環境名は、環境名設定ファイルから読み込まれます。 デフォルトの環境名設定ファイルは<code>env.txt</code>です。
 * 環境名設定ファイルがクラスパス上に存在しない場合、環境名は<code>product</code>に設定されます。
 * </p>
 * <p>
 * 環境名には任意の名前を使用することができますが、Seasar2では標準的な環境名として以下の名前を使用することを推奨しています。
 * </p>
 * <table border="1">
 * <tr>
 * <th>環境名</th>
 * <th>モード</th>
 * <th>用途</th>
 * </tr>
 * <tr>
 * <td><code>ut</code></td>
 * <td>WARM deploy</td>
 * <td>JUnit等を使用した単体テスト環境</td>
 * </tr>
 * <tr>
 * <td><code>ct</code></td>
 * <td>HOT deploy</td>
 * <td>IDEを使用した結合テスト環境</td>
 * </tr>
 * <tr>
 * <td><code>it</code></td>
 * <td>COOL deploy</td>
 * <td>本来の実行環境 (APサーバ) を使用した統合テスト環境</td>
 * </tr>
 * <tr>
 * <td><code>production</code></td>
 * <td>COOL deploy</td>
 * <td>運用環境</td>
 * </tr>
 * </table>
 * <p>
 * 環境名設定ファイルのエンコードはUTF-8固定です。
 * </p>
 * 
 * @author higa
 */
public class Env {

    /** 運用環境を表す環境名です。 */
    public static final String PRODUCT = "product";

    /** 単体テスト環境を表す環境名です。 */
    public static final String UT = "ut";

    /** 結合テスト環境を表す環境名です。 */
    public static final String CT = "ct";

    /** 統合テスト環境を表す環境名です。 */
    public static final String IT = "it";

    /** デフォルトの環境名設定ファイルのパスです。 */
    public static final String DEFAULT_FILE_PATH = "env.txt";

    /** ロガー */
    private static final Logger logger = Logger.getLogger(Env.class);

    /** 環境名です。 */
    private static String value;

    /** 環境名設定ファイルのパスです。 */
    private static String filePath = DEFAULT_FILE_PATH;

    /** 環境名設定ファイル */
    private static File file;

    /** 環境名設定ファイルを読み込んだときのファイルの更新時刻 */
    private static long lastModified;

    static {
        initialize();
    }

    private Env() {
    }

    /**
     * デフォルトの環境名設定ファイルパスを使用するように初期化します。
     * 
     */
    public static void initialize() {
        setFilePath(DEFAULT_FILE_PATH);
    }

    /**
     * 環境名を返します。
     * <p>
     * 環境名設定ファイルがクラスパス上に存在し、そのファイルを最後に読み込んだ後にファイルが更新されていれば、ファイルを再読込します。
     * 環境名設定ファイルが存在せず、環境名が設定されていない場合は環境名として{@link #PRODUCT 運用環境}を返します。
     * </p>
     * 
     * @return 環境名
     */
    public static String getValue() {
        if (file != null && file.lastModified() > lastModified) {
            calcValue();
        }
        if (StringUtil.isEmpty(value)) {
            return PRODUCT;
        }
        return value;
    }

    /**
     * 環境名が設定されていなければ、環境名を設定します。
     * 
     * @param newValue
     *            環境名
     * @return 環境名を設定した場合は<code>true</code>、それ以外の場合は<code>false</code>
     */
    public static boolean setValueIfAbsent(final String newValue) {
        if (value == null) {
            value = newValue;
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0115", new Object[] { value, filePath });
            }
            return true;
        }
        return false;
    }

    /**
     * パスから拡張子を除いて、環境名をサフィックスとして加えたパスを返します。
     * 
     * @param path
     *            パス
     * @return パスから拡張子を除いて、環境名をサフィックスとして加えたパス
     */
    public static String adjustPath(final String path) {
        final String env = getValue();
        if (PRODUCT.equals(env)) {
            return path;
        }
        final int index = path.lastIndexOf('.');
        if (index < 0) {
            return path;
        }
        final String p = path.substring(0, index);
        final String ext = path.substring(index + 1);
        return p + "_" + env + "." + ext;
    }

    /**
     * 環境名設定ファイルのパスを返します。
     * 
     * @return 環境名設定ファイルのパス
     */
    public static String getFilePath() {
        return filePath;
    }

    /**
     * 環境名設定ファイルのパスを設定します。
     * 
     * @param filePath
     *            環境名設定ファイルのパス
     * @throws EmptyRuntimeException
     *             環境名設定ファイルが<code>null</code>あるいは空文字列の場合にスローされます
     */
    public static void setFilePath(final String filePath) {
        if (filePath == null) {
            throw new EmptyRuntimeException("filePath");
        }
        Env.filePath = filePath;
        final URL url = ResourceUtil.getResourceNoException(filePath);
        if (url == null) {
            file = null;
            clearValue();
            return;
        }
        file = ResourceUtil.getFile(url);
        if (file != null) {
            calcValue();
        } else {
            calcValue(url);
        }
    }

    /**
     * 環境名設定ファイルを設定します。
     * 
     * @param fileObj
     *            環境設定ファイルを表す {@link File} オブジェクト
     * @throws EmptyRuntimeException
     *             <code>fileObj</code>が<code>null</code>の場合にスローされます
     * @throws FileNotFoundException
     *             <code>fileObj</code>の示すファイルが存在しない場合にスローされます
     */
    public static void setFile(final File fileObj) throws FileNotFoundException {
        if (fileObj == null) {
            throw new EmptyRuntimeException("fileObj");
        }
        if (!fileObj.exists()) {
            throw new FileNotFoundException(fileObj.getAbsolutePath());
        }
        Env.filePath = fileObj.getAbsolutePath();
        file = fileObj;
        calcValue();
    }

    /**
     * 値を計算します。
     */
    protected static void calcValue() {
        value = TextUtil.readUTF8(file);
        if (value != null) {
            value = value.trim();
        }
        lastModified = file.lastModified();
        if (logger.isDebugEnabled()) {
            logger.log("DSSR0114", new Object[] { value, filePath });
        }
    }

    /**
     * 値を計算します。
     * 
     * @param url
     *            URL
     */
    protected static void calcValue(final URL url) {
        final InputStream is = URLUtil.openStream(url);
        try {
            Reader reader = InputStreamReaderUtil.create(is, "UTF-8");
            value = ReaderUtil.readText(reader);
            if (value != null) {
                value = value.trim();
            }
            if (logger.isDebugEnabled()) {
                logger.log("DSSR0114", new Object[] { value, filePath });
            }
        } finally {
            InputStreamUtil.close(is);
        }
    }

    /**
     * 値をクリアします。
     */
    protected static void clearValue() {
        value = null;
        lastModified = 0;
    }
}
