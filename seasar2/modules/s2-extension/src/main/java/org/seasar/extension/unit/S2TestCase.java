/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.extension.unit;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.extension.dataset.DataReader;
import org.seasar.extension.dataset.DataRow;
import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataTable;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.impl.SqlDeleteTableWriter;
import org.seasar.extension.dataset.impl.SqlReloadReader;
import org.seasar.extension.dataset.impl.SqlReloadTableReader;
import org.seasar.extension.dataset.impl.SqlTableReader;
import org.seasar.extension.dataset.impl.SqlWriter;
import org.seasar.extension.dataset.impl.XlsReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.extension.dataset.types.ColumnTypes;
import org.seasar.extension.jdbc.UpdateHandler;
import org.seasar.extension.jdbc.impl.BasicUpdateHandler;
import org.seasar.extension.mock.servlet.MockHttpServletRequest;
import org.seasar.extension.mock.servlet.MockHttpServletResponse;
import org.seasar.extension.mock.servlet.MockHttpServletResponseImpl;
import org.seasar.extension.mock.servlet.MockServlet;
import org.seasar.extension.mock.servlet.MockServletConfig;
import org.seasar.extension.mock.servlet.MockServletConfigImpl;
import org.seasar.extension.mock.servlet.MockServletContext;
import org.seasar.extension.mock.servlet.MockServletContextImpl;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ContainerConstants;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.container.impl.S2ContainerImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ConnectionUtil;
import org.seasar.framework.util.DataSourceUtil;
import org.seasar.framework.util.FieldUtil;
import org.seasar.framework.util.FileOutputStreamUtil;
import org.seasar.framework.util.MethodUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;

/** 
 * @author higa
 * @see junit.framework.TestCase
 */
public abstract class S2TestCase extends TestCase {

	private static final String DATASOURCE_NAME = "j2ee"
			+ ContainerConstants.NS_SEP + "dataSource";

	private S2Container container_;

	private Servlet servlet_;

	private MockServletConfig servletConfig_;

	private MockServletContext servletContext_;

	private MockHttpServletRequest request_;

	private MockHttpServletResponse response_;

	private DataSource dataSource_;

	private Connection connection_;

	private DatabaseMetaData dbMetaData_;

	private List bindedFields_;

	public S2TestCase() {
	}

	public S2TestCase(String name) {
		super(name);
	}

	public S2Container getContainer() {
		return container_;
	}

	public Object getComponent(String componentName) {
		return container_.getComponent(componentName);
	}

	public Object getComponent(Class componentClass) {
		return container_.getComponent(componentClass);
	}

	public ComponentDef getComponentDef(String componentName) {
		return container_.getComponentDef(componentName);
	}

	public ComponentDef getComponentDef(Class componentClass) {
		return container_.getComponentDef(componentClass);
	}

	public void register(Class componentClass) {
		container_.register(componentClass);
	}

	public void register(Class componentClass, String componentName) {
		container_.register(componentClass, componentName);
	}

	public void register(Object component) {
		container_.register(component);
	}

	public void register(Object component, String componentName) {
		container_.register(component, componentName);
	}

	public void register(ComponentDef componentDef) {
		container_.register(componentDef);
	}

	public void include(String path) {
		S2ContainerFactory.include(container_, convertPath(path));
	}

	private String convertPath(String path) {
		if (ResourceUtil.getResourceNoException(path) != null) {
			return path;
		}
		String prefix = getClass().getName().replace('.', '/').replaceFirst("/[^/]+$", "");
		return prefix + "/" + path;
	}

	public DataSource getDataSource() {
		if (dataSource_ == null) {
			throw new EmptyRuntimeException("dataSource");
		}
		return dataSource_;
	}

	public Connection getConnection() {
		if (connection_ != null) {
			return connection_;
		}
		connection_ = DataSourceUtil.getConnection(getDataSource());
		return connection_;
	}

