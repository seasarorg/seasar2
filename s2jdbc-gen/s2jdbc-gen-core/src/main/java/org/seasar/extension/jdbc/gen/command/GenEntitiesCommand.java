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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.seasar.extension.jdbc.gen.GenDialect;
import org.seasar.extension.jdbc.gen.JavaCode;
import org.seasar.extension.jdbc.gen.JavaCodeGenerator;
import org.seasar.extension.jdbc.gen.converter.EntityModelConverter;
import org.seasar.extension.jdbc.gen.converter.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.dialect.GenDialectManager;
import org.seasar.extension.jdbc.gen.exception.TooManyRootPackageNameRuntimeException;
import org.seasar.extension.jdbc.gen.generator.JavaCodeGeneratorImpl;
import org.seasar.extension.jdbc.gen.javacode.EntityBaseCode;
import org.seasar.extension.jdbc.gen.javacode.EntityCode;
import org.seasar.extension.jdbc.gen.model.EntityModel;
import org.seasar.extension.jdbc.gen.model.TableModel;
import org.seasar.extension.jdbc.gen.reader.SchemaReader;
import org.seasar.extension.jdbc.gen.util.ConfigurationUtil;
import org.seasar.extension.jdbc.gen.util.JavaFileUtil;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.util.ConnectionUtil;
import org.seasar.extension.jdbc.util.DataSourceUtil;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.util.ClassUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

/**
 * @author taedium
 * 
 */
public class GenEntitiesCommand {

    protected String baseClassNameSuffix;

    protected String rootPackageName;

    protected String entityPackageName;

    protected String entityBasePackageName;

    protected String jdbcManagerName;

    protected File templateDir;

    protected File destDir;

    protected String templateEncoding;

    protected String javaCodeEncoding;

    protected String versionColumnName;

    protected String entityTemplateName;

    protected String entityBaseTemplateName;

    protected String diconName;

    protected String schemaName;

    protected boolean initialized;

    protected Configuration configuration;

    protected DataSource dataSource;

    protected GenDialect generationDialect;

    protected PersistenceConvention persistenceConvention;

