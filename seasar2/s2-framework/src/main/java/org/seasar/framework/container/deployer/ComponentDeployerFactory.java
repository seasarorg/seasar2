/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
 * @author higa
 */
public class ComponentDeployerFactory {
    
    private static Provider provider = new DefaultProvider();

    public static Provider getProvider() {
        return provider;
    }

    public static void setProvider(final Provider p) {
        provider = p;
    }

    public static ComponentDeployer createSingletonComponentDeployer(final ComponentDef cd) {
        return getProvider().createSingletonComponentDeployer(cd);
    }
    
    public static ComponentDeployer createPrototypeComponentDeployer(final ComponentDef cd) {
        return getProvider().createPrototypeComponentDeployer(cd);
    }
    
    public static ComponentDeployer createSessionComponentDeployer(final ComponentDef cd) {
        return getProvider().createSessionComponentDeployer(cd);
    }
    
    public static ComponentDeployer createRequestComponentDeployer(final ComponentDef cd) {
        return getProvider().createRequestComponentDeployer(cd);
    }
    
    public static ComponentDeployer createOuterComponentDeployer(final ComponentDef cd) {
        return getProvider().createOuterComponentDeployer(cd);
    }

    public interface Provider {
        
        ComponentDeployer createSingletonComponentDeployer(ComponentDef cd);
        
        ComponentDeployer createPrototypeComponentDeployer(ComponentDef cd);
        
        ComponentDeployer createSessionComponentDeployer(ComponentDef cd);
        
        ComponentDeployer createRequestComponentDeployer(ComponentDef cd);
        
        ComponentDeployer createOuterComponentDeployer(ComponentDef cd);
    }

    public static class DefaultProvider implements Provider {

        public ComponentDeployer createSingletonComponentDeployer(final ComponentDef cd) {
            return new SingletonComponentDeployer(cd);
        }

        public ComponentDeployer createPrototypeComponentDeployer(final ComponentDef cd) {
            return new PrototypeComponentDeployer(cd);
        }

        public ComponentDeployer createRequestComponentDeployer(final ComponentDef cd) {
            return new RequestComponentDeployer(cd);
        }

        public ComponentDeployer createSessionComponentDeployer(final ComponentDef cd) {
            return new SessionComponentDeployer(cd);
        }

        public ComponentDeployer createOuterComponentDeployer(final ComponentDef cd) {
            return new OuterComponentDeployer(cd);
        }

        public ComponentDeployer createDefaultComponentDeployer(final ComponentDef cd) {
            return createOuterComponentDeployer(cd);
        }
    }
}
