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
import java.net.URLConnection;

import org.seasar.framework.exception.IORuntimeException;

/**
 * Mimeタイプ用のユーティリティクラスです。
 * 
 * @author shot
 */
public class MimeTypeUtil {

    /**
     * インスタンスを構築します。
     */
    protected MimeTypeUtil() {
    }

    /**
     * コンテントタイプを予想します。
     * 
     * @param path
     * @return コンテントタイプ
     */
    public static String guessContentType(final String path) {
        AssertionUtil.assertNotNull("path is null.", path);
        final InputStream is = ResourceUtil.getResourceAsStream(path);
        String mimetype = null;
        try {
            mimetype = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        if (mimetype == null) {
            mimetype = URLConnection.guessContentTypeFromName(path);
        }
        return mimetype;
    }
}
