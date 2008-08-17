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
package org.seasar.extension.jdbc.gen.internal.util;

import java.io.IOException;
import java.io.Writer;

import org.seasar.extension.jdbc.gen.internal.exception.TemplateRuntimeException;
import org.seasar.framework.exception.IORuntimeException;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * {@link Template}のユーティリティクラスです。
 * 
 * @author taedium
 */
public class TemplateUtil {

    /**
     * 
     */
    protected TemplateUtil() {
    }

    /**
     * テンプレートを処理します。
     * 
     * @param template
     *            テンプレート
     * @param dataModel
     *            データモデル
     * @param writer
     * @link {@link Writer}
     */
    public static void process(Template template, Object dataModel,
            Writer writer) {
        try {
            template.process(dataModel, writer);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } catch (TemplateException e) {
            throw new TemplateRuntimeException(e);
        }
    }
}
