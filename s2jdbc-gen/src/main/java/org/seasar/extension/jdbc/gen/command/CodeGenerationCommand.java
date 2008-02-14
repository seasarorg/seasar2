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

import org.seasar.extension.jdbc.gen.CodeGenerator;
import org.seasar.extension.jdbc.gen.Command;
import org.seasar.extension.jdbc.gen.GenerationDialect;
import org.seasar.extension.jdbc.gen.converter.EntityModelConverter;
import org.seasar.extension.jdbc.gen.converter.PropertyModelConverter;
import org.seasar.extension.jdbc.gen.dialect.GenerationDialectManager;
import org.seasar.extension.jdbc.gen.exception.TooManyRootPackageNameRuntimeException;
import org.seasar.extension.jdbc.gen.generator.EntityCodeGenerator;
import org.seasar.extension.jdbc.gen.generator.EntityGapCodeGenerator;
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
import freemarker.template.Template;

/**
 * @author taedium
 * 
 */
public class CodeGenerationCommand implements Command {

    protected String gapClassNameSuffix = DEFAULT_GAP_CLASS_NAME_SUFFIX;

    protected String rootPackageName = DEFAULT_ROOT_PACKAGE_NAME;

    protected String entityPackageName = DEFAULT_ENTITY_PACKAGE_NAME;

    protected String entityGapPackageName = DEFAULT_ENTITY_PACKAGE_NAME;

    protected String jdbcManagerName = DEFAULT_JDBC_MANAGER_NAME;

    protected File templateDir = DEFAULT_TEMPLATE_FILE;

    protected File destDir = DEFAULT_DEST_DIR;

    protected String encoding = DEFAULT_ENCODING;

    protected String versionColumn = DEFAULT_VERSION_COLUMN;

    protected String entityTemplate = DEFAULT_ENTITY_TEMPLATE;

    protected String entityGapTemplate = DEFAULT_ENTITU_GAP_TEMPLATE;

    protected String dicon = DEFAULT_DICON;

    protected String schema = DEFAULT_SCHEMA;

    protected boolean initialized;

    protected Configuration configuration;

    protected DataSource dataSource;

    protected GenerationDialect generationDialect;

    protected PersistenceConvention persistenceConvention;

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public void setEntityPackageName(String entityPackageName) {
        this.entityPackageName = entityPackageName;
    }

    public void setEntityGapPackageName(String entityGapPackageName) {
        this.entityGapPackageName = entityGapPackageName;
    }

    public void setDestDir(File destDir) {
        this.destDir = destDir;
    }

    public void setVersionColumn(String versionColumn) {
        this.versionColumn = versionColumn;
    }

    public void setDicon(String dicon) {
        this.dicon = dicon;
    }

    public CodeGenerationCommand() {
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
        initTemplateConfiguration();
        initContainer();
    }

    protected void initTemplateConfiguration() {
        configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        ConfigurationUtil.setDirectoryForTemplateLoading(configuration,
                templateDir);
    }

    protected void initContainer() {
        if (!SingletonS2ContainerFactory.hasContainer()) {
            initialized = true;
            SingletonS2ContainerFactory.setConfigPath(dicon);
            SingletonS2ContainerFactory.init();
        }
        S2Container container = SingletonS2ContainerFactory.getContainer();
        JdbcManagerImplementor jdbcManager = (JdbcManagerImplementor) container
                .getComponent(jdbcManagerName);
        dataSource = jdbcManager.getDataSource();
        persistenceConvention = jdbcManager.getPersistenceConvention();
        generationDialect = GenerationDialectManager
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
            entityGapPackageName = entityPackageName;
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
        converter.setVersionColumn(versionColumn);
        return converter;
    }

    protected void generate(List<EntityModel> entityModels) {
        for (EntityModel entityModel : entityModels) {
            if (!existsEntityCode(entityModel)) {
                createEntityCodeGenerator(entityModel).generate();
            }
            createEntityGapCodeGenerator(entityModel).generate();
        }
    }

    protected boolean existsEntityCode(EntityModel entityModel) {
        String className = getEntityClassName(entityModel.getName());
        String fileName = JavaFileUtil.getJavaFileName(className);
        File javaFile = new File(destDir, fileName);
        return javaFile.exists();
    }

    protected CodeGenerator createEntityCodeGenerator(EntityModel entityModel) {
        String entityName = entityModel.getName();
        return new EntityCodeGenerator(entityModel,
                getEntityClassName(entityName),
                getEntityGapClassName(entityName), entityTemplate,
                configuration, encoding, destDir);
    }

    protected CodeGenerator createEntityGapCodeGenerator(EntityModel entityModel) {
        String entityName = entityModel.getName();
        return new EntityGapCodeGenerator(entityModel,
                getEntityClassName(entityName),
                getEntityGapClassName(entityName), entityGapTemplate,
                configuration, encoding, destDir);
    }

    protected String getEntityClassName(String entityName) {
        return getClassName(rootPackageName, entityPackageName, entityName);
    }

    protected String getEntityGapClassName(String entityName) {
        return getClassName(rootPackageName, entityGapPackageName,
                gapClassNameSuffix + entityName);
    }

    protected String getClassName(String rootPackageName,
            String subPackageName, String className) {
        String fullPackageName = ClassUtil.concatName(rootPackageName,
                subPackageName);
        return ClassUtil.concatName(fullPackageName, className);
    }
}
