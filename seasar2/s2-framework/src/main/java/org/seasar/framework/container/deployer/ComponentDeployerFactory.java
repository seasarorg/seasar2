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
package org.seasar.framework.container.deployer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;

/**
 * {@link ComponentDeployer}を作成するクラスです。
 * 
 * @author higa
 */
public class ComponentDeployerFactory {

    private static Provider provider = new DefaultProvider();

    /**
     * {@link Provider}を返します。
     * 
     * @return
     */
    public static Provider getProvider() {
        return provider;
    }

    /**
     * {@link Provider}を設定します。
     * 
     * @param p
     */
    public static void setProvider(final Provider p) {
        provider = p;
    }

    /**
     * singleton用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createSingletonComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createSingletonComponentDeployer(cd);
    }

    /**
     * prototype用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createPrototypeComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createPrototypeComponentDeployer(cd);
    }

    /**
     * application(serlvetContext)用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createServletContextComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createApplicationComponentDeployer(cd);
    }

    /**
     * session用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createSessionComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createSessionComponentDeployer(cd);
    }

    /**
     * request用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createRequestComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createRequestComponentDeployer(cd);
    }

    /**
     * outer用の{@link ComponentDeployer}を作成します。
     * 
     * @param cd
     * @return
     */
    public static ComponentDeployer createOuterComponentDeployer(
            final ComponentDef cd) {
        return getProvider().createOuterComponentDeployer(cd);
    }

    /**
     * {@link ComponentDeployerFactory}をカスタマイズするためのインターフェースです。
     * 
     * @author koichk
     * 
     */
    public interface Provider {

        /**
         * singleton用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createSingletonComponentDeployer(ComponentDef cd);

        /**
         * prototype用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createPrototypeComponentDeployer(ComponentDef cd);

        /**
         * application用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createApplicationComponentDeployer(ComponentDef cd);

        /**
         * session用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createSessionComponentDeployer(ComponentDef cd);

        /**
         * request用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createRequestComponentDeployer(ComponentDef cd);

        /**
         * outer用の{@link ComponentDeployer}を作成します。
         * 
         * @param cd
         * @return
         */
        ComponentDeployer createOuterComponentDeployer(ComponentDef cd);
    }

    /**
     * {@link ComponentDeployerFactory.Provider}のデフォルト実装です。
     * 
     * @author koichk
     * 
     */
    public static class DefaultProvider implements Provider {

        public ComponentDeployer createSingletonComponentDeployer(
                final ComponentDef cd) {
            return new SingletonComponentDeployer(cd);
        }

        public ComponentDeployer createPrototypeComponentDeployer(
                final ComponentDef cd) {
            return new PrototypeComponentDeployer(cd);
        }

        public ComponentDeployer createRequestComponentDeployer(
                final ComponentDef cd) {
            throw new UnsupportedOperationException(
                    "createRequestComponentDeployer");
        }

        public ComponentDeployer createSessionComponentDeployer(
                final ComponentDef cd) {
            throw new UnsupportedOperationException(
                    "createSessionComponentDeployer");
        }

        public ComponentDeployer createApplicationComponentDeployer(
                final ComponentDef cd) {
            throw new UnsupportedOperationException(
                    "createApplicationComponentDeployer");
        }

        public ComponentDeployer createOuterComponentDeployer(
                final ComponentDef cd) {
            return new OuterComponentDeployer(cd);
        }
    }
}