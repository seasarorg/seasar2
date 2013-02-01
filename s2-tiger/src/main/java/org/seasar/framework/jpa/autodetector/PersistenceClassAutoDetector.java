/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.jpa.autodetector;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.seasar.framework.autodetector.impl.AbstractClassAutoDetector;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourcesUtil;
import org.seasar.framework.util.ClassTraversal.ClassHandler;
import org.seasar.framework.util.ResourcesUtil.Resources;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * 規約を利用してJPAで管理すべき永続クラスを自動検出するクラスです。
 * <p>
 * このインスタンスが自動検出を実行するには{@link #namingConvention}に値が設定されていることが必須です。
 * デフォルトで次の条件に合致するクラスを検出します。
 * </p>
 * <ul>
 * <li>クラスが{@link NamingConvention#getEntityPackageName()}で決定されるパッケージの階層に含まれる</li>
 * <li>クラスに{@link Entity}、{@link MappedSuperclass}、{@link Embeddable}
 * のいずれかのアノテーションが指定されている</li>
 * </ul>
 * 
 * @author taedium
 */
@Component
public class PersistenceClassAutoDetector extends AbstractClassAutoDetector {

    /** アノテーションのリスト */
    protected final List<Class<? extends Annotation>> annotations = CollectionsUtil
            .newArrayList();

    /** 命名規約 */
    protected NamingConvention namingConvention;

    /** エンティティをロードするためのクラスローダ */
    protected ClassLoader classLoader;

    /**
     * インスタンスを構築します。
     * 
     */
    public PersistenceClassAutoDetector() {
        annotations.add(Entity.class);
        annotations.add(MappedSuperclass.class);
        annotations.add(Embeddable.class);
    }

    /**
     * 命名規約を設定します。
     * 
     * @param namingConvention
     *            命名規約
     */
    @Binding(bindingType = BindingType.MAY)
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    /**
     * クラスローダを設定します。
     * 
     * @param classLoader
     *            クラスローダ
     */
    @Binding(bindingType = BindingType.MAY)
    public void setClassLoader(final ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * このインスタンスを初期化します。
     * 
     */
    @InitMethod
    public void init() {
        if (namingConvention != null) {
            final String entityPackageName = namingConvention
                    .getEntityPackageName();
            for (final String rootPackageName : namingConvention
                    .getRootPackageNames()) {
                final String packageName = ClassUtil.concatName(
                        rootPackageName, entityPackageName);
                addTargetPackageName(packageName);
            }
        }
    }

    /**
     * 検出の条件として使用するアノテーションを追加します。
     * 
     * @param annotation
     *            アノテーション
     */
    public void addAnnotation(final Class<? extends Annotation> annotation) {
        annotations.add(annotation);
    }

    @SuppressWarnings("unchecked")
    public void detect(final ClassHandler handler) {
        for (int i = 0; i < getTargetPackageNameSize(); i++) {
            final String targetPackageName = getTargetPackageName(i);
            for (final Resources resources : ResourcesUtil
                    .getResourcesTypes(targetPackageName)) {
                try {
                    resources.forEach(new ClassHandler() {

                        public void processClass(final String packageName,
                                final String shortClassName) {
                            if ((packageName.equals(targetPackageName) || packageName
                                    .startsWith(targetPackageName + "."))
                                    && isEntity(packageName, shortClassName)) {
                                handler.processClass(packageName,
                                        shortClassName);
                            }
                        }
                    });
                } finally {
                    resources.close();
                }
            }
        }
    }

    /**
     * 指定されたクラスが永続クラスである場合{@code true}を返します。
     * 
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            クラス名
     * @return 指定されたクラスが永続クラスである場合{@code true}、永続クラスでない場合{@code false}
     */
    protected boolean isEntity(final String packageName,
            final String shortClassName) {
        final String name = ClassUtil.concatName(packageName, shortClassName);
        final Class<?> clazz = getClass(name);
        for (final Annotation ann : clazz.getAnnotations()) {
            if (annotations.contains(ann.annotationType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 名前から解決してクラスを返します。
     * 
     * @param className
     *            クラス名
     * @return クラス
     */
    protected Class<?> getClass(final String className) {
        if (classLoader != null) {
            return ReflectionUtil.forName(className, classLoader);
        }
        return ReflectionUtil.forNameNoException(className);
    }

}
