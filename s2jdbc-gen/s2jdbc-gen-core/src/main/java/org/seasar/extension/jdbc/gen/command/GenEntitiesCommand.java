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
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.EntityModelConverter;
import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.extension.jdbc.gen.JavaCodeGenerator;
import org.seasar.extension.jdbc.gen.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.SchemaReader;
import org.seasar.extension.jdbc.gen.converter.EntityModelConverterImpl;
import org.seasar.extension.jdbc.gen.converter.PropertyModelConverterImpl;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.TooManyRootPackageNameRuntimeException;
import org.seasar.extension.jdbc.gen.generator.JavaCodeGeneratorImpl;
import org.seasar.extension.jdbc.gen.javacode.EntityBaseCode;
import org.seasar.extension.jdbc.gen.javacode.EntityCode;
import org.seasar.extension.jdbc.gen.model.DbTableDesc;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.reader.SchemaReaderImpl;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import static org.seasar.extension.jdbc.gen.command.GenEntitiesConstants.*;

/**
 * @author taedium
 * 
 */
public class GenEntitiesCommand {

    protected String diconName;

    protected String jdbcManagerName;

    protected String rootPackageName;

    protected String entityPackageName;

    protected String entityBasePackageName;

    protected String baseClassNameSuffix;

    protected File destDir;

    protected String javaCodeEncoding;

    protected File templateDir;

    protected String templateEncoding;

    protected String schemaName;

    protected String versionColumnName;

    protected String entityTemplateName;

    protected String entityBaseTemplateName;

    protected boolean initialized;

    protected DataSource dataSource;

    protected GenDialect dialect;

    protected PersistenceConvention persistenceConvention;

    public GenEntitiesCommand() {
    }

