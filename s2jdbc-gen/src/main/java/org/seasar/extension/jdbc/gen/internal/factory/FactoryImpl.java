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
package org.seasar.extension.jdbc.gen.internal.factory;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.persistence.GenerationType;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.DbmsDialect;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.data.Loader;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
import org.seasar.extension.jdbc.gen.event.GenDdlListener;
import org.seasar.extension.jdbc.gen.generator.GenerationContext;
import org.seasar.extension.jdbc.gen.generator.Generator;
import org.seasar.extension.jdbc.gen.internal.data.DumperImpl;
import org.seasar.extension.jdbc.gen.internal.data.LoaderImpl;
import org.seasar.extension.jdbc.gen.internal.desc.DatabaseDescFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.desc.EntitySetDescFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.generator.GenerationContextImpl;
import org.seasar.extension.jdbc.gen.internal.generator.GeneratorImpl;
import org.seasar.extension.jdbc.gen.internal.meta.DbTableMetaReaderImpl;
import org.seasar.extension.jdbc.gen.internal.meta.EntityMetaReaderImpl;
import org.seasar.extension.jdbc.gen.internal.model.AbstServiceModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.AssociationModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.AttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.CompositeUniqueConstraintModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ConditionAssociationModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ConditionAttributeModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ConditionModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.EntityTestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.NamesAggregateModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ServiceTestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.SqlFileConstantsModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.SqlFileTestModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.TableModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.provider.ValueTypeProviderImpl;
import org.seasar.extension.jdbc.gen.internal.sql.SqlFileExecutorImpl;
import org.seasar.extension.jdbc.gen.internal.sql.SqlUnitExecutorImpl;
import org.seasar.extension.jdbc.gen.internal.version.DdlVersionDirectoryTreeImpl;
import org.seasar.extension.jdbc.gen.internal.version.DdlVersionIncrementerImpl;
import org.seasar.extension.jdbc.gen.internal.version.MigraterImpl;
import org.seasar.extension.jdbc.gen.internal.version.SchemaInfoTableImpl;
import org.seasar.extension.jdbc.gen.meta.DbTableMetaReader;
import org.seasar.extension.jdbc.gen.meta.EntityMetaReader;
import org.seasar.extension.jdbc.gen.model.AbstServiceModelFactory;
import org.seasar.extension.jdbc.gen.model.ConditionModelFactory;
import org.seasar.extension.jdbc.gen.model.EntityModelFactory;
import org.seasar.extension.jdbc.gen.model.EntityTestModelFactory;
import org.seasar.extension.jdbc.gen.model.NamesAggregateModelFactory;
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;
import org.seasar.extension.jdbc.gen.model.ServiceModelFactory;
import org.seasar.extension.jdbc.gen.model.ServiceTestModelFactory;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantNamingRule;
import org.seasar.extension.jdbc.gen.model.SqlFileConstantsModelFactory;
import org.seasar.extension.jdbc.gen.model.SqlFileTestModelFactory;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModelFactory;
import org.seasar.extension.jdbc.gen.provider.ValueTypeProvider;
import org.seasar.extension.jdbc.gen.sql.SqlFileExecutor;
import org.seasar.extension.jdbc.gen.sql.SqlUnitExecutor;
import org.seasar.extension.jdbc.gen.version.DdlVersionDirectoryTree;
import org.seasar.extension.jdbc.gen.version.DdlVersionIncrementer;
import org.seasar.extension.jdbc.gen.version.Migrater;
import org.seasar.extension.jdbc.gen.version.SchemaInfoTable;
import org.seasar.framework.convention.PersistenceConvention;

/**
 * {@link Factory}の実装クラスです。
 * 
 * @author taedium
 */
public class FactoryImpl implements Factory {

    public EntityMetaReader createEntityMetaReader(Command command,
            File classpathDir, String packageName,
            EntityMetaFactory entityMetaFactory, String shortClassNamePattern,
            String ignoreShortClassNamePattern, boolean readComment,
            List<File> javaFileSrcDirList, String javaFileEncoding) {

        return new EntityMetaReaderImpl(classpathDir, packageName,
                entityMetaFactory, shortClassNamePattern,
                ignoreShortClassNamePattern, readComment, javaFileSrcDirList,
                javaFileEncoding);
    }

