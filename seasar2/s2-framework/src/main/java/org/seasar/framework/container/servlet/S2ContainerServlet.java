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
package org.seasar.framework.container.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.seasar.framework.aop.impl.PointcutImpl;
import org.seasar.framework.container.ArgDef;
import org.seasar.framework.container.ArgDefAware;
import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerNotRegisteredRuntimeException;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.MethodDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.ArgDefImpl;
import org.seasar.framework.container.util.SmartDeployUtil;
import org.seasar.framework.env.Env;
import org.seasar.framework.util.StringUtil;

public class S2ContainerServlet extends HttpServlet {

    private static final long serialVersionUID = 407266935204779128L;

    public static final String CONFIG_PATH_KEY = "configPath";

    public static final String DEBUG_KEY = "debug";

    public static final String COMMAND = "command";

    public static final String RESTART = "restart";

    public static final String LIST = "list";

    public static final String PATH = "path";

    private static S2ContainerServlet instance;

    private boolean debug;

    public S2ContainerServlet() {
        instance = this;
    }

    public static S2ContainerServlet getInstance() {
        return instance;
    }

    public static void clearInstance() {
        instance = null;
    }

    public void init() {
        String configPath = null;
        String debugStr = null;
        ServletConfig servletConfig = getServletConfig();
        if (servletConfig != null) {
            configPath = servletConfig.getInitParameter(CONFIG_PATH_KEY);
            debugStr = servletConfig.getInitParameter(DEBUG_KEY);
        }
        if (!StringUtil.isEmpty(debugStr)) {
            debug = Boolean.valueOf(debugStr).booleanValue();
        }
        initializeContainer(configPath);
    }

    protected void initializeContainer(String configPath) {
        SingletonS2ContainerInitializer initializer = new SingletonS2ContainerInitializer();
        initializer.setConfigPath(configPath);
        initializer.setApplication(getServletContext());
        initializer.initialize();
    }

    public void destroy() {
        SingletonS2ContainerFactory.destroy();
    }

    public static S2Container getContainer() {
        return SingletonS2ContainerFactory.getContainer();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String command = request.getParameter(COMMAND);
        if (debug && command != null && RESTART.equalsIgnoreCase(command)) {
            destroy();
            init();
            response.getWriter().write("S2ContainerServlet is restarted.");
        } else if (debug && LIST.equalsIgnoreCase(command)) {
            list(request, response);
        } else {
            response.getWriter().write("S2ContainerServlet is running.");
        }
    }

    protected void list(final HttpServletRequest request,
            final HttpServletResponse response) throws IOException {
        final PrintWriter out = response.getWriter();

        final String path = request.getParameter(PATH);
        final S2Container container = getContainer(path);
        if (container == null) {
            out.write("S2Container[" + container.getPath() + "] is not found.");
            return;
        }
        out.write("<html><head><title>S2 Components</title></head><body>");
        try {
            out.write("<h1>S2Container</h1>");
            out.write("<ul>");
            try {
                out.write("<li>path : <code>" + container.getPath()
                        + "</code></li>");
                final String nameSpace = container.getNamespace();
                if (!StringUtil.isEmpty(nameSpace)) {
                    out.write("<li>namespace : <code>" + nameSpace
                            + "</code></li>");
                }
            } finally {
                out.write("</ul>");
            }
            final String envValue = Env.getValue();
            if (SmartDeployUtil.isHotdeployMode(container)) {
                out.write("<p>");
                out
                        .write("S2 is working under <strong><font color=\"#DC143C\">hotdeploy</font></strong> mode.[env = "
                                + envValue + "]");
                out.write("</p>");
            } else if (SmartDeployUtil.isWarmdeployMode(container)) {
                out.write("<p>");
                out
                        .write("S2 is working under <strong><font color=\"#FF8C00\">warmdeploy</font></strong> mode.[env = "
                                + envValue + "]");
                out.write("</p>");
            } else if (SmartDeployUtil.isCooldeployMode(container)) {
                out.write("<p>");
                out
                        .write("S2 is working under <strong><font color=\"#00008B\">cooldeploy</font></strong> mode.[env = "
                                + envValue + "]");
                out.write("</p>");
            } else {
                out.write("<p>");
                out.write("S2 is working under normal mode.[env = " + envValue
                        + "]");
                out.write("</p>");
            }
            listInclude(container, request, out);
            listComponent(container, out);
        } finally {
            out.write("</body></html>");
        }
    }

