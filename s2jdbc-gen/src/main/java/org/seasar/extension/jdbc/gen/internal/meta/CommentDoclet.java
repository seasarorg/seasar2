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
package org.seasar.extension.jdbc.gen.internal.meta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.internal.util.EntityMetaUtil;
import org.seasar.extension.jdbc.gen.internal.util.PropertyMetaUtil;

import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;

/**
 * エンティティとプロパティのコメントを抽出する{@link Doclet}です。
 * 
 * @author taedium
 */
public class CommentDoclet extends Doclet {

    public static boolean start(RootDoc rootDoc) {
        List<EntityMeta> entityMetaList = CommentDocletContext
                .getEntityMetaList();
        if (entityMetaList == null) {
            throw new NullPointerException("entityMetaList");
        }
        for (EntityMeta entityMeta : entityMetaList) {
            ClassDoc classDoc = rootDoc.classNamed(entityMeta.getEntityClass()
                    .getName());
            if (classDoc == null) {
                continue;
            }
            doEntityComment(classDoc, entityMeta);
        }
        return true;
    }

    /**
     * エンティティクラスのコメントを処理します。
     * 
     * @param classDoc
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected static void doEntityComment(ClassDoc classDoc,
            EntityMeta entityMeta) {
        EntityMetaUtil.setComment(entityMeta, classDoc.commentText());

        Map<String, FieldDoc> fieldDocMap = getFieldDocMap(classDoc);
        for (PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            if (fieldDocMap.containsKey(propertyMeta.getName())) {
                FieldDoc fieldDoc = fieldDocMap.get(propertyMeta.getName());
                doPropertyComment(fieldDoc, propertyMeta);
            }
        }
    }

    /**
     * プロパティのコメントを処理します。
     * 
     * @param fieldDoc
     * @param propertyMeta
     *            プロパティメタデータ
     */
    protected static void doPropertyComment(FieldDoc fieldDoc,
            PropertyMeta propertyMeta) {
        PropertyMetaUtil.setComment(propertyMeta, fieldDoc.commentText());
    }

    /**
     * フィールド名をキー、 {@link FieldDoc}を値とするマップを返します。
     * 
     * @param classDoc
     * @return フィールド名をキー、 {@link FieldDoc}を値とするマップ
     */
    protected static Map<String, FieldDoc> getFieldDocMap(ClassDoc classDoc) {
        Map<String, FieldDoc> fieldMap = new HashMap<String, FieldDoc>();
        for (FieldDoc fieldDoc : classDoc.fields()) {
            fieldMap.put(fieldDoc.name(), fieldDoc);
        }
        for (ClassDoc superclassDoc = classDoc.superclass(); !Object.class
                .getName().equals(superclassDoc.qualifiedName()); superclassDoc = superclassDoc
                .superclass()) {
            if (isMappedSuperclass(superclassDoc)) {
                for (FieldDoc fieldDoc : superclassDoc.fields()) {
                    if (!fieldMap.containsKey(fieldDoc.name())) {
                        fieldMap.put(fieldDoc.name(), fieldDoc);
                    }
                }
            }
        }
        return fieldMap;
    }

    /**
     * {@link MappedSuperclass}を表す場合{@code true}を返します。
     * 
     * @param classDoc
     * @return {@link MappedSuperclass}を表す場合{@code true}
     */
    protected static boolean isMappedSuperclass(ClassDoc classDoc) {
        for (AnnotationDesc desc : classDoc.annotations()) {
            if (MappedSuperclass.class.getName().equals(
                    desc.annotationType().qualifiedName())) {
                return true;
            }
        }
        return false;
    }

}