    public void setTemplateEncoding(String encoding) {
        this.templateEncoding = encoding;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public void setEntityBasePackageName(String entityGapPackageName) {
        this.entityBasePackageName = entityGapPackageName;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setVersionColumnName(String versionColumn) {
        this.versionColumnName = versionColumn;
    }

    public void setDiconName(String dicon) {
        this.diconName = dicon;
    }

    public GenEntitiesCommand() {
    }

    public void execute() {
        init();
        try {
            generate(convert(getTableModels()));
        } finally {
            destroy();
        }
    }

    protected void init() {
        setupDefaults();
        initContainer();
    }

    protected void setupDefaults() {
        if (baseClassNameSuffix == null) {
            baseClassNameSuffix = GenEntitiesConstants.GAP_CLASS_NAME_SUFFIX;
        }
        if (rootPackageName == null) {
            rootPackageName = GenEntitiesConstants.ROOT_PACKAGE_NAME;
        }
        if (entityPackageName == null) {
            entityPackageName = GenEntitiesConstants.ENTITY_PACKAGE_NAME;
        }
        if (entityBasePackageName == null) {
            entityBasePackageName = GenEntitiesConstants.ENTITY_PACKAGE_NAME;
        }
        if (jdbcManagerName == null) {
            jdbcManagerName = GenEntitiesConstants.JDBC_MANAGER_NAME;
        }
        if (templateDir == null) {
            templateDir = GenEntitiesConstants.TEMPLATE_DIR;
        }
        if (destDir == null) {
            destDir = GenEntitiesConstants.DEST_DIR;
        }
        if (templateEncoding == null) {
            templateEncoding = GenEntitiesConstants.ENCODING;
        }
        if (javaCodeEncoding == null) {
            javaCodeEncoding = GenEntitiesConstants.ENCODING;
        }
        if (versionColumnName == null) {
            versionColumnName = GenEntitiesConstants.VERSION_COLUMN_NAME;
        }
        if (entityTemplateName == null) {
            entityTemplateName = GenEntitiesConstants.ENTITY_TEMPLATE_NAME;
        }
        if (entityBaseTemplateName == null) {
            entityBaseTemplateName = GenEntitiesConstants.ENTITU_BASE_TEMPLATE_NAME;
        }
        if (diconName == null) {
            diconName = GenEntitiesConstants.DICON_NAME;
        }
        if (schemaName == null) {
            schemaName = GenEntitiesConstants.SCHEMA_NAME;
        }
    }

    protected void initContainer() {
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
        generationDialect = GenDialectManager
                .getGenerationDialect(jdbcManager.getDialect());
        if (container.hasComponentDef(NamingConvention.class)) {
            NamingConvention nc = (NamingConvention) container
                    .getComponent(NamingConvention.class);
            String[] rootPackageNames = nc.getRootPackageNames();
            if (rootPackageNames.length > 1) {
                throw new TooManyRootPackageNameRuntimeException();
            }
            if (rootPackageNames.length == 1) {
                rootPackageName = rootPackageNames[0];
            }
            entityPackageName = nc.getEntityPackageName();
            entityBasePackageName = entityPackageName;
        }
    }

    protected void destroy() {
        if (initialized) {
            SingletonS2ContainerFactory.destroy();
        }
    }

    protected List<TableModel> getTableModels() {
        Connection con = DataSourceUtil.getConnection(dataSource);
        try {
            DatabaseMetaData metaData = ConnectionUtil.getMetaData(con);
            return createSchemaReader(metaData).getTableModels(null);
        } finally {
            ConnectionUtil.close(con);
        }
    }

    protected SchemaReader createSchemaReader(DatabaseMetaData metaData) {
        return new SchemaReader(metaData, generationDialect);
    }

    protected List<EntityModel> convert(List<TableModel> tableModels) {
        List<EntityModel> entityModels = new ArrayList<EntityModel>();
        EntityModelConverter converter = createEntityModelConverter();
        for (TableModel tableModel : tableModels) {
            EntityModel entityModel = converter.convert(tableModel);
            entityModels.add(entityModel);
        }
        return entityModels;
    }

    protected EntityModelConverter createEntityModelConverter() {
        EntityModelConverter converter = new EntityModelConverter();
        converter.setPersistenceConvention(persistenceConvention);
        converter.setPropertyModelConverter(createPropertyModelConverter());
        return converter;
    }

    protected PropertyModelConverter createPropertyModelConverter() {
        PropertyModelConverter converter = new PropertyModelConverter();
        converter.setPersistenceConvention(persistenceConvention);
        converter.setGenerationDialect(generationDialect);
        converter.setVersionColumn(versionColumnName);
        return converter;
    }

    protected void generate(List<EntityModel> entityModels) {
        JavaCodeGenerator generator = createJavaCodeGenerator();
        for (EntityModel entityModel : entityModels) {
            JavaCode entityCode = createEntityCode(entityModel);
            if (!exists(entityCode)) {
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

    protected boolean exists(JavaCode javaCode) {
        String fileName = JavaFileUtil.getJavaFileName(javaCode.getClassName());
        File javaFile = new File(destDir, fileName);
        return javaFile.exists();
    }

    protected EntityCode createEntityCode(EntityModel entityModel) {
        String entityName = entityModel.getName();
        return new EntityCode(entityModel, getEntityClassName(entityName),
                getEntityBaseClassName(entityName), entityTemplateName);
    }

    protected EntityBaseCode createEntityBaseCode(EntityModel entityModel) {
        String entityName = entityModel.getName();
        return new EntityBaseCode(entityModel,
                getEntityBaseClassName(entityName), entityBaseTemplateName);
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
}
