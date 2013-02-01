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
package org.seasar.framework.container.factory;

/**
 * 
 */
public class Hoge3 {

    /**
     * 
     */
    public static final String COMPONENT = "name = aaa, instance = prototype, autoBinding = property";

    /**
     * 
     */
    public static final String ASPECT = "value=aop.traceInterceptor, pointcut=setAaa";

    /**
     * 
     */
    public static final String INTER_TYPE = "fieldInterType";

    /**
     * 
     */
    public static final String aaa_BINDING = "aaa2";

    /**
     * 
     */
    public static final String bbb_BINDING = "bindingType=none";

    /**
     * 
     */
    public static final String INIT_METHOD = "init";

    /**
     * 
     */
    public static final String DESTROY_METHOD = "destroy";

    /**
     * @param aaa
     */
    @SuppressWarnings("unused")
    public void setAaa(String aaa) {
    }

    /**
     * @param bbb
     */
    @SuppressWarnings("unused")
    public void setBbb(String bbb) {
    }

    /**
     * 
     */
    public void init() {
    }

    /**
     * 
     */
    public void destroy() {
    }
}
