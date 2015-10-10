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
package org.seasar.framework.util;

import java.io.File;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * ファイルをHOTに読み込むクラスです。
 * 
 * @author higa
 * 
 */
public class HotText {

    private String path;

    private String value;

    private File file;

    private long lastModified;

    /**
     * {@link HotText}を作成します。
     */
    public HotText() {
    }

    /**
     * {@link HotText}を作成します。
     * 
     * @param path
     */
    public HotText(String path) {
        setPath(path);
    }

    /**
     * パスを返します。
     * 
     * @return パス
     */
    public String getPath() {
        return path;
    }

    /**
     * パスを設定します。
     * 
     * @param path
     * @throws EmptyRuntimeException
     *             パスが<code>null</code>の場合
     */
    public void setPath(String path) throws EmptyRuntimeException {
        if (path == null) {
            throw new EmptyRuntimeException("path");
        }
        this.path = path;
        file = ResourceUtil.getResourceAsFileNoException(path);
        if (file != null) {
            updateValueByFile();
        } else {
            updateValueByPath();
        }
    }

    /**
     * ファイルの中身を文字列で返します。
     * 
     * @return ファイルの中身
     */
    public String getValue() {
        if (isModified()) {
            updateValueByFile();
        }
        return value;
    }

    /**
     * 値を設定します。
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * 更新されているかどうかを返します。
     * 
     * @return 更新されているかどうか
     */
    public boolean isModified() {
        return file != null && file.lastModified() > lastModified;
    }

    /**
     * {@link File}からデータを読み込みます。
     */
    protected void updateValueByFile() {
        value = TextUtil.readUTF8(file);
        lastModified = file.lastModified();
    }

    /**
     * {@link ClassLoader}を使ってデータを読み込みます。 この場合、ファイルの変更をHOTに認識することはできません。
     */
    protected void updateValueByPath() {
        value = TextUtil.readUTF8(path);
    }
}