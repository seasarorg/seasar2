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
package org.seasar.framework.container.customizer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.MetaDefImpl;

/**
 * {@link org.seasar.framework.container.ComponentDef コンポーネント定義}に{@link org.seasar.framework.container.MetaDef メタデータ定義}を登録するコンポーネントカスタマイザです。
 * <p>
 * コンポーネント定義に登録するメタデータ定義は、 カスタマイザ自身のコンポーネント定義に定義した<code>autoRegister</code>という名前のメタデータ定義の中に定義します。
 * <code>autoRegister</code>というメタデータ定義は、 登録対象のメタデータ定義を保持します。 実際に適用したい定義自身も、
 * メタデータ定義として定義します。 diconファイルでは、 次のように記述します。
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
 * この例では、 <code>remoting</code>という名前を持つメタデータ定義がカスタマイズ対象のコンポーネント定義に設定されます。
 * 登録対象のメタデータ定義を保持する<code>autoRegister</code>という名前のメタデータ定義には、
 * 複数のメタデータ定義を設定することができます。 その場合、 すべてのメタデータ定義がそのままの順番でカスタマイズ対象のコンポーネント定義に設定されます。
 * </p>
 * 
 * @author koichik
 * @author jundu
 */
public class MetaCustomizer extends AbstractCustomizer {

    /**
     * このコンポーネント自身の{@link ComponentDef コンポーネント定義}です。
     */
    protected ComponentDef componentDef;

    /**
     * {@link ComponentDef コンポーネント定義}を設定します。
     * <p>
     * このメソッドは、 このコンポーネント自身のコンポーネント定義を引数として、 {@link S2Container S2コンテナ}から呼び出されることを意図しています。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    public void setComponentDef(final ComponentDef componentDef) {
        this.componentDef = componentDef;
    }

    /**
     * カスタマイズ対象の{@link ComponentDef コンポーネント定義}をカスタマイズします。
     * <p>
     * {@link org.seasar.framework.container.MetaDef メタデータ定義}をコンポーネント定義に登録します。
     * </p>
     * 
     * @param componentDef
     *            コンポーネント定義
     */
    protected void doCustomize(final ComponentDef componentDef) {
        final MetaDef metaDef = getMetaDef();
        if (metaDef == null) {
            return;
        }
        for (int i = 0; i < metaDef.getMetaDefSize(); ++i) {
            final MetaDef meta = metaDef.getMetaDef(i);
            componentDef.addMetaDef(new MetaDefImpl(meta.getName(), meta
                    .getValue()));
        }
    }

    /**
     * 登録対象の{@link org.seasar.framework.container.MetaDef メタデータ定義}を保持するメタデータ定義を返します。
     * <p>
     * このカスタマイザ自身の{@link ComponentDef コンポーネント定義}に<code>autoRegister</code>という名前で定義された、
     * 登録対象のメタデータ定義を保持するメタデータ定義を返します。
     * </p>
     * 
     * @return 登録対象のメタデータ定義を保持するメタデータ定義
     */
    protected MetaDef getMetaDef() {
        return componentDef.getMetaDef("autoRegister");
    }

}
