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
package org.seasar.framework.ejb;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.seasar.framework.log.Logger;

public class EJB3Desc {

    private static final Logger logger = Logger.getLogger(EJB3Desc.class);

    protected static final ConcurrentMap<Class<?>, EJB3Desc> EJB3_DESCS = new ConcurrentHashMap<Class<?>, EJB3Desc>();

    protected Class<?> beanClass;

    protected String beanClassName;

    protected List<Class<?>> businessInterfaces = new ArrayList<Class<?>>();

    protected List<Method> businessMethods = new ArrayList<Method>();

    protected List<Method> allMethods = new ArrayList<Method>();

    protected Method aroundInvokeMethod;

    protected boolean stateless;

    protected boolean stateful;

    protected boolean cmt;

    public static EJB3Desc getEJB3Desc(final Class<?> beanClass) {
        EJB3Desc ejb3Desc = EJB3_DESCS.get(beanClass);
        if (ejb3Desc == null) {
            ejb3Desc = new EJB3Desc(beanClass);
            EJB3_DESCS.putIfAbsent(beanClass, ejb3Desc);
        }
        return ejb3Desc;
    }

    public EJB3Desc(final Class<?> beanClass) {
        this.beanClass = beanClass;
        this.beanClassName = beanClass.getName();
        try {
            introspection();
        } catch (final Exception e) {
            stateless = stateful = false;
            logger.log("ESSR0406", new Object[] { beanClassName, e });
        }
    }

    public boolean isEJB3() {
        return stateless || stateful;
    }

    public boolean isStateless() {
        return stateless;
    }

    public boolean isStateful() {
        return stateful;
    }

    public boolean isCMT() {
        return cmt;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public List<Class<?>> getBusinessInterfaces() {
        return businessInterfaces;
    }

    public List<Method> getBusinessMethods() {
        return businessMethods;
    }

    public List<Method> getAllMethods() {
        return allMethods;
    }

    public Method getAroundInvokeMethod() {
        return aroundInvokeMethod;
    }

    protected void introspection() {
        if (beanClass.isInterface()
                || Modifier.isAbstract(beanClass.getModifiers())) {
            return;
        }

        final Stateless slsb = beanClass.getAnnotation(Stateless.class);
        stateless = slsb != null;
        final Stateful sfsb = beanClass.getAnnotation(Stateful.class);
        stateful = sfsb != null;
        if (!stateless && !stateful) {
            return;
        }
        if (stateless && stateful) {
            throw new SEJBException("ESSR0400", beanClassName);
        }

        findLocalBusinessInterfaces();
        findRemoteBusinessInterfaces();
        if (businessInterfaces.isEmpty()) {
            findImplicitBusinessInterfaces();
            if (businessInterfaces.isEmpty()) {
                throw new SEJBException("ESSR0401", beanClassName);
            }
            if (businessInterfaces.size() > 1) {
                throw new SEJBException("ESSR0402", beanClassName);
            }
        }

        cmt = detectTransactionManagementType();
        findBusinessMethods();
        findAllMethods();
        findAroundInvokeMethod();
        return;
    }

    protected boolean detectTransactionManagementType() {
        final TransactionManagement txManegement = beanClass
                .getAnnotation(TransactionManagement.class);
        if (txManegement == null) {
            return true;
        }

        final TransactionManagementType type = txManegement.value();
        return TransactionManagementType.CONTAINER.equals(type);
    }

    protected void findLocalBusinessInterfaces() {
        final Local local = beanClass.getAnnotation(Local.class);
        if (local == null) {
            return;
        }

        final Class<?>[] localInterfaces = local.value();
        for (final Class<?> type : localInterfaces) {
            if (isBusinessInterface(type)) {
                businessInterfaces.add(type);
            }
        }
    }

    protected void findRemoteBusinessInterfaces() {
        final Remote remote = beanClass.getAnnotation(Remote.class);
        if (remote == null) {
            return;
        }

        final Class<?>[] remoteInterfaces = remote.value();
        for (final Class<?> type : remoteInterfaces) {
            if (isBusinessInterface(type)) {
                businessInterfaces.add(type);
            }
        }
    }

    protected void findImplicitBusinessInterfaces() {
        for (final Class<?> type : beanClass.getInterfaces()) {
            if (isBusinessInterface(type)) {
                businessInterfaces.add(type);
            }
        }
    }

    protected void findBusinessMethods() {
        for (final Method method : beanClass.getMethods()) {
            final int modifier = method.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isFinal(modifier)) {
                continue;
            }
            if (!isBusinessMethod(method)) {
                continue;
            }
            businessMethods.add(method);
        }
    }

    protected void findAllMethods() {
        for (Class<?> clazz = beanClass; clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            for (final Method method : clazz.getDeclaredMethods()) {
                allMethods.add(method);
            }
        }
    }

    protected void findAroundInvokeMethod() {
        for (final Method method : allMethods) {
            final AroundInvoke aroundInvoke = method
                    .getAnnotation(AroundInvoke.class);
            if (aroundInvoke == null) {
                continue;
            }
            if (aroundInvokeMethod != null) {
                throw new SEJBException("ESSR0403", beanClassName);
            }

            final Class<?>[] paramTypes = method.getParameterTypes();
            if (paramTypes == null || paramTypes.length != 1
                    || !InvocationContext.class.equals(paramTypes[0])) {
                throw new SEJBException("ESSR0404", beanClassName, method.getName());
            }

            final Class<?> returnType = method.getReturnType();
            if (returnType == null || !Object.class.equals(returnType)) {
                throw new SEJBException("ESSR0405", beanClassName, method.getName());
            }

            aroundInvokeMethod = method;
        }
    }

    protected boolean isBusinessInterface(final Class<?> type) {
        if (!type.isInterface()) {
            throw new SEJBException("ESSR0407", beanClassName, type.getName());
        }
        if (!type.isAssignableFrom(beanClass)) {
            throw new SEJBException("ESSR0408", beanClassName, type.getName());
        }
        if (Serializable.class.equals(type)) {
            return false;
        }
        if (Exception.class.equals(type)) {
            return false;
        }
        if (type.getName().startsWith("javax.ejb")) {
            return false;
        }
        return true;
    }

    protected boolean isBusinessMethod(final Method method) {
        for (final Class<?> type : businessInterfaces) {
            try {
                type.getDeclaredMethod(method.getName(), method
                        .getParameterTypes());
                return true;
            } catch (final NoSuchMethodException ignore) {
            }
        }
        return false;
    }
}