	public DatabaseMetaData getDatabaseMetaData() {
		if (dbMetaData_ != null) {
			return dbMetaData_;
		}
		dbMetaData_ = ConnectionUtil.getMetaData(getConnection());
		return dbMetaData_;
	}

	public DataSet readXls(String path) {
		DataReader reader = new XlsReader(convertPath(path));
		return reader.read();
	}

	public void writeXls(String path, DataSet dataSet) {
		File dir = ResourceUtil.getBuildDir(getClass());
		File file = new File(dir, convertPath(path));
		DataWriter writer = new XlsWriter(FileOutputStreamUtil.create(file));
		writer.write(dataSet);
	}

	public void writeDb(DataSet dataSet) {
		DataWriter writer = new SqlWriter(getDataSource());
		writer.write(dataSet);
	}

	public DataTable readDbByTable(String table) {
		return readDbByTable(table, null);
	}

	public DataTable readDbByTable(String table, String condition) {
		SqlTableReader reader = new SqlTableReader(getDataSource());
		reader.setTable(table, condition);
		return reader.read();
	}

	public DataTable readDbBySql(String sql, String tableName) {
		SqlTableReader reader = new SqlTableReader(getDataSource());
		reader.setSql(sql, tableName);
		return reader.read();
	}

	public void readXlsWriteDb(String path) {
		writeDb(readXls(path));
	}

	public void readXlsReplaceDb(String path) {
		DataSet dataSet = readXls(path);
		deleteDb(dataSet);
		writeDb(dataSet);
	}

