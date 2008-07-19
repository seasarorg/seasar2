/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.dialect;

import java.sql.Types;

import javax.persistence.GenerationType;

/**
 * H2の方言を扱うクラスです。
 * 
 * @author taedium
 */
public class H2GenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public H2GenDialect() {
        dbTypeMap.put(Types.BINARY, H2DbType.BINARY);
        dbTypeMap.put(Types.DECIMAL, H2DbType.DECIMAL);
    }

    @Override
    public String getDefaultSchemaName(String userName) {
        return null;
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public boolean supportsSequence() {
        return true;
    }

    @Override
    public String getSequenceDefinitionFragment(String dataType, int initValue,
            int allocationSize) {
        return "start with " + allocationSize + " increment by " + initValue;
    }

    /**
     * H2用の{@link DbType}の実装です。
     * 
     * @author taedium
     */
    public static class H2DbType extends StandardDbType {

        private static DbType BINARY = new H2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("binary(%d)", length);
            }
        };

        private static DbType DECIMAL = new H2DbType() {

            @Override
            public String getDefinition(int length, int precision, int scale) {
                return format("decimal(%d,%d)", precision, scale);
            }
        };

        /**
         * インスタンスを構築します。
         */
        protected H2DbType() {
        }

        /**
         * インスタンスを構築します。
         * 
         * @param definition
         *            定義
         */
        protected H2DbType(String definition) {
            super(definition);
        }
    }
}
