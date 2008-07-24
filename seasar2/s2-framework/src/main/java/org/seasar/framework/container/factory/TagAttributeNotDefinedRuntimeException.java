/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 特定のタグにおいて、必要な属性が定義されていない場合にスローされます。
 * 
 * @author higa
 * @author yatsu
 */
public class TagAttributeNotDefinedRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = -1635401319074240988L;

    private String tagName;

    private String attributeName;

    /**
     * <code>TagAttributeNotDefinedRuntimeException</code>を構築します。
     * 
     * @param tagName
     *            タグ名
     * @param attributeName
     *            属性名
     */
    public TagAttributeNotDefinedRuntimeException(String tagName,
            String attributeName) {

        super("ESSR0056", new Object[] { tagName, attributeName });
        this.tagName = tagName;
        this.attributeName = attributeName;
    }

    /**
     * タグ名を返します。
     * 
     * @return タグ名
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * 属性名を返します。
     * 
     * @return 属性名
     */
    public String getAttributeName() {
        return attributeName;
    }
}
