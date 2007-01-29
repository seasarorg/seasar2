/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.customizer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.MetaDefImpl;

/**
 * {@link org.seasar.framework.container.ComponentDef コンポーネント定義}に
 * {@link org.seasar.framework.container.MetaDef メタデータ定義}を 登録するコンポーネントカスタマイザです。
 * <p>
 * コンポーネント定義に登録するメタデータ定義は、カスタマイザ自身のコンポーネント定義に <code>autoRegister</code>という名前で定義されたメタデータ定義に設定されたメタデータ定義となります。
 * diconファイルでは次のように記述します。
 * </p>
 * 
 * <pre>
 * &lt;component class=&quot;org.seasar.framework.container.customizer.MetaCustomizer&quot;&gt;
 *   &lt;meta name=&quot;autoRegister&quot;&gt;
 *     &lt;meta name=&quot;remoting&quot;/&gt;
 *   &lt;/meta&gt;
 * &lt;/component&gt;
 * </pre>
 * 
 * </p>
 * この例では、<code>remoting</code>という名前を持つメタデータ定義がコンポーネント定義に 設定されます。
 * <code>autoRegister</code>という名前で定義されたメタデータ定義には、複数のメタデータ定義を
 * 設定することができます。その場合、すべてのメタデータ定義がそのままの順番でコンポーネント定義に 設定されます。
 * </p>
 * 
 * @author koichik
 */
public class MetaCustomizer extends AbstractCustomizer {

    protected ComponentDef componentDef;

    /**
     * コンポーネント定義を設定します。
     * <p>
     * このメソッドは、このコンポーネント自身のコンポーネント定義を引数として、S2コンテナから呼び出されることを意図しています。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    public void setComponentDef(final ComponentDef componentDef) {
        this.componentDef = componentDef;
    }

    /**
     * カスタマイズ対象のコンポーネント定義をカスタマイズをします。
     * <p>
     * このカスタマイザ自身のコンポーネント定義に <code>autoRegister</code>という名前で定義されたメタデータ定義に設定されたメタデータ定義を
     * コンポーネント定義に登録します。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void doCustomize(final ComponentDef cd) {
        final MetaDef metaDef = componentDef.getMetaDef("autoRegister");
        if (metaDef == null) {
            return;
        }
        for (int i = 0; i < metaDef.getMetaDefSize(); ++i) {
            final MetaDef meta = metaDef.getMetaDef(i);
            cd.addMetaDef(new MetaDefImpl(meta.getName(), meta.getValue()));
        }
    }

}
