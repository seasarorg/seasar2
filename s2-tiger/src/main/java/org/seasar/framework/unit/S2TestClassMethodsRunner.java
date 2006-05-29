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
package org.seasar.framework.unit;

import java.lang.reflect.Method;

import org.junit.internal.runners.TestClassMethodsRunner;
import org.junit.internal.runners.TestMethodRunner;
import org.junit.runner.notification.RunNotifier;

/**
 * @author taedium
 * 
 */
public class S2TestClassMethodsRunner extends TestClassMethodsRunner {
    public S2TestClassMethodsRunner(Class clazz) {
        super(clazz);
    }

    @Override
    protected TestMethodRunner createMethodRunner(Object test, Method method,
            RunNotifier notifier) {

        if (test instanceof S2FrameworkTestCase) {
            return new S2TestMethodRunner((S2FrameworkTestCase) test, method,
                    notifier, methodDescription(method));
        }

        return super.createMethodRunner(test, method, notifier);
    }
}