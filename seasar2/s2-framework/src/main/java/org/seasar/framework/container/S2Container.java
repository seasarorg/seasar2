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
 *
 * @author higa
 */
public interface S2Container extends MetaDefAware {

	Object getComponent(Object componentKey)
		throws
			ComponentNotFoundRuntimeException,
			TooManyRegistrationRuntimeException,
			CyclicReferenceRuntimeException;

	Object[] findComponents(Object componentKey)
		throws
			CyclicReferenceRuntimeException;

	void injectDependency(Object outerComponent)
		throws ClassUnmatchRuntimeException;

	void injectDependency(Object outerComponent, Class componentClass)
		throws ClassUnmatchRuntimeException;

	void injectDependency(Object outerComponent, String componentName)
		throws ClassUnmatchRuntimeException;

	void register(Object component);
	
	void register(Object component, String componentName);

	void register(Class componentClass);

	void register(Class componentClass, String componentName);

	void register(ComponentDef componentDef);

	int getComponentDefSize();

	ComponentDef getComponentDef(int index);

	ComponentDef getComponentDef(Object componentKey)
		throws ComponentNotFoundRuntimeException;

	ComponentDef[] findComponentDefs(Object componentKey);

	boolean hasComponentDef(Object componentKey);
	
	boolean hasDescendant(String path);
	
	S2Container getDescendant(String path)
		throws ContainerNotRegisteredRuntimeException;
	
	void registerDescendant(S2Container descendant);

	void include(S2Container child);
	
	int getChildSize();
	
	S2Container getChild(int index);
    
	int getParentSize();
    
    S2Container getParent(int index);
    
    void addParent(S2Container parent);

	void init();

	void destroy();
	
	String getNamespace();
	
	void setNamespace(String namespace);
	
	String getPath();
	
	void setPath(String path);

	S2Container getRoot();
	
	void setRoot(S2Container root);
	
	ExternalContext getExternalContext();
	
	void setExternalContext(ExternalContext externalContext);
    
	ExternalContextComponentDefRegister getExternalContextComponentDefRegister();
    
    void setExternalContextComponentDefRegister(ExternalContextComponentDefRegister externalContextComponentDefRegister);
    
    boolean isHotswapMode();
    
    void setHotswapMode(boolean hotswapMode);
    
    ClassLoader getClassLoader();
}