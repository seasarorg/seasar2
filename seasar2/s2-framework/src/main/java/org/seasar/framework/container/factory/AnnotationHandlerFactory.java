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
package org.seasar.framework.container.factory;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.util.ClassUtil;

/**
 * アノテーションハンドラを構築するためのファクトリクラスです。
 * <p>
 * アノテーションハンドラファクトリは、 まずTigerアノテーションのハンドラクラスを構築しようと試みます。
 * Tigerアノテーションのハンドラクラスがクラスパス上に存在せずTigerアノテーションが利用できない場合は、
 * 定数アノテーションのハンドラクラスを構築します。
 * </p>
 * 
 * @author jundu
 * 
 */
public class AnnotationHandlerFactory {

    private static final String TIGER_ANNOTATION_HANDLER_CLASS_NAME = "org.seasar.framework.container.factory.TigerAnnotationHandler";

    private static AnnotationHandler annotationHandler;

    static {
        initialize();
    }

    /**
     * アノテーションハンドラファクトリを初期化します。
     */
    protected static void initialize() {
        if (annotationHandler != null) {
            return;
        }
        Class clazz = ConstantAnnotationHandler.class;
        try {
            clazz = ClassUtil.forName(TIGER_ANNOTATION_HANDLER_CLASS_NAME);
        } catch (ClassNotFoundRuntimeException ignore) {
        }
        annotationHandler = (AnnotationHandler) ClassUtil.newInstance(clazz);
    }

    /**
     * アノテーションハンドラを返します。
     * 
     * @return アノテーションハンドラ
     */
    public static AnnotationHandler getAnnotationHandler() {
        initialize();
        return annotationHandler;
    }

    /**
     * アノテーションハンドラを設定します。
     * 
     * @param handler
     *            アノテーションハンドラ
     */
    public static void setAnnotationHandler(AnnotationHandler handler) {
        annotationHandler = handler;
    }
}
