/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.xml;

import java.io.Serializable;

import org.xml.sax.Attributes;

/**
 * XMLのタグを処理するベースクラスです。
 * 
 * @author higa
 * 
 */
public class TagHandler implements Serializable {

    static final long serialVersionUID = 1L;

    /**
     * タグの処理を開始します。
     * 
     * @param context
     * @param attributes
     */
    public void start(TagHandlerContext context, Attributes attributes) {
    }

    /**
     * 追加されたボディを処理します。
     * 
     * @param context
     * @param body
     */
    public void appendBody(TagHandlerContext context, String body) {
    }

    /**
     * タグの処理を終了します。
     * 
     * @param context
     * @param body
     */
    public void end(TagHandlerContext context, String body) {
    }
}
