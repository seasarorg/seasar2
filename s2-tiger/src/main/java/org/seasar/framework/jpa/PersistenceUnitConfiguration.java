/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa;

import org.seasar.framework.autodetector.ClassAutoDetector;
import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * 永続ユニットのコンフィギュレーションです。
 * 
 * @author taedium
 * 
 */
public interface PersistenceUnitConfiguration {

    /**
     * マッピングファイルを追加します。
     * 
     * @param fileName
     *            マッピングファイル名
     */
    void addMappingFile(String fileName);

    /**
     * 指定した永続ユニットにマッピングファイルを追加します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param fileName
     *            マッピングファイル名
     */
    void addMappingFile(String unitName, String fileName);

    /**
     * 永続クラスを追加します。
     * 
     * @param clazz
     *            永続クラス
     */
    void addPersistenceClass(Class<?> clazz);

    /**
     * 指定した永続ユニットに永続クラスを追加します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param clazz
     *            永続クラス
     */
    void addPersistenceClass(String unitName, Class<?> clazz);

    /**
     * リソースの自動検出器を追加します。
     * 
     * @param detector
     *            自動検出器
     */
    void addMappingFileAutoDetector(ResourceAutoDetector detector);

    /**
     * 指定した永続ユニットにリソースを自動登録する自動検出器を追加します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param detector
     *            自動検出器
     */
    public void addMappingFileAutoDetector(String unitName,
            final ResourceAutoDetector detector);

    /**
     * 永続クラスの自動検出器を追加します。
     * 
     * @param detector
     */
    public void addPersistenceClassAutoDetector(ClassAutoDetector detector);

    /**
     * 指定した永続ユニットに永続クラスを自動登録する自動検出器を追加します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param detector
     *            自動検出器
     */
    public void addPersistenceClassAutoDetector(String unitName,
            final ClassAutoDetector detector);

    /**
     * 指定された永続ユニットに登録されるべきマッピングファイルを検出し、処理をハンドラーに委譲します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param handler
     *            リソースのハンドラー
     */
    void detectMappingFiles(String unitName, ResourceHandler handler);

    /**
     * 指定された永続ユニットに登録されるべき永続クラスを検出し、処理をハンドラーに委譲します。
     * 
     * @param unitName
     *            永続ユニット名
     * @param handler
     *            クラスのハンドラー
     */
    void detectPersistenceClasses(String unitName, ClassHandler handler);

    /**
     * 自動検出が有効ならば<code>true</code>を返します。
     * 
     * @return 自動検出が有効ならば<code>true</code>、無効ならば<code>false</code>
     */
    boolean isAutoDetection();
}
