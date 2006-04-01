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

import javax.transaction.TransactionManager;

import org.junit.internal.runners.TestMethodRunner;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.unit.annotation.AfterMethod;
import org.seasar.framework.unit.annotation.BeforeMethod;
import org.seasar.framework.unit.annotation.Rollback;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;

/**
 * @author taedium
 * 
 */
public class S2TestMethodRunner extends TestMethodRunner {

    private final Object testTarget;

    private final Method testMethod;

    private final S2FrameworkTestCaseAdapter testCase;

    public S2TestMethodRunner(S2FrameworkTestCase test, Method method,
            RunNotifier notifier, Description description) {

        super(test, method, notifier, description);
        this.testTarget = test;
        this.testMethod = method;
        this.testCase = new S2FrameworkTestCaseAdapter(test);
    }

    @Override
    public void runProtected() {
        try {
            testCase.setUpContainer();
            try {
                super.runProtected();
            } finally {
                testCase.tearDownContainer();
            }
        } catch (Throwable e) {
            addFailure(e);
        }
    }

    @Override
    protected void runUnprotected() {
        try {
            runBeforeEachMethod();
            try {
                testCase.getContainer().init();
                try {
                    testCase.setUpAfterContainerInit();
                    try {
                        testCase.bindFields();
                        testCase.setUpAfterBindFields();
                        try {
                            runUnprotected0();
                        } finally {
                            testCase.tearDownBeforeUnbindFields();
                        }
                    } finally {
                        testCase.tearDownBeforeContainerDestroy();
                    }
                } finally {
                    testCase.getContainer().destroy();
                }
            } finally {
                runAfterEachMethod();
            }
        } catch (Throwable e) {
            addFailure(e);
        }
    }

    protected void runUnprotected0() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = (TransactionManager) testCase
                        .getComponent(TransactionManager.class);
                tm.begin();
            } catch (Throwable t) {
                System.err.println(t);
            }
        }
        try {
            super.runUnprotected();
        } finally {
            if (tm != null) {
                tm.rollback();
            }
        }
    }
    
    protected boolean needTransaction() {
        return testMethod.isAnnotationPresent(Rollback.class);
    }
    
    protected void runBeforeEachMethod() throws Throwable {
        BeforeMethod before = testMethod.getAnnotation(BeforeMethod.class);
        if (before != null) {
            invoke(before.value());
        }
    }

    protected void runAfterEachMethod() throws Throwable {
        AfterMethod after = testMethod.getAnnotation(AfterMethod.class);
        if (after != null) {
            invoke(after.value());
        }
    }

    protected void invoke(String methodName) throws Throwable {
        try {
            Method method = ClassUtil.getMethod(testTarget.getClass(),
                    methodName, null);
            MethodUtil.invoke(method, testTarget, null);
        } catch (NoSuchMethodRuntimeException ignore) {
        }
    }
}
