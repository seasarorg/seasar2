/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.OperationNotSupportedException;

import org.seasar.framework.container.ComponentNotFoundRuntimeException;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.exception.SRuntimeException;
import org.seasar.framework.util.StringUtil;

/**
 * S2コンテナに<code>JNDI</code>経由でアクセスするためのコンテキストです。
 * 
 * @author higa
 * 
 */
public class JndiContext implements Context {

    /**
     * 環境です。
     */
    protected final Hashtable env;

    /**
     * パスです。
     */
    protected final String path;

    /**
     * {@link JndiContext}を作成します。
     * 
     * @param env
     *            環境
     */
    public JndiContext(final Hashtable env) {
        this.env = env;
        this.path = (String) env.get(PROVIDER_URL);
    }

    public Object addToEnvironment(final String propName, final Object propVal)
            throws NamingException {
        return env.put(propName, propVal);
    }

    public void bind(final Name name, final Object obj) throws NamingException {
        if (name.isEmpty()) {
            throw new NamingException("name is empty");
        }
        bind(JndiResourceLocator.resolveName(name), obj);
    }

    public void bind(final String name, final Object obj)
            throws NamingException {
        bind(JndiResourceLocator.resolveName(name).split("."), obj);
    }

    public void close() throws NamingException {
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
        return SingletonS2ContainerFactory.getContainer().getComponent(
                JndiResourceLocator.resolveName(name));
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

    /**
     * JNDI空間にバインドします。
     * 
     * @param names
     *            名前をデリミタで分割した配列
     * @param obj
     *            オブジェクト
     * @throws NamingException
     *             JNDI例外が発生した場合
     */
    protected void bind(final String[] names, final Object obj)
            throws NamingException {
        final StringBuffer buf = new StringBuffer(100);
        try {
            S2Container context = SingletonS2ContainerFactory.getContainer();
            for (int i = 0; i < names.length - 1; ++i) {
                buf.append(names[i]);
                context = (S2Container) context.getComponent(names[i]);
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

    /**
     * {@link NamingException}を作成します。
     * 
     * @param message
     *            メッセージ
     * @param cause
     *            原因
     * @return {@link NamingException}
     */
    protected NamingException createNamingException(final String message,
            final Throwable cause) {
        final NamingException e = new NamingException(message);
        e.initCause(cause);
        return e;
    }
}
