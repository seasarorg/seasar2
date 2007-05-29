/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.mock.servlet;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * モック用の{@link GenericServlet}です。
 * 
 * @author higa
 * 
 */
public class MockServlet extends GenericServlet {

    private static final long serialVersionUID = -2690818043808621124L;

    /**
     * @see javax.servlet.Servlet#service(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse)
     */
    public void service(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
    }

    public void log(String msg) {
    }

}
