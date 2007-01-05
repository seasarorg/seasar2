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
package org.seasar.extension.component.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.seasar.extension.component.ComponentInvoker;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.hotdeploy.HotdeployUtil;
import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;

public class ComponentInvokerImpl implements ComponentInvoker {

    private S2Container container;

    public ComponentInvokerImpl() {
    }

    public Object invoke(String componentName, String methodName, Object[] args)
            throws Throwable {

        Object component = container.getRoot().getComponent(componentName);
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(component.getClass());
        try {
            if (HotdeployUtil.isHotdeploy()) {
                redeserialize(args);
            }
            return beanDesc.invoke(component, methodName, args);
        } catch (InvocationTargetRuntimeException e) {
            throw e.getCause();
        }
    }

    public void setContainer(S2Container container) {
        this.container = container;
    }

    protected void redeserialize(final Object[] args) {
        try {
            for (int i = 0; i < args.length; ++i) {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream(
                        1024);
                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(args[i]);
                oos.close();

                final ByteArrayInputStream bais = new ByteArrayInputStream(baos
                        .toByteArray());
                final ObjectInputStream ois = new ObjectInputStream(bais);
                args[i] = ois.readObject();
            }
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }
}