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
package org.seasar.framework.hotswap;

import java.io.File;

import org.seasar.framework.util.FileUtil;

public class HotswapClassLoader extends ClassLoader {
    
	public HotswapClassLoader() {
		this(Thread.currentThread().getContextClassLoader());
	}
    
    public HotswapClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

	public Class defineClass(String className, File classFile) {
		return defineClass(className, FileUtil.getBytes(classFile));
	}

	public Class defineClass(String className, byte[] bytes) {
		return defineClass(className, bytes, 0, bytes.length);
	}
}
