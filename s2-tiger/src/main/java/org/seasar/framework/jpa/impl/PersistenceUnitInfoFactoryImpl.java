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
package org.seasar.framework.jpa.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

import org.seasar.extension.datasource.impl.SingletonDataSourceProxy;
import org.seasar.extension.j2ee.JndiResourceLocator;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.env.Env;
import org.seasar.framework.jpa.PersistenceUnitInfoFactory;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.SAXParserFactoryUtil;
import org.seasar.framework.util.SchemaUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.URLUtil;
import org.seasar.framework.util.tiger.CollectionsUtil;
import org.seasar.framework.xml.SaxHandler;
import org.seasar.framework.xml.SaxHandlerParser;
import org.seasar.framework.xml.TagHandler;
import org.seasar.framework.xml.TagHandlerContext;
import org.seasar.framework.xml.TagHandlerRule;
import org.xml.sax.Attributes;

/**
 * 指定された<code>META-INF/persistence.xml</code>を読み込んで
 * {@link PersistenceUnitInfo 永続ユニット情報}を作成するファクトリの実装クラスです。
 * 
 * @author koichik
 */
public class PersistenceUnitInfoFactoryImpl implements
        PersistenceUnitInfoFactory {

    /** デフォルトの永続ユニットプロバイダクラス名 (Hibernate EntityManager) */
    public static final String DEFAULT_PROVIDER = "org.hibernate.ejb.HibernatePersistence";

    /** デフォルトのデータソース名 */
    public static final String DEFAULT_DATASOURCE = "jdbc/dataSource";

    /** <code>persistence.xml</code>のパス名 */
    public static final String PERSISTENCE_XML = "META-INF/persistence.xml";

    /** <code>persistence.xml</code>を検証するXML Schemaのパス名 */
    public static final String PERSISTENCE_SCHEMA_NAME = "persistence_1_0.xsd";

    /** 永続ユニットルートURLのコンテキストキー */
    public static final String PERSISTENCE_UNIT_ROOT_URL = "persistenceUnitRootUrl";

    /** <code>persistence.xml</code>をロードするクラスローダ */
    protected ClassLoader classLoader;

    /** <code>persistence.xml</code>を検証するXML Schema */
    protected Schema persistenceXmlSchema;

    /** S2コンテナ */
    @Binding(bindingType = BindingType.MUST)
    protected S2Container container;

    /** データソースのプロクシを使う場合は<code>true</code> */
    protected boolean useDataSourceProxy = Env.getValue().startsWith("ut");

    /** デフォルトの永続ユニットプロバイダクラス名 */
    protected String defaultProviderClassName = DEFAULT_PROVIDER;

    /** JTA用のデフォルトのデータソース名 */
    protected String defaultJtaDataSource = DEFAULT_DATASOURCE;

    /** 非JTA用のデフォルトのデータソース名 */
    protected String defaultNonJtaDataSource = DEFAULT_DATASOURCE;

    /**
     * コンテキストクラスローダを使用してインスタンスを構築します。
     * 
     */
    public PersistenceUnitInfoFactoryImpl() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * 指定のクラスローダを使用してインスタンスを構築します。
     * 
     * @param classLoader
     *            <code>persistence.xml</code>をロードするクラスローダ
     */
    public PersistenceUnitInfoFactoryImpl(final ClassLoader classLoader) {
        this.classLoader = classLoader;
        final URL schemaUrl = classLoader.getResource(PERSISTENCE_SCHEMA_NAME);
        persistenceXmlSchema = SchemaUtil.newW3cXmlSchema(schemaUrl);
    }

    /**
     * データソースのプロクシを使う場合は<code>true</code>、それ以外の場合は<code>false</code>を設定します。
     * 
     * @param useDataSourceProxy
     *            データソースのプロクシを使う場合は<code>true</code>
     */
    @Binding(bindingType = BindingType.MAY)
    public void setUseDataSourceProxy(final boolean useDataSourceProxy) {
        this.useDataSourceProxy = useDataSourceProxy;
    }

    /**
     * デフォルトの永続ユニットプロバイダクラス名を設定します。
     * 
     * @param defaultProviderClassName
     *            デフォルトの永続ユニットプロバイダクラス名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDefaultProviderClassName(
            final String defaultProviderClassName) {
        this.defaultProviderClassName = defaultProviderClassName;
    }

    /**
     * JTA用のデフォルトのデータソース名を設定します。
     * 
     * @param defaultJtaDataSource
     *            JTA用のデフォルトのデータソース名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDefaultJtaDataSource(final String defaultJtaDataSource) {
        this.defaultJtaDataSource = defaultJtaDataSource;
    }

    /**
     * 非JTA用のデフォルトのデータソース名を設定します。
     * 
     * @param defaultNonJtaDataSource
     *            非JTA用のデフォルトのデータソース名
     */
    @Binding(bindingType = BindingType.MAY)
    public void setDefaultNonJtaDataSource(final String defaultNonJtaDataSource) {
        this.defaultNonJtaDataSource = defaultNonJtaDataSource;
    }

    public List<PersistenceUnitInfo> createPersistenceUnitInfo(
            final URL persistenceXmlUrl) {
        return createPersistenceUnitInfo(persistenceXmlUrl,
                toPersistenceUnitRootUrl(persistenceXmlUrl));
    }

    @SuppressWarnings("unchecked")
    public List<PersistenceUnitInfo> createPersistenceUnitInfo(
            final URL persistenceXmlUrl, final URL persistenceUnitRootUrl) {
        final SaxHandlerParser parser = createSaxHandlerParser(persistenceUnitRootUrl);
        final InputStream is = URLUtil.openStream(persistenceXmlUrl);
        try {
            return (List<PersistenceUnitInfo>) parser.parse(is,
                    persistenceUnitRootUrl.toExternalForm());
        } finally {
            InputStreamUtil.close(is);
        }
    }

    /**
     * <code>META-INF/persistence.xml</code>のURLから永続ユニットのルートURLを求めて返します。
     * 
     * @param url
     *            <code>META-INF/persistence.xml</code>のURL
     * @return 永続ユニットのルートURL
     */
    protected static URL toPersistenceUnitRootUrl(final URL url) {
        final String s = url.toExternalForm();
        if (!s.endsWith(PERSISTENCE_XML)) {
            throw new IllegalArgumentException(s);
        }
        final String rootUrl = s.substring(0, s.length()
                - PERSISTENCE_XML.length());
        return URLUtil.create(url, rootUrl);
    }

    /**
     * XML Schemaを使用して妥当性を検証する{@link SaxHandlerParser}を作成します。
     * 
     * @param persistenceUnitRootUrl
     *            永続ユニットのルートURL
     * @return XML Schemaを使用して妥当性を検証する{@link SaxHandlerParser}
     */
    protected SaxHandlerParser createSaxHandlerParser(
            final URL persistenceUnitRootUrl) {
        final SaxHandler handler = new SaxHandler(
                new PersistenceXmlTagHandlerRule());
        final TagHandlerContext ctx = handler.getTagHandlerContext();
        ctx.addParameter(PERSISTENCE_UNIT_ROOT_URL, persistenceUnitRootUrl);

        final SAXParserFactory factory = SAXParserFactoryUtil.newInstance();
        factory.setNamespaceAware(true);
        factory.setSchema(persistenceXmlSchema);
        final SAXParser saxParser = SAXParserFactoryUtil.newSAXParser(factory);

        return new SaxHandlerParser(handler, saxParser);
    }

    private class PersistenceXmlTagHandlerRule extends TagHandlerRule {

        private static final long serialVersionUID = 1L;

        private PersistenceXmlTagHandlerRule() {
            addTagHandler("/persistence", new PersistenceTagHandler());
            addTagHandler("persistence-unit", new PersistenceUnitTagHandler());
            addTagHandler("provider", new ProviderTagHandler());
            addTagHandler("jta-data-source", new JtaDataSourceTagHandler());
            addTagHandler("non-jta-data-source",
                    new NonJtaDataSourceTagHandler());
            addTagHandler("mapping-file", new MappingFileTagHandler());
            addTagHandler("jar-file", new JarFileTagHandler());
            addTagHandler("class", new ClassTagHandler());
            addTagHandler("exclude-unlisted-classes",
                    new ExcludeUnlistedClassesTagHandler());
            addTagHandler("property", new PropertyTagHandler());
        }

    }

    private class DefaultTagHandler extends TagHandler {

        private static final long serialVersionUID = 1L;

        /**
         * 永続ユニット情報を返します。
         * 
         * @param context
         *            コンテキスト
         * @return 永続ユニット情報
         */
        protected PersistenceUnitInfoImpl getPersistenceUnitInfo(
                final TagHandlerContext context) {
            return PersistenceUnitInfoImpl.class.cast(context.peek());
        }

        /**
         * データソースを返します。
         * 
         * @param name
         *            データソースのJNDI名
         * @return データソース
         */
        protected DataSource getDataSource(final String name) {
            if (useDataSourceProxy) {
                return new SingletonDataSourceProxy(name);
            }
            final String componentName = JndiResourceLocator.resolveName(name);
            return DataSource.class.cast(container.getComponent(componentName));
        }

        /**
         * 永続ユニットのルートURLを返します。
         * 
         * @param context
         *            コンテキスト
         * @return 永続ユニットのルートURL
         */
        protected URL getPersistenceUnitRootURL(final TagHandlerContext context) {
            return URL.class.cast(context
                    .getParameter(PERSISTENCE_UNIT_ROOT_URL));
        }

    }

    private class PersistenceTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void start(final TagHandlerContext context,
                final Attributes attributes) {
            final List<PersistenceUnitInfo> list = CollectionsUtil
                    .newArrayList();
            context.push(list);
        }

    }

    private class PersistenceUnitTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void start(final TagHandlerContext context,
                final Attributes attributes) {
            final URL persistenceUnitRootUrl = URL.class.cast(context
                    .getParameter(PERSISTENCE_UNIT_ROOT_URL));
            final PersistenceUnitInfoImpl info = new PersistenceUnitInfoImpl(
                    classLoader, persistenceUnitRootUrl);
            info.setPersistenceUnitName(attributes.getValue("name"));
            final String transactionType = attributes
                    .getValue("transaction-type");
            if (!StringUtil.isEmpty(transactionType)) {
                info.setTransactionType(PersistenceUnitTransactionType
                        .valueOf(transactionType));
            }
            context.push(info);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void end(final TagHandlerContext context, final String body) {
            final PersistenceUnitInfoImpl info = PersistenceUnitInfoImpl.class
                    .cast(context.pop());
            if (StringUtil.isEmpty(info.getPersistenceProviderClassName())) {
                info.setPersistenceProviderClassName(defaultProviderClassName);
            }
            if (info.getJtaDataSource() == null) {
                info.setJtaDataSource(getDataSource(defaultJtaDataSource));
            }
            if (info.getNonJtaDataSource() == null) {
                info
                        .setNonJtaDataSource(getDataSource(defaultNonJtaDataSource));
            }
            final List<PersistenceUnitInfo> list = List.class.cast(context
                    .peek());
            list.add(info);
        }

    }

    private class ProviderTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            getPersistenceUnitInfo(context).setPersistenceProviderClassName(
                    body);
        }

    }

    private class JtaDataSourceTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            getPersistenceUnitInfo(context).setJtaDataSource(
                    getDataSource(body));
        }

    }

    private class NonJtaDataSourceTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            getPersistenceUnitInfo(context).setNonJtaDataSource(
                    getDataSource(body));
        }

    }

    private class MappingFileTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            getPersistenceUnitInfo(context).addMappingFileNames(body);
        }

    }

    @SuppressWarnings("unchecked")
    private class JarFileTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            final URL url = URLUtil.create(getPersistenceUnitRootURL(context),
                    body);
            getPersistenceUnitInfo(context).addJarFileUrls(url);
        }

    }

    private class ClassTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            getPersistenceUnitInfo(context).addManagedClassNames(body);
        }

    }

    private class ExcludeUnlistedClassesTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void end(final TagHandlerContext context, final String body) {
            final String trimed = body.trim();
            final boolean value = "true".equals(trimed) || "1".equals(trimed);
            getPersistenceUnitInfo(context).setExcludeUnlistedClasses(value);
        }

    }

    private class PropertyTagHandler extends DefaultTagHandler {

        private static final long serialVersionUID = 1L;

        @Override
        public void start(final TagHandlerContext context,
                final Attributes attributes) {
            final String name = attributes.getValue("name");
            final String value = attributes.getValue("value");
            getPersistenceUnitInfo(context).addProperties(name, value);
        }

    }

}
