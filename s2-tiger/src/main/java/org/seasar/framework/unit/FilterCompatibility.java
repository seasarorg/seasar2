package org.seasar.framework.unit;

import java.lang.reflect.Method;

import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.util.MethodUtil;

/**
 * org.junit.runner.manipulation.Filter クラスのシグニチャ変更に対応するクラスです。
 *
 * JUnit 4.4: public void apply(Runner)
 * ↓
 * JUnit 4.5: public void apply(Object)
 *
 *
 * @author manhole
 */
public class FilterCompatibility {

    @SuppressWarnings("unchecked")
    private static final Class[] APPLY_PARAM_TYPES = new Class[] { Object.class };

    /**
     * Filter#apply(filterable)メソッドを実行します。
     * 
     * @param filter
     *            フィルタ
     * @param filterable
     *            フィルタ対象
     * @throws NoTestsRemainException
     *             フィルタ処理によって全てのテストが除外された場合
     */
    public static void apply(final Filter filter, final Runner filterable)
            throws NoTestsRemainException {
        try {
            filter.apply(filterable);
        } catch (final NoSuchMethodError ignore) {
            // JUnit 4.5 or higher
            apply45(filter, filterable);
        }
    }

    private static void apply45(final Filter filter, final Object filterable) {
        final BeanDesc beanDesc = BeanDescFactory
                .getBeanDesc(filter.getClass());
        final Method method = beanDesc.getMethod("apply", APPLY_PARAM_TYPES);
        MethodUtil.invoke(method, filter, new Object[] { filterable });
    }

}
