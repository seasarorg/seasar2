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
package org.seasar.framework.mock.sql;

import java.sql.Types;

/**
 * @author higa
 * 
 */
public class MockColumnMetaData {

    private String columnClassName;

    private int columnDisplaySize;

    private String columnLabel;

    private String columnName;

    private int columnType = Types.VARCHAR;

    private String columnTypeName;

    private int precision;

    private int scale;

    private String catalogName;

    private String schemaName;

    private String tableName;

    private boolean autoIncrement;

    private boolean caseSensitive;

    private boolean currency;

    private boolean definitelyWritable;

    private int nullable;

    private boolean readOnly;

    private boolean searchable;

    private boolean signed;

    private boolean writable;

    /**
     * 自動インクリメントかどうかを返します。
     * 
     * @return 自動インクリメントかどうか
     */
    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    /**
     * 自動インクリメントかどうかを設定します。
     * 
     * @param autoIncrement
     *            自動インクリメントかどうか
     */
    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    /**
     * ケースセンシティブかどうかを返します。
     * 
     * @return ケースセンシティブかどうか
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    /**
     * ケースセンシティブかどうかを設定します。
     * 
     * @param caseSensitive
     *            ケースセンシティブかどうか
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    /**
     * カタログ名を返します。
     * 
     * @return カタログ名
     */
    public String getCatalogName() {
        return catalogName;
    }

    /**
     * カタログ名を設定します。
     * 
     * @param catalogName
     *            カタログ名
     */
    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    /**
     * カラムのクラス名を返します。
     * 
     * @return カラムのクラス名
     */
    public String getColumnClassName() {
        return columnClassName;
    }

    /**
     * カラムのクラス名を設定します。
     * 
     * @param columnClassName
     *            カラムのクラス名
     */
    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    /**
     * カラムの表示サイズを返します。
     * 
     * @return カラムの表示サイズ
     */
    public int getColumnDisplaySize() {
        return columnDisplaySize;
    }

    /**
     * カラムの表示サイズを設定します。
     * 
     * @param columnDisplaySize
     *            The columnDisplaySize to set.
     */
    public void setColumnDisplaySize(int columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    /**
     * カラムのラベルを返します。
     * 
     * @return カラムのラベル
     */
    public String getColumnLabel() {
        return columnLabel;
    }

    /**
     * カラムのラベルを設定します。
     * 
     * @param columnLabel
     *            カラムのラベル
     */
    public void setColumnLabel(String columnLabel) {
        this.columnLabel = columnLabel;
    }

    /**
     * カラム名を返します。
     * 
     * @return カラム名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * カラム名を設定します。
     * 
     * @param columnName
     *            カラム名
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * カラムのタイプを返します。
     * 
     * @return カラムのタイプ
     */
    public int getColumnType() {
        return columnType;
    }

    /**
     * カラムのタイプを設定します。
     * 
     * @param columnType
     *            カラムのタイプ
     */
    public void setColumnType(int columnType) {
        this.columnType = columnType;
    }

    /**
     * カラムのタイプ名を返します。
     * 
     * @return カラムのタイプ名
     */
    public String getColumnTypeName() {
        return columnTypeName;
    }

    /**
     * カラムのタイプ名を設定します。
     * 
     * @param columnTypeName
     *            カラムのタイプ名
     */
    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    /**
     * 通貨かどうかを返します。
     * 
     * @return 通貨かどうか
     */
    public boolean isCurrency() {
        return currency;
    }

    /**
     * 通貨かどうかを設定します。
     * 
     * @param currency
     *            通貨かどうか
     */
    public void setCurrency(boolean currency) {
        this.currency = currency;
    }

    /**
     * 厳密に書き込み可能かどうかを返します。
     * 
     * @return 厳密に書き込み可能かどうか
     */
    public boolean isDefinitelyWritable() {
        return definitelyWritable;
    }

    /**
     * 厳密に書き込み可能かどうかを設定します。
     * 
     * @param definitelyWritable
     *            厳密に書き込み可能かどうか
     */
    public void setDefinitelyWritable(boolean definitelyWritable) {
        this.definitelyWritable = definitelyWritable;
    }

    /**
     * <code>null</code>が可能かどうかを返します。
     * 
     * @return <code>null</code>が可能かどうか
     */
    public int isNullable() {
        return nullable;
    }

    /**
     * <code>null</code>が可能かどうかを設定します。
     * 
     * @param nullable
     *            <code>null</code>が可能かどうか
     */
    public void setNullable(int nullable) {
        this.nullable = nullable;
    }

    /**
     * 精度を返します。
     * 
     * @return 精度
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * 精度を設定します。
     * 
     * @param precision
     *            精度
     */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * 読み取り専用かどうかを返します。
     * 
     * @return 読み取り専用かどうか
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * 読み取り専用かどうかを設定します。
     * 
     * @param readOnly
     *            読み取り専用かどうか
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * スケールを返します。
     * 
     * @return スケール
     */
    public int getScale() {
        return scale;
    }

    /**
     * スケールを設定します。
     * 
     * @param scale
     *            スケール
     */
    public void setScale(int scale) {
        this.scale = scale;
    }

    /**
     * スキーマ名を返します。
     * 
     * @return スキーマ名
     */
    public String getSchemaName() {
        return schemaName;
    }

    /**
     * スキーマ名を設定します。
     * 
     * @param schemaName
     *            スキーマ名
     */
    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    /**
     * WHERE句で使えるかどうかを返します。
     * 
     * @return Returns the searchable.
     */
    public boolean isSearchable() {
        return searchable;
    }

    /**
     * WHERE句で使えるかどうかを設定します。
     * 
     * @param searchable
     *            WHERE句で使えるかどうか
     */
    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    /**
     * 符号付の数字かどうかを返します。
     * 
     * @return 符号付の数字かどうか
     */
    public boolean isSigned() {
        return signed;
    }

    /**
     * 符号付の数字かどうかを設定します。
     * 
     * @param signed
     *            符号付の数字かどうか
     */
    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    /**
     * テーブル名を返します。
     * 
     * @return テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * テーブル名を設定します。
     * 
     * @param tableName
     *            テーブル名
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 書き込み可能かどうかを返します。
     * 
     * @return 書き込み可能かどうか
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * 書き込み可能かどうかを設定します。
     * 
     * @param writable
     *            書き込み可能かどうか
     */
    public void setWritable(boolean writable) {
        this.writable = writable;
    }
}