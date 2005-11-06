package org.seasar.framework.util;

import javax.xml.parsers.DocumentBuilder;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DomUtilTest extends TestCase {

    public void testGetContentsAsStream() throws Exception {
        String contents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo/>";
        assertNotNull("1", DomUtil.getContentsAsStream(contents, "UTF-8"));
    }

    public void testToString() throws Exception {
        DocumentBuilder builder = DocumentBuilderFactoryUtil
                .newDocumentBuilder();
        Document doc = DocumentBuilderUtil.parse(builder, ResourceUtil
                .getResourceAsStream("org/seasar/framework/util/test1.xml"));
        Element root = doc.getDocumentElement();
        String contents = DomUtil.toString(root);
        System.out.println(contents);
    }
}