	public void readXlsAllReplaceDb(String path) {
		DataSet dataSet = readXls(path);
		for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
			deleteTable(dataSet.getTable(i).getTableName());
		}
		writeDb(dataSet);
	}

	public DataSet reload(DataSet dataSet) {
		return new SqlReloadReader(getDataSource(), dataSet).read();
	}

	public DataTable reload(DataTable table) {
		return new SqlReloadTableReader(getDataSource(), table).read();
	}

	public void deleteDb(DataSet dataSet) {
		SqlDeleteTableWriter writer = new SqlDeleteTableWriter(getDataSource());
		for (int i = dataSet.getTableSize() - 1; i >= 0; --i) {
			writer.write(dataSet.getTable(i));
		}
	}

	public void deleteTable(String tableName) {
		UpdateHandler handler = new BasicUpdateHandler(getDataSource(),
				"DELETE FROM " + tableName);
		handler.execute(null);
	}

	public void assertEquals(DataSet expected, DataSet actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataSet expected, DataSet actual) {
		message = message == null ? "" : message;
		assertEquals(message + ":TableSize", expected.getTableSize(), actual
				.getTableSize());
		for (int i = 0; i < expected.getTableSize(); ++i) {
			assertEquals(message, expected.getTable(i), actual.getTable(i));
		}
	}

	public void assertEquals(DataTable expected, DataTable actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataTable expected,
			DataTable actual) {

		message = message == null ? "" : message;
		message = message + ":TableName=" + expected.getTableName();
		assertEquals(message + ":RowSize", expected.getRowSize(), actual
				.getRowSize());
		for (int i = 0; i < expected.getRowSize(); ++i) {
			DataRow expectedRow = expected.getRow(i);
			DataRow actualRow = actual.getRow(i);
			List errorMessages = new ArrayList();
			for (int j = 0; j < expected.getColumnSize(); ++j) {
				try {
					String columnName = expected.getColumnName(j);
					Object expectedValue = expectedRow.getValue(columnName);
					ColumnType ct = ColumnTypes.getColumnType(expectedValue);
					Object actualValue = actualRow.getValue(columnName);
					if (!ct.equals(expectedValue, actualValue)) {
						assertEquals(message + ":Row=" + i + ":columnName="
								+ columnName, expectedValue, actualValue);
					}
				} catch (AssertionFailedError e) {
					errorMessages.add(e.getMessage());
				}
			}
			if (!errorMessages.isEmpty()) {
				fail(message + errorMessages);
			}
		}
	}

	public void assertEquals(DataSet expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	public void assertEquals(String message, DataSet expected, Object actual) {
		if (expected == null || actual == null) {
			Assert.assertEquals(message, expected, actual);
			return;
		}
		if (actual instanceof List) {
			List actualList = (List) actual;
			Assert.assertFalse(actualList.isEmpty());
			Object actualItem = actualList.get(0);
			if (actualItem instanceof Map) {
				assertMapListEquals(message, expected, actualList);
			} else {
				assertBeanListEquals(message, expected, actualList);
			}
		} else if (actual instanceof Object[]) {
			assertEquals(message, expected, Arrays.asList((Object[]) actual));
		} else {
			if (actual instanceof Map) {
				assertMapEquals(message, expected, (Map) actual);
			} else {
				assertBeanEquals(message, expected, actual);
			}
		}
	}

	protected void assertMapEquals(String message, DataSet expected, Map map) {

		MapReader reader = new MapReader(map);
		assertEquals(message, expected, reader.read());
	}

	protected void assertMapListEquals(String message, DataSet expected,
			List list) {

		MapListReader reader = new MapListReader(list);
		assertEquals(message, expected, reader.read());
	}

	protected void assertBeanEquals(String message, DataSet expected,
			Object bean) {

		BeanReader reader = new BeanReader(bean);
		assertEquals(message, expected, reader.read());
	}

	protected void assertBeanListEquals(String message, DataSet expected,
			List list) {

		BeanListReader reader = new BeanListReader(list);
		assertEquals(message, expected, reader.read());
	}

	/**
	 * @see junit.framework.TestCase#runBare()
	 */
	public void runBare() throws Throwable {
		setUpContainer();
		setUp();
		try {
			setUpForEachTestMethod();
			try {
				container_.init();
				try {
					setupDataSource();
					try {
						setUpAfterContainerInit();
						bindFields();
						setUpAfterBindFields();
						try {
							runTestTx();
						} finally {
							tearDownBeforeUnbindFields();
							unbindFields();
						}
						tearDownBeforeContainerDestroy();
					} finally {
						tearDownDataSource();
					}
				} finally {
					tearDownContainer();
				}
			} finally {
				tearDownForEachTestMethod();
			}

		} finally {
			for (int i = 0; i < 5; ++i) {
				System.runFinalization();
				System.gc();
			}
			tearDown();
		}
	}

	protected void setUpContainer() throws Throwable {
		container_ = new S2ContainerImpl();
		servletContext_ = new MockServletContextImpl("s2jsf-example");
		request_ = servletContext_.createRequest("/hello.html");
		response_ = new MockHttpServletResponseImpl(request_);
		servletConfig_ = new MockServletConfigImpl();
		servletConfig_.setServletContext(servletContext_);
		servlet_ = new MockServlet();
		servlet_.init(servletConfig_);
		container_.setServletContext(servletContext_);
		container_.setRequest(request_);
		container_.setResponse(response_);
		SingletonS2ContainerFactory.setContainer(container_);
	}

	protected void tearDownContainer() throws Throwable {
		container_.destroy();
		SingletonS2ContainerFactory.setContainer(null);
		container_ = null;
		servletContext_ = null;
		request_ = null;
		response_ = null;
		servletConfig_ = null;
		servlet_ = null;
	}

	protected void setUpAfterContainerInit() throws Throwable {
	}

	protected void setUpAfterBindFields() throws Throwable {
	}

	protected void tearDownBeforeUnbindFields() throws Throwable {
	}

	protected void setUpForEachTestMethod() throws Throwable {
		String targetName = getTargetName();
		if (targetName.length() > 0) {
			invoke("setUp" + targetName);
		}
	}

	protected void tearDownBeforeContainerDestroy() throws Throwable {
	}

	protected void tearDownForEachTestMethod() throws Throwable {
		String targetName = getTargetName();
		if (targetName.length() > 0) {
			invoke("tearDown" + getTargetName());
		}
	}

	protected Servlet getServlet() {
		return servlet_;
	}

	protected void setServlet(Servlet servlet) {
		servlet_ = servlet;
	}

	protected MockServletConfig getServletConfig() {
		return servletConfig_;
	}

	protected void setServletConfig(MockServletConfig servletConfig) {
		servletConfig_ = servletConfig;
	}

	protected MockServletContext getServletContext() {
		return servletContext_;
	}

	protected void setServletContext(MockServletContext servletContext) {
		servletContext_ = servletContext;
	}

	protected MockHttpServletRequest getRequest() {
		return request_;
	}

	protected void setRequest(MockHttpServletRequest request) {
		request_ = request;
	}

	protected MockHttpServletResponse getResponse() {
		return response_;
	}

	protected void setResponse(MockHttpServletResponse response) {
		response_ = response;
	}

	private String getTargetName() {
		return getName().substring(4);
	}

	private void invoke(String methodName) throws Throwable {
		try {
			Method method = ClassUtil.getMethod(getClass(), methodName, null);
			MethodUtil.invoke(method, this, null);
		} catch (NoSuchMethodRuntimeException ignore) {
		}
	}

	private void bindFields() throws Throwable {
		bindedFields_ = new ArrayList();
		for (Class clazz = getClass(); clazz != S2TestCase.class
				&& clazz != null; clazz = clazz.getSuperclass()) {

			Field[] fields = clazz.getDeclaredFields();
			for (int i = 0; i < fields.length; ++i) {
				bindField(fields[i]);
			}
		}
	}

	private void bindField(Field field) {
		if (isAutoBindable(field)) {
			field.setAccessible(true);
			if (FieldUtil.get(field, this) != null) {
				return;
			}
			String name = normalizeName(field.getName());
			Object component = null;
			if (getContainer().hasComponentDef(name)) {
				Class componentClass = getComponentDef(name)
						.getComponentClass();
				if (componentClass == null) {
					component = getComponent(name);
					if (component != null) {
						componentClass = component.getClass();
					}
				}
				if (componentClass != null
						&& field.getType().isAssignableFrom(componentClass)) {
					if (component == null) {
						component = getComponent(name);
					}
				} else {
					component = null;
				}
			}
			if (component == null
					&& getContainer().hasComponentDef(field.getType())) {
				component = getComponent(field.getType());
			}
			if (component != null) {
				FieldUtil.set(field, this, component);
				bindedFields_.add(field);
			}
		}
	}

	private String normalizeName(String name) {
		return StringUtil.replace(name, "_", "");
	}

	private boolean isAutoBindable(Field field) {
		int modifiers = field.getModifiers();
		return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
				&& !field.getType().isPrimitive();
	}

	private void unbindFields() {
		for (int i = 0; i < bindedFields_.size(); ++i) {
			Field field = (Field) bindedFields_.get(i);
			try {
				field.set(this, null);
			} catch (IllegalArgumentException e) {
				System.err.println(e);
			} catch (IllegalAccessException e) {
				System.err.println(e);
			}
		}
	}

	private void runTestTx() throws Throwable {
		TransactionManager tm = null;
		if (needTransaction()) {
			try {
				tm = (TransactionManager) getComponent(TransactionManager.class);
				tm.begin();
			} catch (Throwable t) {
				System.err.println(t);
			}
		}
		try {
			runTest();
		} finally {
			if (tm != null) {
				tm.rollback();
			}
		}
	}

	private boolean needTransaction() {
		return getName().endsWith("Tx");
	}

	private void setupDataSource() {
		try {
			if (container_.hasComponentDef(DATASOURCE_NAME)) {
				dataSource_ = (DataSource) container_
						.getComponent(DATASOURCE_NAME);
			} else if (container_.hasComponentDef(DataSource.class)) {
				dataSource_ = (DataSource) container_
						.getComponent(DataSource.class);
			}
		} catch (Throwable t) {
			System.err.println(t);
		}
	}

	private void tearDownDataSource() {
		dbMetaData_ = null;
		if (connection_ != null) {
			ConnectionUtil.close(connection_);
			connection_ = null;
		}
		dataSource_ = null;
	}
}