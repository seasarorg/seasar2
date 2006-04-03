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
import org.seasar.framework.unit.annotation.Rollback;
import org.seasar.framework.util.StringUtil;

/**
 * @author taedium
 * 
 */
public class S2TestMethodRunner extends TestMethodRunner {

    private final S2FrameworkTestCase test;

    private final Method testMethod;

    public S2TestMethodRunner(S2FrameworkTestCase test, Method method,
            RunNotifier notifier, Description description) {

        super(test, method, notifier, description);
        this.test = test;
        this.testMethod = method;
    }

    @Override
    public void runProtected() {
        try {
            test.setUpContainer();
            try {
                super.runProtected();
            } finally {
                test.tearDownContainer();
            }
        } catch (Throwable e) {
            addFailure(e);
        }
    }

    @Override
    protected void runUnprotected() {
        try {
            setUpForEachTestMethod();
            try {
                test.getContainer().init();
                try {
                    test.setUpAfterContainerInit();
                    try {
                        test.bindFields();
                        test.setUpAfterBindFields();
                        try {
                            runUnprotected0();
                        } finally {
                            test.tearDownBeforeUnbindFields();
                        }
                    } finally {
                        test.tearDownBeforeContainerDestroy();
                    }
                } finally {
                    test.getContainer().destroy();
                }
            } finally {
                tearDownForEachTestMethod();
            }
        } catch (Throwable e) {
            addFailure(e);
        }
    }

    protected void runUnprotected0() throws Throwable {
        TransactionManager tm = null;
        if (needTransaction()) {
            try {
                tm = (TransactionManager) test
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
        return testMethod.isAnnotationPresent(Rollback.class)
                || testMethod.getName().endsWith("Tx");
    }

    protected void setUpForEachTestMethod() throws Throwable {
        test.invoke("setUp" + StringUtil.capitalize(testMethod.getName()));
    }

    protected void tearDownForEachTestMethod() throws Throwable {
        test.invoke("tearDown" + StringUtil.capitalize(testMethod.getName()));
    }

}
