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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author higa
 */
public interface S2Container extends MetaDefAware {

	public Object getComponent(Object componentKey)
		throws
			ComponentNotFoundRuntimeException,
			TooManyRegistrationRuntimeException,
			CyclicReferenceRuntimeException;

	public Object[] findComponents(Object componentKey)
		throws
			CyclicReferenceRuntimeException;

	public void injectDependency(Object outerComponent)
		throws ClassUnmatchRuntimeException;

	public void injectDependency(Object outerComponent, Class componentClass)
		throws ClassUnmatchRuntimeException;

	public void injectDependency(Object outerComponent, String componentName)
		throws ClassUnmatchRuntimeException;

	public void register(Object component);
	
	public void register(Object component, String componentName);

	public void register(Class componentClass);

	public void register(Class componentClass, String componentName);

	public void register(ComponentDef componentDef);

	public int getComponentDefSize();

	public ComponentDef getComponentDef(int index);

	public ComponentDef getComponentDef(Object componentKey)
		throws ComponentNotFoundRuntimeException;

	public ComponentDef[] findComponentDefs(Object componentKey);

	public boolean hasComponentDef(Object componentKey);
	
	public boolean hasDescendant(String path);
	
	public S2Container getDescendant(String path)
		throws ContainerNotRegisteredRuntimeException;
	
	public void registerDescendant(S2Container descendant);

	public void include(S2Container child);
	
	public int getChildSize();
	
	public S2Container getChild(int index);

	public void init();

	public void destroy();
	
	public String getNamespace();
	
	public void setNamespace(String namespace);
	
	public String getPath();
	
	public void setPath(String path);

	public S2Container getRoot();
	
	public void setRoot(S2Container root);
	
	public HttpServletRequest getRequest();
	
	public void setRequest(HttpServletRequest request);
	
	public HttpSession getSession();
	
	public HttpServletResponse getResponse();
	
	public void setResponse(HttpServletResponse response);
	
	public ServletContext getServletContext();
	
	public void setServletContext(ServletContext servletContext);
    
    public boolean isHotswapMode();
    
    public void setHotswapMode(boolean hotswapMode);
    
    public ClassLoader getClassLoader();
}