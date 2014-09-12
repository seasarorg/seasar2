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
