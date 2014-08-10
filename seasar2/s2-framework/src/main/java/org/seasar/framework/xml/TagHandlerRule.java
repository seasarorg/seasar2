/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
import java.util.HashMap;
import java.util.Map;

/**
 * {@link TagHandler}を登録するクラスです。
 * 
 * @author higa
 * 
 */
public class TagHandlerRule implements Serializable {

    static final long serialVersionUID = 1L;

    private Map tagHandlers = new HashMap();

    /**
     * {@link TagHandlerRule}を作成します。
     */
    public TagHandlerRule() {
    }

    /**
     * {@link TagHandler}を追加します。
     * 
     * @param path
     * @param tagHandler
     */
    public final void addTagHandler(String path, TagHandler tagHandler) {
        tagHandlers.put(path, tagHandler);
    }

    /**
     * {@link TagHandler}を返します。
     * 
     * @param path
     * @return {@link TagHandler}
     */
    public final TagHandler getTagHandler(String path) {
        return (TagHandler) tagHandlers.get(path);
    }
}