    protected S2Container getContainer(final String path) {
        final S2Container root = SingletonS2ContainerFactory.getContainer();
        try {
            return StringUtil.isEmpty(path) ? root : root.getDescendant(path);
        } catch (final ContainerNotRegisteredRuntimeException e) {
            return null;
        }
    }

    protected void listInclude(final S2Container container,
            final HttpServletRequest request, final PrintWriter out)
            throws IOException {
        if (container.getChildSize() == 0) {
            return;
        }
        out.write("<h2>Includes</h2>");
        out.write("<p><ul>");
        try {
            final String requestUri = request.getRequestURI();
            final String queryString = "?" + COMMAND + "=" + LIST + "&" + PATH
                    + "=";
            for (int i = 0; i < container.getChildSize(); ++i) {
                final S2Container child = container.getChild(i);
                final String path = child.getPath();
                out.write("<li><a href='" + requestUri + queryString + path
                        + "'><code>" + path + "</code></a></li>");
            }
        } finally {
            out.write("</ul></p>");
        }
    }

    protected void listComponent(final S2Container container,
            final PrintWriter out) throws IOException {
        if (container.getComponentDefSize() == 0) {
            return;
        }
        out.write("<h2>Components</h2>");
        out.write("<p><ul>");
        try {
            for (int i = 0; i < container.getComponentDefSize(); ++i) {
                final ComponentDef cd = container.getComponentDef(i);
                printComponent(cd, out);
            }
        } finally {
            out.write("</ul></p>");
        }
    }

    protected void printComponent(final ComponentDef cd, final PrintWriter out)
            throws IOException {
        final String name = cd.getComponentName();
        final Class clazz = cd.getComponentClass();
        out
                .write("<li style='list-style-type: square'><code><strong>"
                        + (name != null ? name : "-") + " ["
                        + (clazz != null ? clazz.getName() : "-")
                        + "]</strong></code>");
        out.write("<ul>");
        out.write("<li style='list-style-type: circle'>instance : <code>"
                + cd.getInstanceDef().getName() + "</code></li>");
        out.write("<li style='list-style-type: circle'>autoBinding : <code>"
                + cd.getAutoBindingDef().getName() + "</code></li>");

        Expression expression = cd.getExpression();
        final String expr = (expression != null) ? expression.toString() : "";
        if (!StringUtil.isEmpty(expr)) {
            out.write("<li style='list-style-type: circle'>ognl : <code>"
                    + expr + "</code></li>");
        }

        printArg(cd, out);
        printAspect(cd, out);
        printProperty(cd, out);
        printInitMethod(cd, out);
        printDestroyMethod(cd, out);

        try {
            final Object component = cd.getComponent();
            out
                    .write("<li style='list-style-type: circle'>toString : <pre style='border-style: solid; border-width: 1'>"
                            + component + "</pre></li>");
        } catch (final Exception ignore) {
        }
        out.write("</ul>");
    }

    protected void printArg(final ArgDefAware cd, final PrintWriter out)
            throws IOException {
        for (int i = 0; i < cd.getArgDefSize(); ++i) {
            out.write("<li style='list-style-type: circle'>arg<ul>");
            final ArgDef ad = cd.getArgDef(i);

            Expression expression = ad.getExpression();
            final String expr = (expression != null) ? expression.toString()
                    : "";
            if (!StringUtil.isEmpty(expr)) {
                out.write("<li style='list-style-type: circle'>ognl : <code>"
                        + expr + "</code></li>");
            }

            final ComponentDef child = getChildComponentDef(ad);
            if (child != null) {
                printComponent(child, out);
            }

            out.write("</ul></li>");
        }
    }

