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
package org.seasar.extension.j2ee;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * @author higa
 *
 */
public class JndiContext implements Context {

	private Hashtable env_;
	private S2Container container_;

	public JndiContext(Hashtable env) {
		env_ = env;
		container_ = SingletonS2ContainerFactory.getContainer();
	}

	/**
	 * @see javax.naming.Context#close()
	 */
	public void close() throws NamingException {
		container_ = null;
	}

	/**
	 * @see javax.naming.Context#getNameInNamespace()
	 */
	public String getNameInNamespace() throws NamingException {
		throw new UnsupportedOperationException("getNameInNamespace");
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(java.lang.String)
	 */
	public void destroySubcontext(String name) throws NamingException {
		throw new UnsupportedOperationException("destroySubcontext");
	}

	/**
	 * @see javax.naming.Context#unbind(java.lang.String)
	 */
	public void unbind(String name) throws NamingException {
		throw new UnsupportedOperationException("unbind");
	}

	/**
	 * @see javax.naming.Context#getEnvironment()
	 */
	public Hashtable getEnvironment() throws NamingException {
		return env_;
	}

	/**
	 * @see javax.naming.Context#destroySubcontext(javax.naming.Name)
	 */
	public void destroySubcontext(Name name) throws NamingException {
		throw new UnsupportedOperationException("destroySubcontext");

	}

	/**
	 * @see javax.naming.Context#unbind(javax.naming.Name)
	 */
	public void unbind(Name name) throws NamingException {
		throw new UnsupportedOperationException("unbind");
	}

	/**
	 * @see javax.naming.Context#lookup(java.lang.String)
	 */
	public Object lookup(String name) throws NamingException {
		return container_.getComponent(name);
	}

	/**
	 * @see javax.naming.Context#lookupLink(java.lang.String)
	 */
	public Object lookupLink(String name) throws NamingException {
		throw new UnsupportedOperationException("lookupLink");
	}

	/**
	 * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
	 */
	public Object removeFromEnvironment(String propName)
		throws NamingException {

		return env_.remove(propName);
	}

	/**
	 * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
	 */
	public void bind(String name, Object obj) throws NamingException {
		container_.register(obj, name);
	}

	/**
	 * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
	 */
	public void rebind(String name, Object obj) throws NamingException {
		throw new UnsupportedOperationException("rebind");
	}

	/**
	 * @see javax.naming.Context#lookup(javax.naming.Name)
	 */
	public Object lookup(Name name) throws NamingException {
		return container_.getComponent(name.toString());
	}

	/**
	 * @see javax.naming.Context#lookupLink(javax.naming.Name)
	 */
	public Object lookupLink(Name name) throws NamingException {
		throw new UnsupportedOperationException("lookupLink");
	}

	/**
	 * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
	 */
	public void bind(Name name, Object obj) throws NamingException {
		container_.register(obj, name.toString());
	}

	/**
	 * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
	 */
	public void rebind(Name name, Object obj) throws NamingException {
		throw new UnsupportedOperationException("rebind");
	}

	/**
	 * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
	 */
	public void rename(String oldName, String newName) throws NamingException {
		throw new UnsupportedOperationException("rename");
	}

	/**
	 * @see javax.naming.Context#createSubcontext(java.lang.String)
	 */
	public Context createSubcontext(String name) throws NamingException {
		throw new UnsupportedOperationException("createSubcontext");
	}

	/**
	 * @see javax.naming.Context#createSubcontext(javax.naming.Name)
	 */
	public Context createSubcontext(Name name) throws NamingException {
		throw new UnsupportedOperationException("createSubcontext");
	}

	/**
	 * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
	 */
	public void rename(Name oldName, Name newName) throws NamingException {
		throw new UnsupportedOperationException("rename");
	}

	/**
	 * @see javax.naming.Context#getNameParser(java.lang.String)
	 */
	public NameParser getNameParser(String name) throws NamingException {
		throw new UnsupportedOperationException("getNameParser");
	}

	/**
	 * @see javax.naming.Context#getNameParser(javax.naming.Name)
	 */
	public NameParser getNameParser(Name name) throws NamingException {
		throw new UnsupportedOperationException("getNameParser");
	}

	/**
	 * @see javax.naming.Context#list(java.lang.String)
	 */
	public NamingEnumeration list(String name) throws NamingException {
		throw new UnsupportedOperationException("list");
	}

	/**
	 * @see javax.naming.Context#listBindings(java.lang.String)
	 */
	public NamingEnumeration listBindings(String name) throws NamingException {
		throw new UnsupportedOperationException("listBindings");
	}

	/**
	 * @see javax.naming.Context#list(javax.naming.Name)
	 */
	public NamingEnumeration list(Name name) throws NamingException {
		throw new UnsupportedOperationException("list");
	}

	/**
	 * @see javax.naming.Context#listBindings(javax.naming.Name)
	 */
	public NamingEnumeration listBindings(Name name) throws NamingException {
		throw new UnsupportedOperationException("listBindings");
	}

	/**
	 * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
	 */
	public Object addToEnvironment(String propName, Object propVal)
		throws NamingException {

		return env_.put(propName, propVal);
	}

	/**
	 * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
	 */
	public String composeName(String name, String prefix)
		throws NamingException {

		throw new UnsupportedOperationException("composeName");
	}

	/**
	 * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
	 */
	public Name composeName(Name name, Name prefix) throws NamingException {
		throw new UnsupportedOperationException("composeName");
	}
}
