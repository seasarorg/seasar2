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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJBException;
import javax.interceptor.InvocationContext;

import org.aopalliance.intercept.MethodInvocation;

public class InvocationContextImpl implements InvocationContext {
    protected MethodInvocation context;

    protected boolean lifecycleCallback;

    protected Map<String, Object> contextData = new HashMap<String, Object>();

    public InvocationContextImpl(final MethodInvocation context) {
        this(context, false);
    }

    public InvocationContextImpl(final MethodInvocation context,
            final boolean lifecycleCallback) {
        this.context = context;
        this.lifecycleCallback = lifecycleCallback;
    }

    public Object getTarget() {
        return context.getThis();
    }

    public Method getMethod() {
        return context.getMethod();
    }

    public Object[] getParameters() {
        if (lifecycleCallback) {
            throw new IllegalStateException();
        }
        return context.getArguments();
    }

    public void setParameters(final Object[] newParameters) {
        if (lifecycleCallback) {
            throw new IllegalStateException();
        }
        final Object[] oldParameters = getParameters();
        if (newParameters.length != oldParameters.length) {
            throw new EJBException();
        }
        System.arraycopy(newParameters, 0, oldParameters, 0,
                newParameters.length);
    }

    public Map<String, Object> getContextData() {
        return contextData;
    }

    public Object proceed() throws Exception {
        try {
            return context.proceed();
        } catch (final Exception e) {
            throw e;
        } catch (final Error e) {
            throw e;
        } catch (final Throwable t) {
            final EJBException e = new EJBException();
            e.initCause(t);
            throw e;
        }
    }
}
