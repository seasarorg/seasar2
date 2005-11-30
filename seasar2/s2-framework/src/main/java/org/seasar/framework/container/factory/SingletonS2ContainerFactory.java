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
package org.seasar.framework.container.factory;

import javax.servlet.ServletContext;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.exception.EmptyRuntimeException;

public final class SingletonS2ContainerFactory {

	private static String configPath = "app.dicon";
	private static ServletContext servletContext;
	private static S2Container container;
	
	private SingletonS2ContainerFactory() {
	}
	
	public static String getConfigPath() {
		return configPath;
	}
	
	public static void setConfigPath(String path) {
		configPath = path;
	}
	
	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	public static void setServletContext(ServletContext context) {
		servletContext = context;
	}

	public static void init() {
		container = S2ContainerFactory.create(configPath);
		container.setServletContext(servletContext);
		container.init();
	}
	
	public static void destroy() {
		container.destroy();
		container = null;
	}
	
	public static S2Container getContainer() {
		if (container == null) {
			throw new EmptyRuntimeException("S2Container");
		}
		return container;
	}
	
	public static void setContainer(S2Container c) {
		container = c;
	}
	
	public static boolean hasContainer() {
		return container != null;
	}
}
