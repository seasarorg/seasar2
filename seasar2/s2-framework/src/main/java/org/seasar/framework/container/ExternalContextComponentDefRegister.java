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
package org.seasar.framework.container;

/**
 * 外部コンテキストが提供するコンポーネント定義を、 S2コンテナに登録します。
 * <p>
 * <code>ExternalContextComponentDefRegister</code>が外部コンテキストの{@link ComponentDef コンポーネント定義}を登録することにより、
 * {@link ExternalContext}インターフェースを通して、 外部コンテキストのコンポーネントを取得できるようになります。
 * </p>
 * <p>
 * コンポーネントを取得可能な外部コンテキストの種類については、 {@link ExternalContext}インターフェースを参照して下さい。
 * </p>
 * 
 * @author higa
 * @author belltree
 */
public interface ExternalContextComponentDefRegister {

    /**
     * 指定されたS2コンテナに、 外部コンテキストのコンポーネント定義を登録します。
     * 
     * @param container
     *            S2コンテナ
     */
    void registerComponentDefs(S2Container container);
}