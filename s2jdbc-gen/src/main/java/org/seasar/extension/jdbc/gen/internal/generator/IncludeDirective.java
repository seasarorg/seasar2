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
package org.seasar.extension.jdbc.gen.internal.generator;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * インクルードのディレクティブです。
 * <p>
 * インクルード先のテンプレートで任意のオブジェクトをルートのデータモデルに指定できます。
 * </p>
 * 
 * @author taedium
 */
public class IncludeDirective implements TemplateDirectiveModel {

    /** インクルードするテンプレート名のパラメータ名 */
    protected static final String PARAM_NAME = "name";

    /** ルートモデルのパラメータ名 */
    protected static final String PARAM_ROOT_MODEL = "rootModel";

    public void execute(Environment env,
            @SuppressWarnings("unchecked") Map params,
            TemplateModel[] loopVars, TemplateDirectiveBody body)
            throws TemplateException, IOException {

        Object name = params.get(PARAM_NAME);
        if (name == null) {
            throw new IllegalArgumentException("params[" + PARAM_NAME + "]");
        }
        if (!SimpleScalar.class.isInstance(name)) {
            throw new IllegalArgumentException("params[" + PARAM_NAME + "]");
        }

        Object rootModel = params.get(PARAM_ROOT_MODEL);
        if (rootModel == null) {
            throw new IllegalArgumentException("params[" + PARAM_ROOT_MODEL
                    + "]");
        }

        Template template = env.getTemplateForInclusion(((SimpleScalar) name)
                .getAsString(), null, true);
        template.process(rootModel, env.getOut());
        if (body != null) {
            body.render(env.getOut());
        }
    }
}
