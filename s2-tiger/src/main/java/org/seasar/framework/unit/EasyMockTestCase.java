/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import junit.framework.TestCase;

import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.StringUtil;

/**
 * テンプレートメソッドパターンに従った手続きでEasyMockを利用できるようにサポートするクラスです。
 * 
 * @author koichik
 */
public abstract class EasyMockTestCase extends TestCase {

    // instance fields
    /** EasyMockとの対話をサポートするオブジェクト */
    protected EasyMockSupport easyMockSupport = new EasyMockSupport();

    /**
     * インスタンスを構築します。
     */
    public EasyMockTestCase() {
    }

    /**
     * 名前を指定してインスタンスを構築します。
     * 
     * @param name
     *            テストケースの名前
     */
    public EasyMockTestCase(final String name) {
        super(name);
    }

    @Override
    public void runBare() throws Throwable {
        easyMockSupport.clear();
        setUp();
        try {
            bindFields();
            try {
                doRunTest();
            } finally {
                unbindFields();
            }
        } finally {
            tearDown();
        }
    }

    /**
     * モックをフィールドにバインディングします。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void bindFields() throws Throwable {
        easyMockSupport.bindMockFields(this, null);
    }

    /**
     * テストを実行します。
     * 
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected void doRunTest() throws Throwable {
        final boolean recorded = doRecord();
        if (recorded) {
            easyMockSupport.replay();
        }
        runTest();
        if (recorded) {
            easyMockSupport.verify();
            easyMockSupport.reset();
        }
    }

    /**
     * モックの振る舞いを記録します。
     * 
     * @return モックの振る舞いが記録するメソッドが存在する場合<code>true</code>、存在しない場合<code>false</code>
     * @throws Throwable
     *             何らかの例外またはエラーが発生した場合
     */
    protected boolean doRecord() throws Throwable {
        final String targetName = getTargetName();
        if (!StringUtil.isEmpty(targetName)) {
            try {
                final Method method = ClassUtil.getMethod(getClass(), "record"
                        + targetName, null);
                MethodUtil.invoke(method, this, null);
                return true;
            } catch (final NoSuchMethodRuntimeException ignore) {
            }
        }
        return false;
    }

    /**
     * テストケースの名前からプレフィックス<code>test</code>を除いた名前を返します。
     * 
     * @return テストケースの名前からプレフィックス<code>test</code>を除いた名前
     */
    protected String getTargetName() {
        return getName().substring(4);
    }

    /**
     * モックとフィールドのバインディングを解除します。
     */
    protected void unbindFields() {
        easyMockSupport.unbindMockFields(this);
    }

    /**
     * デフォルトのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象となるクラス
     * @return 作成されたモック
     */
    protected <T> T createMock(final Class<T> clazz) {
        return easyMockSupport.createMock(clazz);
    }

    /**
     * Niceモードのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象となるクラス
     * @return 作成されたモック
     */
    protected <T> T createNiceMock(final Class<T> clazz) {
        return easyMockSupport.createNiceMock(clazz);
    }

    /**
     * Strictモードのモックを作成します。
     * 
     * @param <T>
     *            モックの型
     * @param clazz
     *            モックの対象となるクラス
     * @return 作成されたモック
     */
    protected <T> T createStrictMock(final Class<T> clazz) {
        return easyMockSupport.createStrictMock(clazz);
    }

    /**
     * EasyMockの利用に必要な一連のメソッド呼び出しを1つのテンプレートメソッドとして提供する抽象クラスです。
     * 
     * @author taedium
     */
    protected abstract class Subsequence {

        /**
         * テストを実行します。
         * <p>
         * テストは次の順序で行われます。
         * <ul>
         * <li>モックの振る舞いの記録する</li>
         * <li>モックのモードをreplayモードに設定する</li>
         * <li>モックとのインタラクションを再現する</li>
         * <li>モックとのインタラクションを検証する</li>
         * <li>モックをリセットする</li>
         * </ul>
         * </p>
         * 
         * @throws Exception
         *             何らかの例外が発生した場合
         */
        public void doTest() throws Exception {
            record();
            EasyMockTestCase.this.easyMockSupport.replay();
            replay();
            EasyMockTestCase.this.easyMockSupport.verify();
            EasyMockTestCase.this.easyMockSupport.reset();
        }

        /**
         * モックの振る舞いを記録します。
         * 
         * @throws Exception
         *             何らかの例外が発生した場合
         */
        protected void record() throws Exception {
        }

        /**
         * モックとのインタラクションを再現します。
         * 
         * @throws Exception
         *             何らかの例外が発生した場合
         */
        protected abstract void replay() throws Exception;
    }
}
