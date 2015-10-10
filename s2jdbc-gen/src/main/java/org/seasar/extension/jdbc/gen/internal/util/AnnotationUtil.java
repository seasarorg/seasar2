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
package org.seasar.extension.jdbc.gen.internal.util;

import javax.persistence.Column;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.seasar.extension.jdbc.annotation.ReferentialConstraint;
import org.seasar.framework.util.tiger.ReflectionUtil;

/**
 * アノテーションに関するユーティリティクラスです。
 * 
 * @author taedium
 */
public class AnnotationUtil {

    /** デフォルトの{@link Table}を取得するためのクラス */
    @Table
    protected static class TableAnnotated {
    }

    /** デフォルトの{@link Table} */
    protected static Table DEFAULT_TABLE = TableAnnotated.class
            .getAnnotation(Table.class);

    /** デフォルトの{@link Column} */
    @Column(length = 255, precision = 19, scale = 2)
    protected static Column DEFAULT_COLUMN = ReflectionUtil.getDeclaredField(
            AnnotationUtil.class, "DEFAULT_COLUMN").getAnnotation(Column.class);

    /** デフォルトの{@link SequenceGenerator} */
    @SequenceGenerator(name = "default")
    protected static final SequenceGenerator DEFAULT_SEQUENCE_GENERATOR = ReflectionUtil
            .getDeclaredField(AnnotationUtil.class,
                    "DEFAULT_SEQUENCE_GENERATOR").getAnnotation(
                    SequenceGenerator.class);

    /** デフォルトの{@link TableGenerator} */
    @TableGenerator(name = "default")
    protected static final TableGenerator DEFAULT_TABLE_GENERATOR = ReflectionUtil
            .getDeclaredField(AnnotationUtil.class, "DEFAULT_TABLE_GENERATOR")
            .getAnnotation(TableGenerator.class);

    /** デフォルトの{@link ReferentialConstraint} */
    @ReferentialConstraint
    protected static final ReferentialConstraint DEFAULT_REFERENTIAL_CONSTRAINT = ReflectionUtil
            .getDeclaredField(AnnotationUtil.class,
                    "DEFAULT_REFERENTIAL_CONSTRAINT").getAnnotation(
                    ReferentialConstraint.class);

    /**
     * 
     */
    protected AnnotationUtil() {
    }

    /**
     * デフォルトの{@link Table}を返します。
     * 
     * @return デフォルトの{@link Table}
     */
    public static Table getDefaultTable() {
        return DEFAULT_TABLE;
    }

    /**
     * デフォルトの{@link Column}を返します。
     * 
     * @return デフォルトの{@link Column}
     */
    public static Column getDefaultColumn() {
        return DEFAULT_COLUMN;
    }

    /**
     * デフォルトの{@link SequenceGenerator}を返します。
     * 
     * @return デフォルトの{@link SequenceGenerator}
     */
    public static SequenceGenerator getDefaultSequenceGenerator() {
        return DEFAULT_SEQUENCE_GENERATOR;
    }

    /**
     * デフォルトの{@link TableGenerator}を返します。
     * 
     * @return デフォルトの{@link TableGenerator}
     */
    public static TableGenerator getDefaultTableGenerator() {
        return DEFAULT_TABLE_GENERATOR;
    }

    /**
     * デフォルトの{@link ReferentialConstraint}を返します。
     * 
     * @return デフォルトの{@link ReferentialConstraint}
     */
    public static ReferentialConstraint getDefaultReferentialConstraint() {
        return DEFAULT_REFERENTIAL_CONSTRAINT;
    }
}
