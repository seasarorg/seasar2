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
package org.seasar.extension.jdbc.gen.internal.desc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.GenerationType;

import org.seasar.extension.jdbc.gen.desc.AttributeDescFactory;
import org.seasar.extension.jdbc.gen.desc.CompositeUniqueConstraintDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntityDesc;
import org.seasar.extension.jdbc.gen.desc.EntityDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntitySetDesc;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.meta.DbForeignKeyMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMeta;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.exception.IORuntimeException;

/**
 * {@link EntitySetDescFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntitySetDescFactoryImpl implements EntitySetDescFactory {

    /** テーブルメタデータのリーダ */
    protected DbTableMetaReader dbTableMetaReader;

    /** 永続化層の命名規約 */
    protected PersistenceConvention persistenceConvention;

    /** 方言 */
    protected GenDialect dialect;

    /** バージョンカラム名のパターン */
    protected String versionColumnNamePattern;

    /** 単語を複数系に変換するための辞書ファイル、使用しない場合は{@code null} */
    protected File pluralFormFile;

    /** エンティティの識別子の生成方法を示す列挙型 、生成しない場合は{@code null} */
    protected GenerationType generationType;

    /** エンティティの識別子の初期値、指定しない場合は{@code null} */
    protected Integer initialValue;

    /** エンティティの識別子の割り当てサイズ、指定しない場合は{@code null} */
    protected Integer allocationSize;

    /** エンティティ記述のファクトリ */
    protected EntityDescFactory entityDescFactory;

    /**
     * インスタンスを構築します。
     * 
     * @param dbTableMetaReader
     *            テーブルメタデータのリーダ
     * @param dialect
     *            方言
     * @param persistenceConvention
     *            永続化層の命名規約
     * @param versionColumnNamePattern
     *            バージョンカラム名のパターン
     * @param pluralFormFile
     *            単語を複数系に変換するための辞書ファイル、使用しない場合は{@code null}
     * @param generationType
     *            エンティティの識別子の生成方法を示す列挙型 、生成しない場合は{@code null}
     * @param initialValue
     *            エンティティの識別子の初期値、指定しない場合は{@code null}
     * @param allocationSize
     *            エンティティの識別子の割り当てサイズ、指定しない場合は{@code null}
     */
    public EntitySetDescFactoryImpl(DbTableMetaReader dbTableMetaReader,
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnNamePattern, File pluralFormFile,
            GenerationType generationType, Integer initialValue,
            Integer allocationSize) {
        if (dbTableMetaReader == null) {
            throw new NullPointerException("dbTableMetaReader");
        }
        if (dialect == null) {
            throw new NullPointerException("dialect");
        }
        if (persistenceConvention == null) {
            throw new NullPointerException("persistenceConvention");
        }
        if (versionColumnNamePattern == null) {
            throw new NullPointerException("versionColumnNamePattern");
        }
        this.dbTableMetaReader = dbTableMetaReader;
        this.dialect = dialect;
        this.persistenceConvention = persistenceConvention;
        this.versionColumnNamePattern = versionColumnNamePattern;
        this.pluralFormFile = pluralFormFile;
        this.generationType = generationType;
        this.initialValue = initialValue;
        this.allocationSize = allocationSize;
        entityDescFactory = createEntityDescFactory();
    }

    public EntitySetDesc getEntitySetDesc() {
        EntitySetDesc entitySetDesc = new EntitySetDesc();
        List<DbTableMeta> dbTableMetaList = dbTableMetaReader.read();
        for (DbTableMeta tableMeta : dbTableMetaList) {
            EntityDesc entityDesc = entityDescFactory.getEntityDesc(tableMeta);
            entitySetDesc.addEntityDesc(entityDesc);
        }

        PluralFormDictinary pluralFormDictinary = createPluralFormDictinary();
        AssociationResolver associationResolver = createAssociationResolver(
                entitySetDesc, pluralFormDictinary);
        for (DbTableMeta tableMeta : dbTableMetaList) {
            for (DbForeignKeyMeta fkMeta : tableMeta.getForeignKeyMetaList()) {
                associationResolver.resolve(tableMeta, fkMeta);
            }
        }
        return entitySetDesc;
    }

    /**
     * {@link EntityDescFactory}の実装を作成します。
     * 
     * @return {@link EntityDescFactory}の実装
     */
    protected EntityDescFactory createEntityDescFactory() {
        AttributeDescFactory attributeDescFactory = new AttributeDescFactoryImpl(
                persistenceConvention, dialect, versionColumnNamePattern,
                generationType, initialValue, allocationSize);
        CompositeUniqueConstraintDescFactory compositeUniqueConstraintDescFactory = new CompositeUniqueConstraintDescFactoryImpl();
        return new EntityDescFactoryImpl(persistenceConvention,
                attributeDescFactory, compositeUniqueConstraintDescFactory);
    }

    /**
     * 関連のリゾルバを作成します。
     * 
     * @param entitySetDesc
     *            エンティティ集合記述
     * @param pluralFormDictinary
     *            単語を複数形に変換するための辞書
     * @return 関連のリゾルバ
     */
    protected AssociationResolver createAssociationResolver(
            EntitySetDesc entitySetDesc, PluralFormDictinary pluralFormDictinary) {
        return new AssociationResolver(entitySetDesc, pluralFormDictinary,
                persistenceConvention);
    }

    /**
     * 単語を複数形に変換するための辞書を作成します。
     * 
     * @return 単語を複数形に変換するための辞書
     */
    protected PluralFormDictinary createPluralFormDictinary() {
        if (pluralFormFile != null) {
            LinkedHashMap<String, String> map = loadPluralFormFile();
            return new PluralFormDictinary(map);
        }
        return new PluralFormDictinary();
    }

    /**
     * 辞書ファイルをロードし結果を{@link LinkedHashMap}として返します。
     * 
     * @return 正規表現をキー、置換文字列を値とするマップ
     */
    public LinkedHashMap<String, String> loadPluralFormFile() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(pluralFormFile), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                char firstChar = line.charAt(0);
                if (firstChar == '#' || firstChar == '!') {
                    continue;
                }
                int pos = line.indexOf('=');
                if (pos < 0) {
                    continue;
                }
                String key = line.substring(0, pos);
                String value = line.substring(pos + 1, line.length());
                map.put(key, value);
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return map;
    }

}
