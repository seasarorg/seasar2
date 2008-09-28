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
package org.seasar.extension.jdbc.gen.internal.factory;

import java.io.File;
import java.util.List;

import javax.persistence.GenerationType;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.gen.command.Command;
import org.seasar.extension.jdbc.gen.data.Dumper;
import org.seasar.extension.jdbc.gen.data.Loader;
import org.seasar.extension.jdbc.gen.desc.DatabaseDescFactory;
import org.seasar.extension.jdbc.gen.desc.EntitySetDescFactory;
import org.seasar.extension.jdbc.gen.dialect.GenDialect;
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
import org.seasar.extension.jdbc.gen.internal.model.NamesModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.SchemaInfoTableModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.ServiceModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.TableModelFactoryImpl;
import org.seasar.extension.jdbc.gen.internal.model.TestModelFactoryImpl;
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
import org.seasar.extension.jdbc.gen.model.NamesModelFactory;
import org.seasar.extension.jdbc.gen.model.SchemaInfoTableModelFactory;
import org.seasar.extension.jdbc.gen.model.ServiceModelFactory;
import org.seasar.extension.jdbc.gen.model.SqlIdentifierCaseType;
import org.seasar.extension.jdbc.gen.model.SqlKeywordCaseType;
import org.seasar.extension.jdbc.gen.model.TableModelFactory;
import org.seasar.extension.jdbc.gen.model.TestModelFactory;
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
            String ignoreShortClassNamePattern) {

        return new EntityMetaReaderImpl(classpathDir, packageName,
                entityMetaFactory, shortClassNamePattern,
                ignoreShortClassNamePattern);
    }

    public DatabaseDescFactory createDatabaseDescFactory(Command command,
            EntityMetaFactory entityMetaFactory,
            EntityMetaReader entityMetaReader, GenDialect dialect) {

        return new DatabaseDescFactoryImpl(entityMetaFactory, entityMetaReader,
                dialect);
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
            String tableNamePattern, String ignoreTableNamePattern) {

        return new DbTableMetaReaderImpl(dataSource, dialect, schemaName,
                tableNamePattern, ignoreTableNamePattern);
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
            GenDialect dialect, DataSource dataSource,
            List<String> createDirNameList, List<String> dropFileNameList) {

        return new DdlVersionIncrementerImpl(ddlVersionDirectoryTree, dialect,
                dataSource, createDirNameList, dropFileNameList);
    }

    public TableModelFactory createTableModelFactory(Command command,
            GenDialect dialect, DataSource dataSource,
            SqlIdentifierCaseType sqlIdentifierCaseType,
            SqlKeywordCaseType sqlKeywordCaseType, char statementDelimiter,
            String tableOption) {

        return new TableModelFactoryImpl(dialect, dataSource,
                sqlIdentifierCaseType, sqlKeywordCaseType, statementDelimiter,
                tableOption);
    }

    public SchemaInfoTableModelFactory createSchemaInfoTableModelFactory(
            Command command, GenDialect dialect, String tableName,
            String columnName, SqlIdentifierCaseType sqlIdentifierCaseType,
            SqlKeywordCaseType sqlKeywordCaseType, char statementDelimiter,
            String tableOption) {

        return new SchemaInfoTableModelFactoryImpl(dialect, tableName,
                columnName, sqlIdentifierCaseType, sqlKeywordCaseType,
                statementDelimiter, tableOption);
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
            String packageName, Class<?> superclass, boolean useAccessor,
            boolean showCatalogName, boolean showSchemaName,
            boolean showTableName, boolean showColumnName,
            boolean showColumnDefinition, boolean showJoinColumn) {

        return new EntityModelFactoryImpl(packageName, superclass,
                new AttributeModelFactoryImpl(showColumnName,
                        showColumnDefinition), new AssociationModelFactoryImpl(
                        showJoinColumn),
                new CompositeUniqueConstraintModelFactoryImpl(), useAccessor,
                showCatalogName, showSchemaName, showTableName);
    }

    public ServiceModelFactory createServiceModelFactory(Command command,
            String packageName, String serviceClassNameSuffix,
            NamesModelFactory namesModelFactory, boolean useNamesClass,
            String jdbcManagerName) {

        return new ServiceModelFactoryImpl(packageName, serviceClassNameSuffix,
                namesModelFactory, useNamesClass, jdbcManagerName);
    }

    public AbstServiceModelFactory createAbstServiceModelFactory(
            Command command, String packageName, String serviceClassNameSuffix) {

        return new AbstServiceModelFactoryImpl(packageName,
                serviceClassNameSuffix);
    }

    public TestModelFactory createTestModelFactory(Command command,
            String configPath, String jdbcManagerName,
            String testClassNameSuffix) {

        return new TestModelFactoryImpl(configPath, jdbcManagerName,
                testClassNameSuffix);
    }

    public NamesModelFactory createNamesModelFactory(Command command,
            String packageName, String namesClassNameSuffix) {

        return new NamesModelFactoryImpl(packageName, namesClassNameSuffix);
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
            String dumpFileEncoding, int batchSize) {

        return new LoaderImpl(dialect, dumpFileEncoding, batchSize);
    }

    public GenerationContext createGenerationContext(Command command,
            Object model, File file, String templateName, String encoding,
            boolean overwrite) {

        return new GenerationContextImpl(model, file, templateName, encoding,
                overwrite);
    }
}