    public DatabaseDescFactory createDatabaseDescFactory(Command command,
            EntityMetaFactory entityMetaFactory,
            EntityMetaReader entityMetaReader, GenDialect dialect,
            ValueTypeProvider valueTypeProvider, boolean regardRelationshipAsFk) {

        return new DatabaseDescFactoryImpl(entityMetaFactory, entityMetaReader,
                dialect, valueTypeProvider, regardRelationshipAsFk);
    }

    public Dumper createDumper(Command command, GenDialect dialect,
            String dumpFileEncoding) {

        return new DumperImpl(dialect, dumpFileEncoding);
    }

    public SqlUnitExecutor createSqlUnitExecutor(Command command,
            DataSource dataSource, UserTransaction userTransaction,
            boolean haltOnError) {

        return new SqlUnitExecutorImpl(dataSource, userTransaction, haltOnError);
    }

    public DbTableMetaReader createDbTableMetaReader(Command command,
            DataSource dataSource, GenDialect dialect, String schemaName,
            String tableNamePattern, String ignoreTableNamePattern,
            boolean readComment) {

        return new DbTableMetaReaderImpl(dataSource, dialect, schemaName,
                tableNamePattern, ignoreTableNamePattern, readComment);
    }

    public SqlFileExecutor createSqlFileExecutor(Command command,
            GenDialect dialect, String sqlFileEncoding,
            char statementDelimiter, String blockDelimiter) {

        return new SqlFileExecutorImpl(dialect, sqlFileEncoding,
                statementDelimiter, blockDelimiter);
    }

    public ConditionModelFactory createConditionModelFactory(Command command,
            String packageName, String conditionClassNameSuffix) {

        ConditionAttributeModelFactoryImpl attributeModelFactory = new ConditionAttributeModelFactoryImpl();
        ConditionAssociationModelFactoryImpl associationModelFactory = new ConditionAssociationModelFactoryImpl(
                conditionClassNameSuffix);
        return new ConditionModelFactoryImpl(attributeModelFactory,
                associationModelFactory, packageName, conditionClassNameSuffix);
    }

    public Generator createGenerator(Command command,
            String templateFileEncoding, File templateFilePrimaryDir) {

        return new GeneratorImpl(templateFileEncoding, templateFilePrimaryDir);
    }

    public DdlVersionDirectoryTree createDdlVersionDirectoryTree(
            Command command, File baseDir, File versionFile,
            String versionNoPattern, String env, boolean applyEnvToVersion) {

        return new DdlVersionDirectoryTreeImpl(baseDir, versionFile,
                versionNoPattern, applyEnvToVersion ? env : null);
    }

    public DdlVersionIncrementer createDdlVersionIncrementer(Command command,
            DdlVersionDirectoryTree ddlVersionDirectoryTree,
            GenDdlListener genDdlListener, GenDialect dialect,
            DataSource dataSource, List<String> createDirNameList,
            List<String> dropFileNameList) {

        return new DdlVersionIncrementerImpl(ddlVersionDirectoryTree,
                genDdlListener, dialect, dataSource, createDirNameList,
                dropFileNameList);
    }

    public TableModelFactory createTableModelFactory(Command command,
            GenDialect dialect, DataSource dataSource,
            SqlIdentifierCaseType sqlIdentifierCaseType,
            SqlKeywordCaseType sqlKeywordCaseType, char statementDelimiter,
            String tableOption, boolean useComment) {

        return new TableModelFactoryImpl(dialect, dataSource,
                sqlIdentifierCaseType, sqlKeywordCaseType, statementDelimiter,
                tableOption, useComment);
    }

    public EntitySetDescFactory createEntitySetDescFactory(Command command,
            DbTableMetaReader dbTableMetaReader,
            PersistenceConvention persistenceConvention, GenDialect dialect,
            String versionColumnNamePattern, File pluralFormFile,
            GenerationType generationType, Integer initialValue,
            Integer allocationSize) {

        return new EntitySetDescFactoryImpl(dbTableMetaReader,
                persistenceConvention, dialect, versionColumnNamePattern,
                pluralFormFile, generationType, initialValue, allocationSize);
    }

