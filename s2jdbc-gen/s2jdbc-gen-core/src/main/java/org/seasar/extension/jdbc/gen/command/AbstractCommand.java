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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.exception.RequiredPropertyNullRuntimeException;
import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.factory.BeanDescFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.log.Logger;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.ResourceUtil;

/**
 * エンティティのを生成するコマンドの抽象クラスです。
 * 
 * @author taedium
 */
public abstract class AbstractCommand implements Command {

    /** ロガー */
    protected static Logger logger = Logger.getLogger(AbstractCommand.class);

    /** diconファイル */
    @BindableProperty
    protected String diconFile = "s2jdbc.dicon";

    /** {@link JdbcManager}のコンポーネント名 */
    @BindableProperty
    protected String jdbcManagerName = "jdbcManager";

    /** ルートパッケージ名 */
    @BindableProperty
    protected String rootPackageName = "";

    /** エンティティクラスのパッケージ名 */
    @BindableProperty
    protected String entityPackageName = "entity";

    /** テンプレートファイルを格納するディレクトリ */
    @BindableProperty
    protected File templateDir = ResourceUtil.getResourceAsFile("templates");

    /** テンプレートファイルのエンコーディング */
    @BindableProperty
    protected String templateFileEncoding = "UTF-8";

    /** このインスタンスで{@link SingletonS2ContainerFactory}が初期化されたら{@code true} */
    protected boolean initialized;

    /**
     * インスタンスを構築します。
     */
    public AbstractCommand() {
    }

    public void setDiconFile(String diconFile) {
        this.diconFile = diconFile;
    }

    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    public void setTemplateFileEncoding(String templateFileEncoding) {
        this.templateFileEncoding = templateFileEncoding;
    }

    public void execute() {
        validate();
        init();
        try {
            doExecute();
        } finally {
            destroy();
        }
    }

    protected void validate() {
        Class<?> clazz = getClass();
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(clazz);
        for (int i = 0; i < beanDesc.getFieldSize(); i++) {
            Field field = beanDesc.getField(i);
            BindableProperty property = field
                    .getAnnotation(BindableProperty.class);
            if (property == null) {
                continue;
            }
            field.setAccessible(true);
            if (property.required() && FieldUtil.get(field, this) == null) {
                throw new RequiredPropertyNullRuntimeException(field.getName());
            }
            logger.log("DS2JDBCGen0001", new Object[] { field.getName(),
                    FieldUtil.get(field, this) });
        }
    }

    /**
     * 初期化します。
     */
    protected void init() {
        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(diconFile);
            SingletonS2ContainerFactory.init();
        }
    }

    protected abstract void doExecute();

    /**
     * 破棄します。
     */
    protected void destroy() {
        if (initialized) {
            SingletonS2ContainerFactory.destroy();
        }
    }

    /**
     * 外部のクラスからセッタメソッドを介して値を設定可能であることを示します。
     * 
     * @author taedium
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface BindableProperty {

        /** {@code null}以外の値であることを示します。 */
        boolean required() default false;
    }

}
