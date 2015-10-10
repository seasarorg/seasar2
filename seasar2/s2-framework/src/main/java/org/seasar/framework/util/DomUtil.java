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
package org.seasar.framework.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.seasar.framework.exception.IORuntimeException;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * DOM用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class DomUtil {

    /**
     * インスタンスを構築します。
     */
    protected DomUtil() {
    }

    /**
     * XMLの内容を {@link InputStream}として取得します。
     * 
     * @param contents
     * @return {@link InputStream}
     */
    public static InputStream getContentsAsStream(String contents) {
        return getContentsAsStream(contents, null);
    }

    /**
     * XMLの内容を {@link InputStream}として取得します。
     * 
     * @param contents
     * @param encoding
     * @return {@link InputStream}
     */
    public static InputStream getContentsAsStream(String contents,
            String encoding) {

        if (encoding == null) {
            return new ByteArrayInputStream(contents.getBytes());
        }
        try {
            return new ByteArrayInputStream(contents.getBytes(encoding));
        } catch (UnsupportedEncodingException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * 属性の値をエンコードします。
     * 
     * @param s
     * @return エンコードされた値
     */
    public static String encodeAttrQuot(final String s) {
        if (s == null) {
            return null;
        }
        char[] content = s.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            case '"':
                buf.append("&quot;");
                break;
            default:
                buf.append(content[i]);
            }
        }
        return buf.toString();
    }

    /**
     * テキストをエンコードします。
     * 
     * @param s
     * @return エンコードされた値
     */
    public static String encodeText(final String s) {
        if (s == null) {
            return null;
        }
        char[] content = s.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < content.length; i++) {
            switch (content[i]) {
            case '<':
                buf.append("&lt;");
                break;
            case '>':
                buf.append("&gt;");
                break;
            case '&':
                buf.append("&amp;");
                break;
            default:
                buf.append(content[i]);
            }
        }
        return buf.toString();
    }

    /**
     * {@link Document}を文字列に変換します。
     * 
     * @param document
     * @return 変換された文字列
     */
    public static String toString(Document document) {
        StringBuffer buf = new StringBuffer();
        appendElement(document.getDocumentElement(), buf);
        return buf.toString();
    }

    /**
     * {@link Element}を文字列に変換します。
     * 
     * @param element
     * @return 変換された文字列
     */
    public static String toString(Element element) {
        StringBuffer buf = new StringBuffer();
        appendElement(element, buf);
        return buf.toString();
    }

    /**
     * {@link Element}の文字列表現を追加します。
     * 
     * @param element
     * @param buf
     */
    public static void appendElement(Element element, StringBuffer buf) {
        String tag = element.getTagName();
        buf.append('<');
        buf.append(tag);
        appendAttrs(element.getAttributes(), buf);
        buf.append('>');
        appendChildren(element.getChildNodes(), buf);
        buf.append("</");
        buf.append(tag);
        buf.append('>');
    }

    /**
     * {@link NodeList}の文字列表現を追加します。
     * 
     * @param children
     * @param buf
     */
    public static void appendChildren(NodeList children, StringBuffer buf) {
        int length = children.getLength();
        for (int i = 0; i < length; ++i) {
            appendNode(children.item(i), buf);
        }
    }

    /**
     * {@link NamedNodeMap}の文字列表現を追加します。
     * 
     * @param attrs
     * @param buf
     */
    public static void appendAttrs(NamedNodeMap attrs, StringBuffer buf) {
        int length = attrs.getLength();
        for (int i = 0; i < length; ++i) {
            Attr attr = (Attr) attrs.item(i);
            buf.append(' ');
            appendAttr(attr, buf);
        }
    }

    /**
     * {@link Attr}の文字列表現を追加します。
     * 
     * @param attr
     * @param buf
     */
    public static void appendAttr(Attr attr, StringBuffer buf) {
        buf.append(attr.getName());
        buf.append("=\"");
        buf.append(encodeAttrQuot(attr.getValue()));
        buf.append('\"');
    }

    /**
     * {@link Text}の文字列表現を追加します。
     * 
     * @param text
     * @param buf
     */
    public static void appendText(Text text, StringBuffer buf) {
        buf.append(encodeText(text.getData()));
    }

    /**
     * {@link CDATASection}の文字列表現を追加します。
     * 
     * @param cdataSection
     * @param buf
     */
    public static void appendCDATASection(CDATASection cdataSection,
            StringBuffer buf) {
        buf.append("<![CDATA[");
        buf.append(cdataSection.getData());
        buf.append("]]>");
    }

    /**
     * {@link EntityReference}を追加します。
     * 
     * @param entityReference
     * @param buf
     */
    public static void appendEntityReference(EntityReference entityReference,
            StringBuffer buf) {
        buf.append('&');
        buf.append(entityReference.getNodeName());
        buf.append(';');
    }

    /**
     * {@link Node}の文字列表現を追加します。
     * 
     * @param node
     * @param buf
     */
    public static void appendNode(Node node, StringBuffer buf) {
        switch (node.getNodeType()) {
        case Node.ELEMENT_NODE:
            appendElement((Element) node, buf);
            break;
        case Node.TEXT_NODE:
            appendText((Text) node, buf);
            break;
        case Node.CDATA_SECTION_NODE:
            appendCDATASection((CDATASection) node, buf);
            break;
        case Node.ENTITY_REFERENCE_NODE:
            appendEntityReference((EntityReference) node, buf);
            break;
        }
    }
}