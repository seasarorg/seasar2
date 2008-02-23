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
package org.seasar.extension.jdbc.gen.javacode;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;

import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.framework.util.ClassUtil;

/**
 * エンティティクラスを表す{@link JavaCode}の実装です。
 * 
 * @author taedium
 */
public class EntityJavaCode extends AbstractJavaCode {

    /** エンティティモデル */
    protected EntityModel entityModel;

    /** インポートパッケージ名の名前 */
    protected Set<String> importPackageNames;

    /** 基底クラス名 */
    protected String baseClassName;

    /** パッケージ名を除いた基底クラス名 */
    protected String shortBaseClassName;

    /**
     * インスタンスを構築します。
     * 
     * @param entityModel
     *            エンティティモデル
     * @param className
     *            クラス名
     * @param baseClassName
     *            基底クラス名
     * @param templateName
     *            テンプレート名
     */
    public EntityJavaCode(EntityModel entityModel, String className,
            String baseClassName, String templateName) {
        super(className, templateName);
        this.entityModel = entityModel;
        this.baseClassName = baseClassName;
        this.shortBaseClassName = ClassUtil
                .splitPackageAndShortClassName(baseClassName)[1];
        this.importPackageNames = createImportPackageNames();
    }

    public Set<String> getImportPackageNames() {
        return importPackageNames;
    }

    public String getBaseClassName() {
        return baseClassName;
    }

    public String getShortBaseClassName() {
        return shortBaseClassName;
    }

    /**
     * エンティティモデルを返します。
     * 
     * @return エンティティモデル
     */
    public EntityModel getEntityModel() {
        return entityModel;
    }

    /**
     * インポートパッケージ名のセットを作成します。
     * 
     * @return インポートパッケージ名のセット
     */
    protected Set<String> createImportPackageNames() {
        Set<String> packageNames = new TreeSet<String>();
        packageNames.add(Entity.class.getName());
        packageNames.add(baseClassName);
        return packageNames;
    }

}
