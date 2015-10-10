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
package org.seasar.extension.jdbc.gen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * テーブルのモデルです。
 * 
 * @author taedium
 */
public class TableModel extends DdlModel {

    /** テーブルオプション */
    protected String tableOption;

    /** コメント */
    protected String comment;

    /** 主キーモデル */
    protected PrimaryKeyModel primaryKeyModel;

    /** 外部キーモデルのリスト */
    protected List<ForeignKeyModel> foreignKeyModelList = new ArrayList<ForeignKeyModel>();

    /** 一意キーモデルのリスト */
    protected List<UniqueKeyModel> uniqueKeyModelList = new ArrayList<UniqueKeyModel>();

    /** カラムモデルのリスト */
    protected List<ColumnModel> columnModelList = new ArrayList<ColumnModel>();

    /** シーケンスモデルのリスト */
    protected List<SequenceModel> sequenceModelList = new ArrayList<SequenceModel>();

    /**
     * テーブルオプションを返します。
     * 
     * @return テーブルオプション
     */
    public String getTableOption() {
        return tableOption;
    }

    /**
     * テーブルオプションを設定します。
     * 
     * @param tableOption
     *            テーブルオプション
     */
    public void setTableOption(String tableOption) {
        this.tableOption = tableOption;
    }

    /**
     * コメントを返します。
     * 
     * @return コメント
     */
    public String getComment() {
        return comment;
    }

    /**
     * コメントを設定します。
     * 
     * @param comment
     *            コメント
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 主キーモデルを返します。
     * 
     * @return 主キーモデル
     */
    public PrimaryKeyModel getPrimaryKeyModel() {
        return primaryKeyModel;
    }

    /**
     * 主キーモデルを設定します。
     * 
     * @param primaryKeyModel
     *            主キーモデル
     */
    public void setPrimaryKeyModel(PrimaryKeyModel primaryKeyModel) {
        this.primaryKeyModel = primaryKeyModel;
    }

    /**
     * 外部キーモデルのリストを返します。
     * 
     * @return 外部キーモデルのリスト
     */
    public List<ForeignKeyModel> getForeignKeyModelList() {
        return Collections.unmodifiableList(foreignKeyModelList);
    }

    /**
     * 外部キーモデルを追加します。
     * 
     * @param foreignKeyModel
     *            外部キーモデルのリスト
     */
    public void addForeignKeyModel(ForeignKeyModel foreignKeyModel) {
        foreignKeyModelList.add(foreignKeyModel);
    }

    /**
     * 一意キーモデルのリストを返します。
     * 
     * @return 一意キーモデルのリスト
     */
    public List<UniqueKeyModel> getUniqueKeyModelList() {
        return Collections.unmodifiableList(uniqueKeyModelList);
    }

    /**
     * 一意キーモデルを追加します。
     * 
     * @param uniqueKeyModel
     *            一意キーモデル
     */
    public void addUniqueKeyModel(UniqueKeyModel uniqueKeyModel) {
        uniqueKeyModelList.add(uniqueKeyModel);
    }

    /**
     * カラムモデルのリストを返します。
     * 
     * @return カラムモデルのリスト
     */
    public List<ColumnModel> getColumnModelList() {
        return Collections.unmodifiableList(columnModelList);
    }

    /**
     * カラムモデルを追加します。
     * 
     * @param columnModel
     *            カラムモデルのリスト
     */
    public void addColumnModel(ColumnModel columnModel) {
        columnModelList.add(columnModel);
    }

    /**
     * シーケンスモデルのリストを返します。
     * 
     * @return シーケンスモデルのリスト
     */
    public List<SequenceModel> getSequenceModelList() {
        return Collections.unmodifiableList(sequenceModelList);
    }

    /**
     * シーケンスモデルを追加します。
     * 
     * @param sequenceModel
     *            シーケンスモデルのリスト
     */
    public void addSequenceModel(SequenceModel sequenceModel) {
        sequenceModelList.add(sequenceModel);
    }

}