    protected void printAspect(final ComponentDef cd, final PrintWriter out)
            throws IOException {
        for (int i = 0; i < cd.getAspectDefSize(); ++i) {
            out.write("<li style='list-style-type: circle'>aspect<ul>");
            final AspectDef ad = cd.getAspectDef(i);
            final PointcutImpl pc = (PointcutImpl) ad.getPointcut();
            if (pc != null) {
                final String[] pointCuts = pc.getMethodNames();
                if (pointCuts != null && pointCuts.length > 0) {
                    out
                            .write("<li style='list-style-type: circle'>pointcut<ul>");
                    for (int j = 0; j < pointCuts.length; ++j) {
                        out.write("<li style='list-style-type: circle'><code>"
                                + pointCuts[j] + "</code></li>");
                    }
                    out.write("</ul></li>");
                }
            }

            Expression expression = ad.getExpression();
            final String expr = (expression != null) ? expression.toString()
                    : "";
            if (!StringUtil.isEmpty(expr)) {
                out.write("<li style='list-style-type: circle'>ognl : <code>"
                        + expr + "</code></li>");
            }

            final ComponentDef child = getChildComponentDef(ad);
            if (child != null) {
                printComponent(child, out);
            }

            out.write("</ul></li>");
        }
    }

    protected void printProperty(final ComponentDef cd, final PrintWriter out)
            throws IOException {
        for (int i = 0; i < cd.getPropertyDefSize(); ++i) {
            out.write("<li style='list-style-type: circle'>property<ul>");
            final PropertyDef pd = cd.getPropertyDef(i);
            out.write("<li style='list-style-type: circle'>name : <code>"
                    + pd.getPropertyName() + "</code></li>");

            Expression expression = pd.getExpression();
            final String expr = (expression != null) ? expression.toString()
                    : "";
            if (!StringUtil.isEmpty(expr)) {
                out.write("<li style='list-style-type: circle'>ognl : <code>"
                        + expr + "</code></li>");
            }

            final ComponentDef child = getChildComponentDef(pd);
            if (child != null) {
                printComponent(child, out);
            }

            out.write("</ul></li>");
        }
    }

    protected void printInitMethod(final ComponentDef cd, final PrintWriter out)
            throws IOException {
        for (int i = 0; i < cd.getInitMethodDefSize(); ++i) {
            out.write("<li style='list-style-type: circle'>initMethod<ul>");
            printMethod(cd.getInitMethodDef(i), out);
            out.write("</ul></li>");
        }
    }

    protected void printDestroyMethod(final ComponentDef cd,
            final PrintWriter out) throws IOException {
        for (int i = 0; i < cd.getDestroyMethodDefSize(); ++i) {
            out.write("<li style='list-style-type: circle'>destroyMethod<ul>");
            printMethod(cd.getDestroyMethodDef(i), out);
            out.write("</ul></li>");
        }
    }

    protected void printMethod(final MethodDef md, final PrintWriter out)
            throws IOException {
        out.write("<li style='list-style-type: circle'>name : <code>"
                + md.getMethodName() + "</code></li>");

        Expression expression = md.getExpression();
        final String expr = (expression != null) ? expression.toString() : "";
        if (!StringUtil.isEmpty(expr)) {
            out.write("<li style='list-style-type: circle'>ognl : <code>"
                    + expr + "</code></li>");
        }

        final ComponentDef child = getChildComponentDef(md);
        if (child != null) {
            printComponent(child, out);
        }
    }

    protected ComponentDef getChildComponentDef(final Object o) {
        try {
            final Field f = ArgDefImpl.class
                    .getDeclaredField("childComponentDef");
            f.setAccessible(true);
            return (ComponentDef) f.get(o);
        } catch (final Exception e) {
            return null;
        }
    }

}
