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
package org.seasar.framework.container.assembler;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ConstructorAssembler;
import org.seasar.framework.container.MethodAssembler;
import org.seasar.framework.container.PropertyAssembler;

/**
 * コンポーネントアセンブラのファクトリです。
 * 
 * @author higa
 * @author koichik
 */
public class AssemblerFactory {
    private static Provider provider = new DefaultProvider();

    /**
     * プロバイダを返します。
     * 
     * @return
     */
    public static Provider getProvider() {
        return provider;
    }

    /**
     * プロバイダを設定します。 プロバイダを切り替えることで振る舞いをカスタマイズできます。
     * 
     * @param p
     */
    public static void setProvider(final Provider p) {
        provider = p;
    }

    /**
     * initメソッドアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static MethodAssembler createInitMethodAssembler(
            final ComponentDef cd) {
        return getProvider().createInitMethodAssembler(cd);
    }

    /**
     * destroyメソッドアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static MethodAssembler createDestroyMethodAssembler(
            final ComponentDef cd) {
        return getProvider().createDestroyMethodAssembler(cd);
    }

    /**
     * 自動コンストラクタアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static ConstructorAssembler createAutoConstructorAssembler(
            final ComponentDef cd) {
        return getProvider().createAutoConstructorAssembler(cd);
    }

    /**
     * デフォルトコンストラクタ用のコンストラクタアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static ConstructorAssembler createDefaultConstructorConstructorAssembler(
            final ComponentDef cd) {
        return getProvider().createDefaultConstructorConstructorAssembler(cd);
    }

    /**
     * 自動プロパティアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static PropertyAssembler createAutoPropertyAssembler(
            final ComponentDef cd) {
        return getProvider().createAutoPropertyAssembler(cd);
    }

    /**
     * プロパティ定義を明示的に指定した用のプロパティアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static PropertyAssembler createManualOnlyPropertyAssembler(
            final ComponentDef cd) {
        return getProvider().createManualOnlyPropertyAssembler(cd);
    }

    /**
     * EJB3用のプロパティアセンブラを作成します。
     * 
     * @param cd
     * @return
     */
    public static PropertyAssembler createSemiAutoPropertyAssembler(
            final ComponentDef cd) {
        return getProvider().createSemiAutoPropertyAssembler(cd);
    }

    /**
     * コンストラクタアセンブラ、プロパティアセンブラ、 メソッドアセンブラを作成する機能を提供するインターフェースです。
     * 
     */
    public interface Provider {

        /**
         * initメソッドアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        MethodAssembler createInitMethodAssembler(ComponentDef cd);

        /**
         * destroyメソッドアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        MethodAssembler createDestroyMethodAssembler(ComponentDef cd);

        /**
         * 自動コンストラクタアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        ConstructorAssembler createAutoConstructorAssembler(ComponentDef cd);

        /**
         * デフォルトコンストラクタ用のコンストラクタアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        ConstructorAssembler createDefaultConstructorConstructorAssembler(
                ComponentDef cd);

        /**
         * 自動プロパティアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        PropertyAssembler createAutoPropertyAssembler(ComponentDef cd);

        /**
         * プロパティ定義を明示的に指定した用のプロパティアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        PropertyAssembler createManualOnlyPropertyAssembler(ComponentDef cd);

        /**
         * EJB3用のプロパティアセンブラを作成します。
         * 
         * @param cd
         * @return
         */
        PropertyAssembler createSemiAutoPropertyAssembler(ComponentDef cd);
    }

    /**
     * デフォルトのプロバイダ実装です。
     * 
     */
    public static class DefaultProvider implements Provider {

        public MethodAssembler createInitMethodAssembler(final ComponentDef cd) {
            return new DefaultInitMethodAssembler(cd);

        }

        public MethodAssembler createDestroyMethodAssembler(
                final ComponentDef cd) {
            return new DefaultDestroyMethodAssembler(cd);
        }

        public ConstructorAssembler createAutoConstructorAssembler(
                final ComponentDef cd) {
            return new AutoConstructorAssembler(cd);
        }

        public ConstructorAssembler createDefaultConstructorConstructorAssembler(
                final ComponentDef cd) {
            return new DefaultConstructorConstructorAssembler(cd);
        }

        public PropertyAssembler createAutoPropertyAssembler(
                final ComponentDef cd) {
            return new AutoPropertyAssembler(cd);
        }

        public PropertyAssembler createManualOnlyPropertyAssembler(
                final ComponentDef cd) {
            return new ManualOnlyPropertyAssembler(cd);
        }

        public PropertyAssembler createSemiAutoPropertyAssembler(
                final ComponentDef cd) {
            return new SemiAutoPropertyAssembler(cd);
        }
    }
}
