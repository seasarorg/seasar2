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
package org.seasar.extension.j2ee;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * @author higa
 * 
 */
public class JndiContext implements Context {
    public static final String ENC_PREFIX = "java:comp/env/";

    //public static final String ENC_NAMESPACE = "j2ee";

    protected static final Map MAGIC_COMPONENTS = new HashMap();
    static {
        MAGIC_COMPONENTS
                .put("java:comp/UserTransaction", "jta/UserTransaction");
    }

    protected final Hashtable env;

    protected final String path;

    protected S2Container container;

    public JndiContext(final Hashtable env) throws NamingException {
        this.env = env;
        this.path = (String) env.get(PROVIDER_URL);
        this.container = SingletonS2ContainerFactory.getContainer();
    }

    public Object addToEnvironment(final String propName, final Object propVal)
            throws NamingException {
        return env.put(propName, propVal);
    }

    public void bind(final Name name, final Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new NamingException("name is empty");
        }
        bind(resolveName(name), obj);
    }

    public void bind(final String name, final Object obj)
            throws NamingException {
        bind(resolveName(name).split("/"), obj);
    }

    public void close() throws NamingException {
        container = null;
    }

    public Name composeName(final Name name, final Name prefix)
            throws NamingException {
        throw new OperationNotSupportedException("composeName");
    }

    public String composeName(final String name, final String prefix)
            throws NamingException {
        throw new OperationNotSupportedException("composeName");
    }

    public Context createSubcontext(final Name name) throws NamingException {
        throw new OperationNotSupportedException("createSubcontext");
    }

    public Context createSubcontext(final String name) throws NamingException {
        throw new OperationNotSupportedException("createSubcontext");
    }

    public void destroySubcontext(final Name name) throws NamingException {
        throw new OperationNotSupportedException("destroySubcontext");
    }

    public void destroySubcontext(final String name) throws NamingException {
        throw new OperationNotSupportedException("destroySubcontext");
    }

    public Hashtable getEnvironment() throws NamingException {
        return env;
    }

    public String getNameInNamespace() throws NamingException {
        throw new OperationNotSupportedException("getNameInNamespace");
    }

    public NameParser getNameParser(final Name name) throws NamingException {
        throw new OperationNotSupportedException("getNameParser");
    }

    public NameParser getNameParser(final String name) throws NamingException {
        throw new OperationNotSupportedException("getNameParser");
    }

    public NamingEnumeration list(final Name name) throws NamingException {
        throw new OperationNotSupportedException("list");
    }

    public NamingEnumeration list(final String name) throws NamingException {
        throw new OperationNotSupportedException("list");
    }

    public NamingEnumeration listBindings(final Name name)
            throws NamingException {
        throw new OperationNotSupportedException("listBindings");
    }

    public NamingEnumeration listBindings(final String name)
            throws NamingException {
        throw new OperationNotSupportedException("listBindings");
    }

    public Object lookup(final Name name) throws NamingException {
        if (name.isEmpty()) {
            return new JndiContext(new Hashtable(env));
        }
        return lookup(name.toString());
    }

    public Object lookup(final String name) throws NamingException {
        if (StringUtil.isEmpty(name)) {
            return new JndiContext(new Hashtable(env));
        }
        return container.getComponent(StringUtil.replace(resolveName(name), "/", "."));
    }

    public Object lookupLink(final Name name) throws NamingException {
        throw new OperationNotSupportedException("lookupLink");
    }

    public Object lookupLink(final String name) throws NamingException {
        throw new OperationNotSupportedException("lookupLink");
    }

    public void rebind(final Name name, final Object obj)
            throws NamingException {
        throw new OperationNotSupportedException("rebind");
    }

    public void rebind(final String name, final Object obj)
            throws NamingException {
        throw new OperationNotSupportedException("rebind");
    }

    public Object removeFromEnvironment(final String propName)
            throws NamingException {
        return env.remove(propName);
    }

    public void rename(final Name oldName, final Name newName)
            throws NamingException {
        throw new OperationNotSupportedException("rename");
    }

    public void rename(final String oldName, final String newName)
            throws NamingException {
        throw new OperationNotSupportedException("rename");
    }

    public void unbind(final Name name) throws NamingException {
        throw new OperationNotSupportedException("unbind");
    }

    public void unbind(final String name) throws NamingException {
        throw new OperationNotSupportedException("unbind");
    }

    protected void bind(final String[] names, final Object obj)
            throws NamingException {
        final StringBuffer buf = new StringBuffer(100);
        try {
            S2Container context = container;
            for (int i = 0; i < names.length - 1; ++i) {
                buf.append(names[i]);
                context = (S2Container) container.getComponent(names[i]);
                buf.append('/');
            }
            final String name = names[names.length - 1];
            buf.append(name);
            if (context.hasComponentDef(name)) {
                throw new NameAlreadyBoundException(new String(buf));
            }
            context.register(obj, name);
        } catch (final ComponentNotFoundRuntimeException e) {
            throw createNamingException(new String(buf), e);
        } catch (final SRuntimeException e) {
            throw createNamingException(e.getMessage(), e);
        }
    }
/*
    protected Object lookup(final String[] names) throws NamingException {
        //final StringBuffer buf = new StringBuffer(100);
        try {
            S2Container context = container;
            for (int i = 0; i < names.length - 1; ++i) {
                //buf.append(names[i]);
                context = (S2Container) context.getComponent(names[i]);
                //buf.append('/');
            }
            final String name = names[names.length - 1];
            //buf.append(name);
            return context.getComponent(name);
        } catch (final ComponentNotFoundRuntimeException e) {
            final StringBuffer buf = new StringBuffer(100);
            for (int i = 0; i < names.length - 1; ++i) {
                buf.append(names[i]);
                buf.append('/');
                buf.append(names[names.length - 1]);
            }
            throw createNamingException(new String(buf), e);
        } catch (final SRuntimeException e) {
            throw createNamingException(e.getMessage(), e);
        }
    }
*/
    protected String resolveName(final Name name) {
        /*
        StringBuffer buf = new StringBuffer(100);
        for (int i = 0; i < name.size(); ++i) {
            buf.append(name.get(i));
            buf.append("/");
        }
        buf.setLength(buf.length() - 1);
        return resolveName(buf.toString());
        */
        return resolveName(name.toString());
    }
    
    protected String resolveName(final String name) {
        String n = name;
        if (MAGIC_COMPONENTS.containsKey(name)) {
            n = (String) MAGIC_COMPONENTS.get(name);
        }
        if (name.startsWith(ENC_PREFIX)) {
            n = name.substring(ENC_PREFIX.length());
        }
        return StringUtil.replace(n, "/", ContainerConstants.NS_SEP_STR);
    }
/*
    protected String toStringArray(final Name name) {
        final String[] names = new String[name.size()];
        for (int i = 0; i < name.size(); ++i) {
            names[i] = name.get(i);
        }
        return names;
    }
*/
    protected NamingException createNamingException(final String message,
            final Throwable cause) {
        final NamingException e = new NamingException(message);
        e.initCause(cause);
        return e;
    }
}