    public EntityModelFactory createEntityModelFactory(Command command,
            String packageName, Class<?> superclass, boolean useTemporalType,
            boolean useAccessor, boolean useComment, boolean showCatalogName,
            boolean showSchemaName, boolean showTableName,
            boolean showColumnName, boolean showColumnDefinition,
            boolean showJoinColumn, PersistenceConvention persistenceConvention) {

        return new EntityModelFactoryImpl(packageName, superclass,
                new AttributeModelFactoryImpl(showColumnName,
                        showColumnDefinition, useTemporalType,
                        persistenceConvention),
                new AssociationModelFactoryImpl(showJoinColumn),
                new CompositeUniqueConstraintModelFactoryImpl(), useAccessor,
                useComment, showCatalogName, showSchemaName, showTableName);
    }

    public ServiceModelFactory createServiceModelFactory(Command command,
            String packageName, String serviceClassNameSuffix,
            NamesModelFactory namesModelFactory, boolean useNamesClass,
            String jdbcManagerName) {

        return new ServiceModelFactoryImpl(packageName, serviceClassNameSuffix,
                namesModelFactory, useNamesClass, jdbcManagerName);
    }

    public ServiceTestModelFactory createServiceTestModelFactory(
            Command command, String configPath, String packageName,
            String serviceClassNameSuffix, String testClassNameSuffix,
            boolean useS2junit4) {

        return new ServiceTestModelFactoryImpl(configPath, packageName,
                serviceClassNameSuffix, testClassNameSuffix, useS2junit4);
    }

    public AbstServiceModelFactory createAbstServiceModelFactory(
            Command command, String packageName, String serviceClassNameSuffix) {

        return new AbstServiceModelFactoryImpl(packageName,
                serviceClassNameSuffix);
    }

    public EntityTestModelFactory createEntityTestModelFactory(Command command,
            String configPath, String jdbcManagerName,
            String testClassNameSuffix, NamesModelFactory namesModelFactory,
            boolean useNamesClass, boolean useS2junit4) {

        return new EntityTestModelFactoryImpl(configPath, jdbcManagerName,
                testClassNameSuffix, namesModelFactory, useNamesClass,
                useS2junit4);
    }

    public NamesModelFactory createNamesModelFactory(Command command,
            String packageName, String namesClassNameSuffix) {

        return new NamesModelFactoryImpl(packageName, namesClassNameSuffix);
    }

    public NamesAggregateModelFactory createNamesAggregateModelFactory(
            Command command, String packageName, String shortClassName) {

        return new NamesAggregateModelFactoryImpl(packageName, shortClassName);
    }

    public SchemaInfoTable createSchemaInfoTable(Command command,
            DataSource dataSource, GenDialect dialect, String fullTableName,
            String columnName) {

        return new SchemaInfoTableImpl(dataSource, dialect, fullTableName,
                columnName);
    }

    public Migrater createMigrater(Command command,
            SqlUnitExecutor sqlUnitExecutor, SchemaInfoTable schemaInfoTable,
            DdlVersionDirectoryTree ddlVersionDirectoryTree, String version,
            String env) {

        return new MigraterImpl(sqlUnitExecutor, schemaInfoTable,
                ddlVersionDirectoryTree, version, env);
    }

    public Loader createLoader(Command command, GenDialect dialect,
            String dumpFileEncoding, int batchSize, boolean delete) {

        return new LoaderImpl(dialect, dumpFileEncoding, batchSize, delete);
    }

    public GenerationContext createGenerationContext(Command command,
            Object model, File file, String templateName, String encoding,
            boolean overwrite) {

        return new GenerationContextImpl(model, file, templateName, encoding,
                overwrite);
    }

    public ValueTypeProvider createValueTypeProvider(Command command,
            DbmsDialect dbmsDialect) {

        return new ValueTypeProviderImpl(dbmsDialect);
    }

    public SqlFileTestModelFactory createSqlFileTestModelFactory(
            Command command, File classpathDir, Set<File> sqlFileSet,
            String configPath, String jdbcManagerName, String packageName,
            String shortClassName, boolean useS2junit4) {

        return new SqlFileTestModelFactoryImpl(classpathDir, sqlFileSet,
                configPath, jdbcManagerName, packageName, shortClassName,
                useS2junit4);
    }

    public SqlFileConstantsModelFactory createSqlFileConstantsModelFactory(
            Command command, File classpathDir, Set<File> sqlFileSet,
            SqlFileConstantNamingRule sqlFileConstantNamingRule,
            String packageName, String shortClassName) {

        return new SqlFileConstantsModelFactoryImpl(classpathDir, sqlFileSet,
                sqlFileConstantNamingRule, packageName, shortClassName);
    }

}