    public void setDiconName(String diconName) {
        this.diconName = diconName;
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

    public void setEntityBasePackageName(String entityBasePackageName) {
        this.entityBasePackageName = entityBasePackageName;
    }

    public void setBaseClassNameSuffix(String baseClassNameSuffix) {
        this.baseClassNameSuffix = baseClassNameSuffix;
    }

    public void setTemplateDir(File templateDir) {
        this.templateDir = templateDir;
    }

    public void setTemplateEncoding(String templateEncoding) {
        this.templateEncoding = templateEncoding;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setJavaCodeEncoding(String javaCodeEncoding) {
        this.javaCodeEncoding = javaCodeEncoding;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void setVersionColumnName(String versionColumnName) {
        this.versionColumnName = versionColumnName;
    }

    public void execute() {
        init();
        try {
            SchemaReader reader = createSchemaReader();
            generate(convert(reader.getDbTableDescs(schemaName)));
        } finally {
            destroy();
        }
    }

    protected void init() {
        setupDefaults();
        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(diconName);
            SingletonS2ContainerFactory.init();
        }
        S2Container container = SingletonS2ContainerFactory.getContainer();
        JdbcManagerImplementor jdbcManager = (JdbcManagerImplementor) container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        dialect = GenDialectManager.getGenerationDialect(jdbcManager
                .getDialect());
        if (container.hasComponentDef(NamingConvention.class)) {
            NamingConvention nc = (NamingConvention) container
                    .getComponent(NamingConvention.class);
            if (nc.getRootPackageNames().length > 0) {
                if (nc.getRootPackageNames().length > 1) {
                    throw new TooManyRootPackageNameRuntimeException();
                }
                if (!ROOT_PACKAGE_NAME.equals(rootPackageName)) {
                    rootPackageName = nc.getRootPackageNames()[0];
                }
                if (!ENTITY_PACKAGE_NAME.equals(entityPackageName)) {
                    entityPackageName = nc.getEntityPackageName();
                }
                if (!ENTITY_BASE_PACKAGE_NAME.equals(entityBasePackageName)) {
                    entityBasePackageName = nc.getEntityPackageName();
                }
            }
        }
    }

    protected void setupDefaults() {
        if (diconName == null) {
            diconName = DICON_NAME;
        }
        if (jdbcManagerName == null) {
            jdbcManagerName = JDBC_MANAGER_NAME;
        }
        if (rootPackageName == null) {
            rootPackageName = ROOT_PACKAGE_NAME;
        }
        if (entityPackageName == null) {
            entityPackageName = ENTITY_PACKAGE_NAME;
        }
        if (entityBasePackageName == null) {
            entityBasePackageName = ENTITY_PACKAGE_NAME;
        }
        if (baseClassNameSuffix == null) {
            baseClassNameSuffix = GAP_CLASS_NAME_SUFFIX;
        }
        if (destDir == null) {
            destDir = DEST_DIR;
        }
        if (javaCodeEncoding == null) {
            javaCodeEncoding = JAVA_CODE_ENCODING;
        }
        if (templateDir == null) {
            templateDir = TEMPLATE_DIR;
        }
        if (templateEncoding == null) {
            templateEncoding = TEMPLATE_ENCODING;
        }
        if (entityTemplateName == null) {
            entityTemplateName = ENTITY_TEMPLATE_NAME;
        }
        if (entityBaseTemplateName == null) {
            entityBaseTemplateName = ENTITU_BASE_TEMPLATE_NAME;
        }
        if (schemaName == null) {
            schemaName = SCHEMA_NAME;
        }
        if (versionColumnName == null) {
            versionColumnName = VERSION_COLUMN_NAME;
        }
    }

    protected void destroy() {
        if (initialized) {
            SingletonS2ContainerFactory.destroy();
        }
    }

    protected SchemaReader createSchemaReader() {
        return new SchemaReaderImpl(dataSource, dialect);
    }

    protected List<EntityModel> convert(List<DbTableDesc> tableDescs) {
        List<EntityModel> entityModels = new ArrayList<EntityModel>();
        EntityModelConverter entityConverter = createEntityModelConverter();
        for (DbTableDesc tableDesc : tableDescs) {
            EntityModel entityModel = entityConverter.convert(tableDesc);
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    protected EntityModelConverter createEntityModelConverter() {
        return new EntityModelConverterImpl(persistenceConvention,
                createPropertyModelConverter());
    }

    protected PropertyModelConverter createPropertyModelConverter() {
        return new PropertyModelConverterImpl(persistenceConvention, dialect,
                versionColumnName);
    }

    protected void generate(List<EntityModel> entityModels) {
        JavaCodeGenerator generator = createJavaCodeGenerator();
        for (EntityModel entityModel : entityModels) {
            JavaCode entityCode = createEntityCode(entityModel);
            if (!exists(entityCode, destDir)) {
                generator.generate(entityCode);
            }
            JavaCode entityBaseCode = createEntityBaseCode(entityModel);
            generator.generate(entityBaseCode);
        }
    }

    protected JavaCodeGenerator createJavaCodeGenerator() {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        ConfigurationUtil.setDirectoryForTemplateLoading(cfg, templateDir);
        return new JavaCodeGeneratorImpl(cfg, destDir, templateEncoding);
    }

    protected EntityCode createEntityCode(EntityModel entityModel) {
        String entityClassName = getEntityClassName(entityModel.getName());
        String entityBaseClassName = getEntityBaseClassName(entityModel
                .getName());
        return new EntityCode(entityModel, entityClassName,
                entityBaseClassName, entityTemplateName);
    }

    protected EntityBaseCode createEntityBaseCode(EntityModel entityModel) {
        String entityBaseClassName = getEntityBaseClassName(entityModel
                .getName());
        return new EntityBaseCode(entityModel, entityBaseClassName,
                entityBaseTemplateName);
    }

    protected String getEntityClassName(String entityName) {
        return getClassName(rootPackageName, entityPackageName, entityName);
    }

    protected String getEntityBaseClassName(String entityName) {
        return getClassName(rootPackageName, entityBasePackageName,
                baseClassNameSuffix + entityName);
    }

    protected String getClassName(String rootPackageName,
            String subPackageName, String className) {
        String fullPackageName = ClassUtil.concatName(rootPackageName,
                subPackageName);
        return ClassUtil.concatName(fullPackageName, className);
    }

    protected boolean exists(JavaCode javaCode, File baseDir) {
        return javaCode.getFile(baseDir).exists();
    }
}
