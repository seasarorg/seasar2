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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DDLをまとめたモデルです。
 * 
 * @author taedium
 */
public class SummaryDdlModel {

    /** テーブルを作成するDDLのテンプレートファイル名 */
    protected String createTableTemplateFileName;

    /** スキーマ情報テーブルを作成するDDLのテンプレートファイル名 */
    protected String createSchemaInfoTableTemplateFileName;

    /** 一意キーを作成するDDLのテンプレートファイル名 */
    protected String createUniqueKeyTemplateFileName;

    /** シーケンスを生成するDDLのテンプレートファイル */
    protected String createSequenceTemplateFileName;

    /** 外部キーを作成するDDLのテンプレートファイル名 */
    protected String createForeignKeyTemplateFileName;

    /** テーブルを削除するDDLのテンプレートファイル名 */
    protected String dropTableTemplateFileName;

    /** スキーマ情報テーブルを削除するDDLのテンプレートファイル名 */
    protected String dropSchemaInfoTableTemplateFileName;

    /** 一意キーを削除するDDLのテンプレートファイル名 */
    protected String dropUniqueKeyTemplateFileName;

    /** シーケンスを削除するDDLのテンプレートファイル名 */
    protected String dropSequenceTemplateFileName;

    /** 外部キーを削除するDDLのテンプレートファイル名 */
    protected String dropForeignKeyTemplateFileName;

    /** テーブルモデルのリスト */
    protected List<TableModel> tableModelList = new ArrayList<TableModel>();

    /** スキーマ情報テーブルのモデル */
    protected SchemaInfoTableModel schemaInfoTableModel;

    /**
     * テーブルを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを作成するDDLのテンプレートファイル名
     */
    public String getCreateTableTemplateFileName() {
        return createTableTemplateFileName;
    }

    /**
     * テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createTableTemplateFileName
     *            テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateTableTemplateFileName(
            String createTableTemplateFileName) {
        this.createTableTemplateFileName = createTableTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを作成するDDLのテンプレートファイル名
     */
    public String getCreateSequenceTemplateFileName() {
        return createSequenceTemplateFileName;
    }

