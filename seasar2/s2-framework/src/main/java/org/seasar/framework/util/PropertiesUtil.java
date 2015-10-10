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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link Properties}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class PropertiesUtil {

    /**
     * インスタンスを構築します。
     */
    protected PropertiesUtil() {
    }

    /**
     * {@link Properties#load(InputStream)}の例外処理をラップします。
     * 
     * @param props
     * @param in
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static void load(Properties props, InputStream in)
            throws IORuntimeException {
        try {
            props.load(in);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
