/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.tx.adapter;

import org.easymock.IAnswer;
import org.seasar.extension.tx.TransactionCallback;

import com.ibm.websphere.uow.UOWSynchronizationRegistry;
import com.ibm.wsspi.uow.UOWAction;
import com.ibm.wsspi.uow.UOWManager;

import static com.ibm.websphere.uow.UOWSynchronizationRegistry.*;

import static org.easymock.EasyMock.*;

/**
 * {@link WAS6TransactionManagerAdapter}のテストクラスです。
 * <p>
 * メソッド名は次のようになっています。
 * </p>
 * 
 * <pre>
 * test&lt;TXATTR&gt;_&lt;CONTEXT&gt;_&lt;BEHAVIOR&gt;()
 * </pre>
 * 
 * <dl>
 * <dt>&lt;TXATTR&gt;</dt>
 * <dd>テスト対象のトランザクション属性</dd>
 * <dt>&lt;CONTEXT&gt;</dt>
 * <dd>現在のスレッド上でトランザクションが開始されているか (<code>withTx</code>)いないか(<code>withoutTx()</code></dd>
 * <dt>&lt;BEHAVIOR&gt;</dt>
 * <dd>{@link TransactionCallback}の振る舞い。 リターンしてコミット (<code>returnCommit</code>)、
 * リターンしてロールバック (<code>returnRollback)、
 * 例外をスローしてコミット (<code>exceptionCommit</code>)、 例外をスローしてロールバック (<code>exceptionRollback)
 * のいすれか</dd>
 * </dl>
 * 
 * @author koichik
 */
public class WAS6TransactionManagerAdapterTest extends
        AbstTransactionManagerAdapterTest {

    WAS6TransactionManagerAdapter target;

    UOWManager uowManager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        uowManager = createStrictMock(UOWManager.class);
        target = new WAS6TransactionManagerAdapter(uowManager);
    }

    /**
     * @throws Throwable
     */
    public void testRequired_returnCommit() throws Throwable {
        assertEquals("hoge", target.required(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_returnCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(true), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_returnRollback() throws Throwable {
        assertEquals("hoge", target.required(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_returnRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(true), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_exceptionCommit() throws Throwable {
        try {
            target.required(new ExceptionCommitCallback());
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_exceptionCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(true), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testRequired_exceptionRollback() throws Throwable {
        try {
            target.required(new ExceptionRollbackCallback());
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequired_exceptionRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(true), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_returnCommit() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_returnCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_returnRollback() throws Throwable {
        assertEquals("hoge", target.requiresNew(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_returnRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_exceptionCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testRequiresNew_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .requiresNew(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordRequiresNew_exceptionRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_GLOBAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_returnCommit() throws Throwable {
        try {
            assertEquals("hoge", target.mandatory(new ReturnCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_returnCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_returnRollback() throws Throwable {
        try {
            assertEquals("hoge", target.mandatory(new ReturnRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_returnRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_exceptionCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withoutTx_exceptionRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.mandatory(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_returnCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.mandatory(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_returnRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_exceptionCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testMandatory_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .mandatory(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordMandatory_withTx_exceptionRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_returnCommit() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_returnCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_LOCAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_returnRollback() throws Throwable {
        assertEquals("hoge", target.notSupported(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_returnRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_LOCAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_exceptionCommit() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_LOCAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
    }

    /**
     * @throws Throwable
     */
    public void testNotSupported_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target
                    .notSupported(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNotSupported_exceptionRollback() throws Throwable {
        uowManager.runUnderUOW(
                eq(UOWSynchronizationRegistry.UOW_TYPE_LOCAL_TRANSACTION),
                eq(false), isA(UOWAction.class));
        expectLastCall().andAnswer(new InvokeAction()).asStub();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_returnCommit() throws Throwable {
        assertEquals("hoge", target.never(new ReturnCommitCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_returnCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_returnRollback() throws Throwable {
        assertEquals("hoge", target.never(new ReturnRollbackCallback()));
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_returnRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionCommitCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_exceptionCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withoutTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionRollbackCallback()));
            fail();
        } catch (Exception expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withoutTx_exceptionRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_NONE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_returnCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ReturnCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_returnCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_returnRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ReturnRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_returnRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_exceptionCommit() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionCommitCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_exceptionCommit() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testNever_withTx_exceptionRollback() throws Throwable {
        try {
            assertEquals("hoge", target.never(new ExceptionRollbackCallback()));
            fail();
        } catch (IllegalStateException expected) {
        }
    }

    /**
     * @throws Throwable
     */
    public void recordNever_withTx_exceptionRollback() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
    }

    /**
     * @throws Throwable
     */
    public void testSetRollbackOnly() throws Throwable {
        target.setRollbackOnly();
        target.setRollbackOnly();
    }

    /**
     * @throws Throwable
     */
    public void recordSetRollbackOnly() throws Throwable {
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ACTIVE);
        uowManager.setRollbackOnly();
        expect(uowManager.getUOWStatus()).andReturn(UOW_STATUS_ROLLBACKONLY);
    }

    /**
     * @author koichik
     */
    public static class InvokeAction implements IAnswer<Object> {

        public Object answer() throws Throwable {
            Object[] args = getCurrentArguments();
            UOWAction action = UOWAction.class.cast(args[2]);
            action.run();
            return null;
        }

    }

}
