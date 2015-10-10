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
package org.seasar.framework.beans.util;

import java.util.Map;

/**
 * プロパティのコピーをするクラスです。
 * 
 * @author higa
 * 
 */
public class Copy extends AbstractCopy<Copy> {

    /**
     * コピー元です。
     */
    protected Object src;

    /**
     * コピー先です。
     */
    protected Object dest;

    /**
     * インスタンスを構築します。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     * @throws NullPointerException
     *             引数が<code>null</code>だった場合
     */
    public Copy(Object src, Object dest) throws NullPointerException {
        if (src == null) {
            throw new NullPointerException("src");
        }
        if (dest == null) {
            throw new NullPointerException("dest");
        }
        this.src = src;
        this.dest = dest;
    }

    /**
     * プロパティをコピーします。
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        if (src instanceof Map && dest instanceof Map) {
            copyMapToMap((Map) src, (Map) dest);
        } else if (src instanceof Map) {
            copyMapToBean((Map) src, dest);
        } else if (dest instanceof Map) {
            copyBeanToMap(src, (Map) dest);
        } else {
            copyBeanToBean(src, dest);
        }
    }
}