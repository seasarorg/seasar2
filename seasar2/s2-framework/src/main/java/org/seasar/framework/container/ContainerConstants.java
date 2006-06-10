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
public interface ContainerConstants {

    char NS_SEP = '.';

    char PACKAGE_SEP = '_';

    String NS_SEP_STR = Character.toString(NS_SEP);

    String CONTAINER_NAME = "container";

    String REQUEST_NAME = "request";

    String RESPONSE_NAME = "response";

    String SESSION_NAME = "session";
    
    String SERVLET_CONTEXT_NAME = "application";
    
    String APPLICATION_SCOPE = "applicationScope";
    
    String INIT_PARAM = "initParam";
    
    String SESSION_SCOPE = "sessionScope";

    String COOKIE = "cookie";

    String HEADER = "header";

    String HEADER_VALUES = "headerValues";

    String PARAM = "param";

    String PARAM_VALUES = "paramValues";

    String REQUEST_SCOPE = "requestScope";

    

    String COMPONENT_DEF_NAME = "componentDef";
}
