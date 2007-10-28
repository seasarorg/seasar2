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
package org.seasar.extension.jdbc.it;

import org.seasar.extension.jdbc.JdbcManagerImplementor;
import org.seasar.extension.jdbc.dialect.H2Dialect;
import org.seasar.extension.jdbc.dialect.HsqlDialect;

/**
 * @author taedium
 * 
 */
public class S2JdbcItUtil {

    private S2JdbcItUtil() {
    }

    /**
     * プロシージャをテスト対象とする場合{@code true}を返します。
     * 
     * @param jdbcManagerImplementor
     * @return テスト対象とする場合{@code true}
     */
    public static boolean supportsProcedure(
            JdbcManagerImplementor jdbcManagerImplementor) {
        if (jdbcManagerImplementor.getDialect() instanceof HsqlDialect
            || jdbcManagerImplementor.getDialect() instanceof H2Dialect) {
            return false;
        }
        return true;
    }
}
