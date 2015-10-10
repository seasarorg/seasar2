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

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

/**
 * org.junit.runner.notification.RunNotifier クラスの変更に対応するクラスです。
 *
 * @author manhole
 */
public class RunNotifierCompatibility {

    /**
     * RunNotifier#testAborted メソッドと同等の処理をします。
     * (このメソッドはJUnit4.5で廃止されました)
     * 
     * @param notifier
     *            ノティフィアー
     * @param description
     *            テストのディスクリプション
     * @param e
     *            発生した例外
     */
    public static void testAborted(final RunNotifier notifier,
            final Description description, final Throwable e) {
        notifier.fireTestStarted(description);
        notifier.fireTestFailure(new Failure(description, e));
        notifier.fireTestFinished(description);
    }

}
