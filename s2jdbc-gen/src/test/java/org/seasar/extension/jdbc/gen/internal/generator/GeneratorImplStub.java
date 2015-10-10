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

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.seasar.extension.jdbc.gen.generator.GenerationContext;

import freemarker.template.TemplateDateModel;
import freemarker.template.TemplateModelException;

/**
 * @author taedium
 * 
 */
public class GeneratorImplStub extends GeneratorImpl {

    private Writer writer = new StringWriter();

    /**
     * 
     * @throws ParseException
     */
    public GeneratorImplStub() {
        this("UTF-8", null);
    }

    /**
     * 
     * @param templateFielEncoding
     * @param templateDir
     */
    public GeneratorImplStub(String templateFielEncoding, File templateDir) {
        super(templateFielEncoding, templateDir);
        configuration.setSharedVariable("currentDate", new TemplateDateModel() {

            public Date getAsDate() throws TemplateModelException {
                return getDate();
            }

            public int getDateType() {
                return UNKNOWN;
            }
        });
    }

    /**
     * 
     * @return
     */
    protected Date getDate() {
        try {
            return new SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                    .parse("2009/04/01 13:12:11");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean exists(File file) {
        return false;
    }

    @Override
    protected void mkdirs(File dir) {
    }

    @Override
    protected Writer openWriter(GenerationContext context) {
        return writer;
    }

    /**
     * 
     * @return
     */
    protected String getResult() {
        return writer.toString();
    }

    /**
     * 
     */
    protected void clear() {
        writer = new StringWriter();
    }
}