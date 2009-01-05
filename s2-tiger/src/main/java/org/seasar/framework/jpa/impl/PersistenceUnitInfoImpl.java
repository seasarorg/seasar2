/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.impl;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.seasar.framework.jpa.util.ChildFirstClassLoader;
import org.seasar.framework.jpa.util.TransformClassLoader;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * {@link PersistenceUnitInfo}の実装クラスです。
 * 
 * @author koichik
 */
public class PersistenceUnitInfoImpl implements PersistenceUnitInfo {

    /** クラスローダ */
    protected TransformClassLoader classLoader;

    /** 永続ユニットのルートURL */
    protected URL persistenceUnitRootUrl;

    /** 永続ユニット名 */
    protected String persistenceUnitName;

    /** トランザクションタイプ */
    protected PersistenceUnitTransactionType transactionType = PersistenceUnitTransactionType.JTA;

    /** 永続プロバイダのクラス名 */
    protected String persistenceProviderClassName;

    /** JTAデータソース */
    protected DataSource jtaDataSource;

    /** 非JTAデータソース */
    protected DataSource nonJtaDataSource;

    /** マッピングファイル名のリスト */
    protected List<String> mappingFileNames = CollectionsUtil.newArrayList();

    /** JarファイルのURLのリスト */
    protected List<URL> jarFileUrls = CollectionsUtil.newArrayList();

    /** 管理対象となるクラス名のリスト */
    protected List<String> managedClassNames = CollectionsUtil.newArrayList();

    /** 列挙されていないクラスを管理対象にしない場合に<code>true</code> */
    protected boolean excludeUnlistedClasses;

    /** プロパティリスト */
    protected Properties properties = new Properties();

    /** クラストランスファーマのリスト */
    protected List<ClassTransformer> transformers = CollectionsUtil
            .newArrayList();

    /**
     * インスタンスを構築します。
     */
    public PersistenceUnitInfoImpl() {
    }

    /**
     * インスタンスを構築します。
     * 
     * @param classLoader
     *            クラスローダ
     * @param persistenceUnitRootUrl
     *            永続ユニットのルートURL
     */
    public PersistenceUnitInfoImpl(final ClassLoader classLoader,
            final URL persistenceUnitRootUrl) {
        setClassLoader(classLoader);
        this.persistenceUnitRootUrl = persistenceUnitRootUrl;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * クラスローダを設定します。
     * 
     * @param classLoader
     *            クラスローダ
     */
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = new TransformClassLoader(classLoader);
    }

    public ClassLoader getNewTempClassLoader() {
        return new ChildFirstClassLoader(classLoader);
    }

    public URL getPersistenceUnitRootUrl() {
        return persistenceUnitRootUrl;
    }

    /**
     * 永続ユニットのルートURLを設定します。
     * 
     * @param persistenceUnitRootUrl
     *            永続ユニットのルートURL
     */
    public void setPersistenceUnitRootUrl(final URL persistenceUnitRootUrl) {
        this.persistenceUnitRootUrl = persistenceUnitRootUrl;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    /**
     * 永続ユニット名を設定します。
     * 
     * @param persistenceUnitName
     *            永続ユニット名
     */
    public void setPersistenceUnitName(final String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public PersistenceUnitTransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * トランザクションタイプを設定します。
     * 
     * @param transactionType
     *            トランザクションタイプ
     */
    public void setTransactionType(
            final PersistenceUnitTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getPersistenceProviderClassName() {
        return persistenceProviderClassName;
    }

    /**
     * 永続プロバイダのクラス名を設定します。
     * 
     * @param persistenceProviderClassName
     *            永続プロバイダのクラス名
     */
    public void setPersistenceProviderClassName(
            final String persistenceProviderClassName) {
        this.persistenceProviderClassName = persistenceProviderClassName;
    }

    public DataSource getJtaDataSource() {
        return jtaDataSource;
    }

    /**
     * JTAデータソースを設定します。
     * 
     * @param jtaDataSource
     *            JTAデータソース
     */
    public void setJtaDataSource(final DataSource jtaDataSource) {
        this.jtaDataSource = jtaDataSource;
    }

    public DataSource getNonJtaDataSource() {
        return nonJtaDataSource;
    }

    /**
     * 非JTAデータソースを設定します。
     * 
     * @param nonJtaDataSource
     *            非JTAデータソース
     */
    public void setNonJtaDataSource(final DataSource nonJtaDataSource) {
        this.nonJtaDataSource = nonJtaDataSource;
    }

    public List<String> getMappingFileNames() {
        return mappingFileNames;
    }

    /**
     * マッピングファイル名を追加します。
     * 
     * @param mappingFileName
     *            マッピングファイル名
     */
    public void addMappingFileNames(final String mappingFileName) {
        mappingFileNames.add(mappingFileName);
    }

    public List<URL> getJarFileUrls() {
        return jarFileUrls;
    }

    /**
     * JarファイルのURLを追加します。
     * 
     * @param jarFileUrl
     *            JarファイルのURL
     */
    public void addJarFileUrls(final URL jarFileUrl) {
        jarFileUrls.add(jarFileUrl);
    }

    public List<String> getManagedClassNames() {
        return managedClassNames;
    }

    /**
     * 管理対象のクラスを追加します。
     * 
     * @param managedClassName
     *            管理対象のクラス
     */
    public void addManagedClassNames(final String managedClassName) {
        managedClassNames.add(managedClassName);
    }

    public boolean excludeUnlistedClasses() {
        return excludeUnlistedClasses;
    }

    /**
     * 列挙されていないクラスを管理対象にしない場合は<code>true</code>を設定します。
     * 
     * @param excludeUnlistedClasses
     *            列挙されていないクラスを管理対象にしない場合は<code>true</code>
     */
    public void setExcludeUnlistedClasses(final boolean excludeUnlistedClasses) {
        this.excludeUnlistedClasses = excludeUnlistedClasses;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * プロパティリストにキーと値のマッピングを追加します。
     * 
     * @param key
     *            キー
     * @param value
     *            値
     */
    public void addProperties(final String key, final String value) {
        properties.put(key, value);
    }

    /**
     * クラストランスファーマのリストを返します。
     * 
     * @return クラストランスファーマのリスト
     */
    public List<ClassTransformer> getTransformers() {
        return transformers;
    }

    public void addTransformer(final ClassTransformer transformer) {
        transformers.add(transformer);
        classLoader.addTransformer(transformer);
    }

    /**
     * トランスファーマークラスローダーを返します。
     * 
     * @return トランスファーマークラスローダー
     */
    public TransformClassLoader getTransformClassLoader() {
        return classLoader;
    }

}