    /**
     * シーケンスを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSequenceTemplateFileName
     *            シーケンスを作成するDDLのテンプレートファイル名
     */
    public void setCreateSequenceTemplateFileName(
            String createSequenceTemplateFileName) {
        this.createSequenceTemplateFileName = createSequenceTemplateFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return テーブルを削除するDDLのテンプレートファイル名
     */
    public String getDropTableTemplateFileName() {
        return dropTableTemplateFileName;
    }

    /**
     * テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropTableTemplateFileName
     *            テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropTableTemplateFileName(String dropTableTemplateFileName) {
        this.dropTableTemplateFileName = dropTableTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return シーケンスを削除するDDLのテンプレートファイル名
     */
    public String getDropSequenceTemplateFileName() {
        return dropSequenceTemplateFileName;
    }

    /**
     * シーケンスを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSequenceTemplateFileName
     *            シーケンスを削除するDDLのテンプレートファイル名
     */
    public void setDropSequenceTemplateFileName(
            String dropSequenceTemplateFileName) {
        this.dropSequenceTemplateFileName = dropSequenceTemplateFileName;
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return 一意キーを作成するDDLのテンプレートファイル名
     */
    public String getCreateUniqueKeyTemplateFileName() {
        return createUniqueKeyTemplateFileName;
    }

    /**
     * 一意キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createUniqueKeyTemplateFileName
     *            一意キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateUniqueKeyTemplateFileName(
            String createUniqueKeyTemplateFileName) {
        this.createUniqueKeyTemplateFileName = createUniqueKeyTemplateFileName;
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return 外部キーを作成するDDLのテンプレートファイル名
     */
    public String getCreateForeignKeyTemplateFileName() {
        return createForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createForeignKeyTemplateFileName
     *            外部キーを作成するDDLのテンプレートファイル名
     */
    public void setCreateForeignKeyTemplateFileName(
            String createForeignKeyTemplateFileName) {
        this.createForeignKeyTemplateFileName = createForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 外部キーを削除するDDLのテンプレートファイル名
     */
    public String getDropForeignKeyTemplateFileName() {
        return dropForeignKeyTemplateFileName;
    }

    /**
     * 外部キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropForeignKeyTemplateFileName
     *            外部キーを削除するDDLのテンプレートファイル名
     */
    public void setDropForeignKeyTemplateFileName(
            String dropForeignKeyTemplateFileName) {
        this.dropForeignKeyTemplateFileName = dropForeignKeyTemplateFileName;
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return 一意キーを削除するDDLのテンプレートファイル名
     */
    public String getDropUniqueKeyTemplateFileName() {
        return dropUniqueKeyTemplateFileName;
    }

    /**
     * 一意キーを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropUniqueKeyTemplateFileName
     *            一意キーを削除するDDLのテンプレートファイル名
     */
    public void setDropUniqueKeyTemplateFileName(
            String dropUniqueKeyTemplateFileName) {
        this.dropUniqueKeyTemplateFileName = dropUniqueKeyTemplateFileName;
    }

    /**
     * スキーマ情報テーブルを作成するDDLのテンプレートファイル名を返します。
     * 
     * @return スキーマ情報テーブルを作成するDDLのテンプレートファイル名
     */
    public String getCreateSchemaInfoTableTemplateFileName() {
        return createSchemaInfoTableTemplateFileName;
    }

    /**
     * スキーマ情報テーブルを作成するDDLのテンプレートファイル名を設定します。
     * 
     * @param createSchemaInfoTableTemplateFileName
     *            スキーマ情報テーブルを作成するDDLのテンプレートファイル名
     */
    public void setCreateSchemaInfoTableTemplateFileName(
            String createSchemaInfoTableTemplateFileName) {
        this.createSchemaInfoTableTemplateFileName = createSchemaInfoTableTemplateFileName;
    }

    /**
     * スキーマ情報テーブルを削除するDDLのテンプレートファイル名を返します。
     * 
     * @return スキーマ情報テーブルを削除するDDLのテンプレートファイル名
     */
    public String getDropSchemaInfoTableTemplateFileName() {
        return dropSchemaInfoTableTemplateFileName;
    }

    /**
     * スキーマ情報テーブルを削除するDDLのテンプレートファイル名を設定します。
     * 
     * @param dropSchemaInfoTableTemplateFileName
     *            スキーマ情報テーブルを削除するDDLのテンプレートファイル名
     */
    public void setDropSchemaInfoTableTemplateFileName(
            String dropSchemaInfoTableTemplateFileName) {
        this.dropSchemaInfoTableTemplateFileName = dropSchemaInfoTableTemplateFileName;
    }

    /**
     * テーブルモデルのリストを返します。
     * 
     * @return テーブルモデルのリスト
     */
    public List<TableModel> getTableModelList() {
        return Collections.unmodifiableList(tableModelList);
    }

    /**
     * テーブルモデルを追加します。
     * 
     * @param tableModel
     *            テーブルモデル
     */
    public void addTableModel(TableModel tableModel) {
        tableModelList.add(tableModel);
    }

    /**
     * スキーマ情報テーブルのモデルを返します。
     * 
     * @return スキーマ情報テーブルのモデル
     */
    public SchemaInfoTableModel getSchemaInfoTableModel() {
        return schemaInfoTableModel;
    }

    /**
     * スキーマ情報テーブルのモデルを設定します。
     * 
     * @param schemaInfoTableModel
     *            スキーマ情報テーブルのモデル
     */
    public void setSchemaInfoTableModel(
            SchemaInfoTableModel schemaInfoTableModel) {
        this.schemaInfoTableModel = schemaInfoTableModel;
    }

}
