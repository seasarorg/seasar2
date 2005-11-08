package org.seasar.extension.dataset.impl;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.seasar.extension.dataset.DataSet;
import org.seasar.extension.dataset.DataWriter;
import org.seasar.extension.dataset.impl.XlsReader;
import org.seasar.extension.dataset.impl.XlsWriter;
import org.seasar.framework.util.ResourceUtil;

public class XlsWriterTest extends TestCase {

	private static final String PATH =
		"org/seasar/extension/dataset/impl/XlsReaderImplTest.xls";
	private static final String PATH2 =
		"XlsWriterImplTest.xls";

	private DataSet dataSet_;
	private DataWriter writer_;

	public XlsWriterTest(String name) {
		super(name);
	}

	public void testWrite() throws Exception {
		writer_.write(dataSet_);
	}

	protected void setUp() throws Exception {
        File readFile = ResourceUtil.getFile(ResourceUtil.getResource(PATH));
		dataSet_ = new XlsReader(readFile).read();
        File writeFile = new File(readFile.getParentFile(), PATH2);
		writer_ = new XlsWriter(writeFile);
	}

	protected void tearDown() throws Exception {
	}

	public static Test suite() {
		return new TestSuite(XlsWriterTest.class);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.main(
			new String[] { XlsWriterTest.class.getName()});
	}
}