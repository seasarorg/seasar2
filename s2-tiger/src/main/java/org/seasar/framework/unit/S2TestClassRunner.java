/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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

import org.junit.internal.runners.InitializationError;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;

/**
 * テストクラスを扱うランナーです。
 * 
 * @author taedium
 */
public class S2TestClassRunner extends Runner implements Filterable, Sortable {

    private final Runner delegate;

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            テストクラス
     * @param runner
     *            委譲先のランナー
     * @throws InitializationError
     *             初期化時に何らかのエラーが発生した場合
     */
    public S2TestClassRunner(final Class<?> clazz, final Runner runner)
            throws InitializationError {
        delegate = runner;
        final S2MethodValidator methodValidator = new S2MethodValidator(clazz);
        validate(methodValidator);
        methodValidator.assertValid();
    }

    /**
     * メソッドの検証を実行します。
     * 
     * @param methodValidator
     *            メソッドバリデータ
     */
    protected void validate(final S2MethodValidator methodValidator) {
        methodValidator.validateMethodsForDefaultRunner();
    }

    @Override
    public void run(final RunNotifier notifier) {
        delegate.run(notifier);
    }

    @Override
    public Description getDescription() {
        return delegate.getDescription();
    }

    public void filter(Filter filter) throws NoTestsRemainException {
        FilterCompatibility.apply(filter, delegate);
    }

    public void sort(Sorter sorter) {
        SorterCompatibility.apply(sorter, delegate);
    }
}
