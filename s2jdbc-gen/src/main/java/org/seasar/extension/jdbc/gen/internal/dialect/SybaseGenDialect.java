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
package org.seasar.extension.jdbc.gen.internal.dialect;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.gen.internal.sqltype.BinaryType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BlobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.BooleanType;
import org.seasar.extension.jdbc.gen.internal.sqltype.ClobType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DateType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DecimalType;
import org.seasar.extension.jdbc.gen.internal.sqltype.DoubleType;
import org.seasar.extension.jdbc.gen.internal.sqltype.IntegerType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimeType;
import org.seasar.extension.jdbc.gen.internal.sqltype.TimestampType;

/**
 * Sybaseの方言を扱うクラスです。
 * 
 * @author taedium
 */
public class SybaseGenDialect extends StandardGenDialect {

    /**
     * インスタンスを構築します。
     */
    public SybaseGenDialect() {
        sqlTypeMap.put(Types.BINARY, new BinaryType("varbinary($l)"));
        sqlTypeMap.put(Types.BOOLEAN, new BooleanType("bit"));
        sqlTypeMap.put(Types.BLOB, new BlobType("image"));
        sqlTypeMap.put(Types.CLOB, new ClobType("text"));
        sqlTypeMap.put(Types.DATE, new DateType("datetime"));
        sqlTypeMap.put(Types.DECIMAL, new DecimalType("decimal($p,$s)"));
        sqlTypeMap.put(Types.DOUBLE, new DoubleType("double precision"));
        sqlTypeMap.put(Types.INTEGER, new IntegerType("int"));
        sqlTypeMap.put(Types.TIME, new TimeType("datetime"));
        sqlTypeMap.put(Types.TIMESTAMP, new TimestampType("datetime"));

        columnTypeMap.put("binary", SybaseColumnType.BINARY);
        columnTypeMap.put("bit", SybaseColumnType.BIT);
        columnTypeMap.put("datetime", SybaseColumnType.DATETIME);
        columnTypeMap.put("decimal", SybaseColumnType.DECIMAL);
        columnTypeMap.put("image", SybaseColumnType.IMAGE);
        columnTypeMap.put("int", SybaseColumnType.INT);
        columnTypeMap.put("money", SybaseColumnType.MONEY);
        columnTypeMap.put("nchar", SybaseColumnType.NCHAR);
        columnTypeMap.put("ntext", SybaseColumnType.NTEXT);
        columnTypeMap.put("numeric", SybaseColumnType.NUMERIC);
        columnTypeMap.put("nvarchar", SybaseColumnType.NVARCHAR);
        columnTypeMap.put("smalldatetime", SybaseColumnType.SMALLDATETIME);
        columnTypeMap.put("smallmoney", SybaseColumnType.SMALLMONEY);
        columnTypeMap.put("text", SybaseColumnType.TEXT);
        columnTypeMap.put("varbinary", SybaseColumnType.VARBINARY);
    }

    @Override
    public String getName() {
        return "sybase";
    }

    @Override
    public GenerationType getDefaultGenerationType() {
        return GenerationType.IDENTITY;
    }

    @Override
    public String getSqlBlockDelimiter() {
        return "go";
    }

    @Override
    public String getIdentityColumnDefinition() {
        return "identity not null";
    }

    @Override
    public SqlBlockContext createSqlBlockContext() {
        return new SybaseSqlBlockContext();
    }

    @Override
    public boolean supportsIdentity() {
        return true;
    }

    /**
     * Sybase用の{@link ColumnType}の実装です。
     * 
     * @author taedium
     */
    public static class SybaseColumnType extends StandardColumnType {

        private static SybaseColumnType BINARY = new SybaseColumnType(
                "binary($l)", byte[].class);

        private static SybaseColumnType BIT = new SybaseColumnType("bit",
                Boolean.class);

        private static SybaseColumnType DATETIME = new SybaseColumnType(
                "datetime", Timestamp.class);

        private static SybaseColumnType DECIMAL = new SybaseColumnType(
                "decimal($p,$s)", BigDecimal.class);

        private static SybaseColumnType IMAGE = new SybaseColumnType("image",
                byte[].class, true);

        private static SybaseColumnType INT = new SybaseColumnType("int",
                Integer.class);

        private static SybaseColumnType MONEY = new SybaseColumnType("money",
                BigDecimal.class);

        private static SybaseColumnType NCHAR = new SybaseColumnType(
                "nchar($l)", BigDecimal.class);

        private static SybaseColumnType NTEXT = new SybaseColumnType("ntext",
                BigDecimal.class);

        private static SybaseColumnType NUMERIC = new SybaseColumnType(
                "numeric($p,$s)", BigDecimal.class);

        private static SybaseColumnType NVARCHAR = new SybaseColumnType(
                "nvarchar($l)", String.class);

        private static SybaseColumnType SMALLDATETIME = new SybaseColumnType(
                "smalldatetime", Timestamp.class);

        private static SybaseColumnType SMALLMONEY = new SybaseColumnType(
                "smallmoney", BigDecimal.class);

        private static SybaseColumnType TEXT = new SybaseColumnType("text",
                String.class);

        private static SybaseColumnType VARBINARY = new SybaseColumnType(
                "varbinary($l)", byte[].class);

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性クラス
         */
        public SybaseColumnType(String dataType, Class<?> attributeClass) {
            super(dataType, attributeClass);
        }

        /**
         * インスタンスを構築します。
         * 
         * @param dataType
         *            データ型
         * @param attributeClass
         *            属性のクラス
         * @param lob
         *            LOBの場合{@code true}
         */
        public SybaseColumnType(String dataType, Class<?> attributeClass,
                boolean lob) {
            super(dataType, attributeClass, lob);
        }

    }

    /**
     * Sybase用の{@link StandardColumnType}の実装クラスです。
     * 
     * @author taedium
     */
    public static class SybaseSqlBlockContext extends StandardSqlBlockContext {

        /**
         * インスタンスを構築します。
         */
        protected SybaseSqlBlockContext() {
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("create", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "procedure"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "function"));
            sqlBlockStartKeywordsList.add(Arrays.asList("alter", "trigger"));
            sqlBlockStartKeywordsList.add(Arrays.asList("declare"));
            sqlBlockStartKeywordsList.add(Arrays.asList("begin"));
        }
    }
}
