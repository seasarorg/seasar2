/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import org.seasar.framework.container.ExternalContext;

/**
 * {@link ExternalContext}用の{@link ComponentDeployerFactory.Provider}です。
 * 
 * @author higa
 * 
 */
public class ExternalComponentDeployerProvider extends
        ComponentDeployerFactory.DefaultProvider {

    public ComponentDeployer createRequestComponentDeployer(
            final ComponentDef cd) {
        return new RequestComponentDeployer(cd);
    }

    public ComponentDeployer createSessionComponentDeployer(
            final ComponentDef cd) {
        return new SessionComponentDeployer(cd);
    }

    public ComponentDeployer createApplicationComponentDeployer(
            final ComponentDef cd) {
        return new ApplicationComponentDeployer(cd);
    }
}