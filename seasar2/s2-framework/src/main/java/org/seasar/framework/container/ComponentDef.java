/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import org.seasar.framework.hotswap.Hotswap;

/**
 * @author higa
 *
 */
public interface ComponentDef
	extends
		ArgDefAware,
		PropertyDefAware,
		InitMethodDefAware,
		DestroyMethodDefAware,
		AspectDefAware,
		MetaDefAware {

	public Object getComponent()
		throws TooManyRegistrationRuntimeException, CyclicReferenceRuntimeException;
		
	public void injectDependency(Object outerComponent);

	public S2Container getContainer();

	public void setContainer(S2Container container);

	public Class getComponentClass();

	public String getComponentName();
    
    public void setComponentName(String componentName);

	public Class getConcreteClass();

	public AutoBindingDef getAutoBindingDef();

	public void setAutoBindingDef(AutoBindingDef autoBindingDef);

	public InstanceDef getInstanceDef();

	public void setInstanceDef(InstanceDef instanceDef);
	
	public String getExpression();

	public void setExpression(String expression);

	public void init();

	public void destroy();
    
    public Hotswap getHotswap();
}
