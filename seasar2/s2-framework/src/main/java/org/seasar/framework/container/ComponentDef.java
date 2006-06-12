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
package org.seasar.framework.container;

/**
 * @author higa
 * 
 */
public interface ComponentDef extends ArgDefAware, InterTypeDefAware,
        PropertyDefAware, InitMethodDefAware, DestroyMethodDefAware,
        AspectDefAware, MetaDefAware {

    Object getComponent() throws TooManyRegistrationRuntimeException,
            CyclicReferenceRuntimeException;

    void injectDependency(Object outerComponent);

    S2Container getContainer();

    void setContainer(S2Container container);

    Class getComponentClass();

    String getComponentName();

    void setComponentName(String componentName);

    Class getConcreteClass();

    AutoBindingDef getAutoBindingDef();

    void setAutoBindingDef(AutoBindingDef autoBindingDef);

    InstanceDef getInstanceDef();

    void setInstanceDef(InstanceDef instanceDef);

    Expression getExpression();

    void setExpression(Expression expression);
    
    boolean isExternalBinding();
    
    void setExternalBinding(boolean externalBinding);

    void init();

    void destroy();
}
