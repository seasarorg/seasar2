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
package org.seasar.extension.jdbc.gen.internal.model;

import org.seasar.extension.jdbc.gen.model.ClassModel;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link ClassModel}のサポートクラスです。
 * 
 * @author taedium
 */
public class ClassModelSupport {

    /**
     * クラスモデルにインポート名を追加します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importClass
     *            インポート対象のクラス
     */
    public void addImportName(ClassModel classModel, Class<?> importClass) {
        String canonicalName = importClass.getCanonicalName();
        if (canonicalName == null) {
            return;
        }
        String importedPackageName = ClassUtil
                .splitPackageAndShortClassName(canonicalName)[0];
        if (isImportTargetPackage(classModel, importedPackageName)) {
            classModel.addImportName(canonicalName);
        }
    }

    /**
     * クラスモデルにインポート名を追加します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importClassName
     *            インポート対象のクラス名
     */
    public void addImportName(ClassModel classModel, String importClassName) {
        String importedPackageName = ClassUtil
                .splitPackageAndShortClassName(importClassName)[0];
        if (isImportTargetPackage(classModel, importedPackageName)) {
            classModel.addImportName(importClassName);
        }
    }

    /**
     * インポートが必要なパッケージの場合{@code true}を返します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importPackageName
     *            インポートするパッケージ名
     * @return インポートが必要なパッケージの場合{@code true}
     */
    protected boolean isImportTargetPackage(ClassModel classModel,
            String importPackageName) {
        if (importPackageName == null) {
            return false;
        }
        if (importPackageName.equals(classModel.getPackageName())) {
            return false;
        }
        if (importPackageName.equals("java.lang")) {
            return false;
        }
        return true;
    }

    /**
     * クラスモデルにstaticインポート名を追加します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importClass
     *            インポート対象のクラス
     */
    public void addStaticImportName(ClassModel classModel, Class<?> importClass) {
        String canonicalName = importClass.getCanonicalName();
        if (canonicalName == null) {
            return;
        }
        String importedPackageName = ClassUtil
                .splitPackageAndShortClassName(canonicalName)[0];
        if (isStaticImportTargetPackage(classModel, importedPackageName)) {
            classModel.addStaticImportName(canonicalName + ".*");
        }
    }

    /**
     * クラスモデルにstaticインポート名を追加します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importClassName
     *            インポート対象のクラス名
     */
    public void addStaticImportName(ClassModel classModel,
            String importClassName) {
        String importedPackageName = ClassUtil
                .splitPackageAndShortClassName(importClassName)[0];
        if (isStaticImportTargetPackage(classModel, importedPackageName)) {
            classModel.addStaticImportName(importClassName + ".*");
        }
    }

    /**
     * staticインポートが必要なパッケージの場合{@code true}を返します。
     * 
     * @param classModel
     *            クラスモデル
     * @param importPackageName
     *            インポートするパッケージ名
     * @return インポートが必要なパッケージの場合{@code true}
     */
    protected boolean isStaticImportTargetPackage(ClassModel classModel,
            String importPackageName) {
        if (importPackageName == null) {
            return false;
        }
        return true;
    }
}
