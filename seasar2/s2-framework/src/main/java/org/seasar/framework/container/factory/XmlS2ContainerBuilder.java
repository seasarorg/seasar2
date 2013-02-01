/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.SAXParserFactoryUtil;
import org.seasar.framework.xml.SaxHandler;
import org.seasar.framework.xml.SaxHandlerParser;
import org.seasar.framework.xml.TagHandlerContext;

/**
 * diconファイルから{@link org.seasar.framework.container.S2Container S2コンテナ}を構築します。
 * <p>
 * このクラスに対し、変更を加えることによって、S2コンテナの構築をカスタマイズすることが出来ます。 例えば、新規の{@link org.seasar.framework.xml.TagHandler}を追加した{@link org.seasar.framework.container.factory.S2ContainerTagHandlerRule}を設定することによって、
 * 設定ファイルをカスタマイズすることが出来ます。
 * </p>
 * 
 * @author higa
 * @author yatsu
 */
public class XmlS2ContainerBuilder extends AbstractS2ContainerBuilder {

    /**
     * Seasar2.0以降でサポートされているDTDのパブリックIDです。
     */
    public static final String PUBLIC_ID = "-//SEASAR//DTD S2Container//EN";

    /**
     * Seasar2.1以降でサポートされているDTDのパブリックIDです。
     */
    public static final String PUBLIC_ID21 = "-//SEASAR2.1//DTD S2Container//EN";

    /**
     * Seasar2.3以降でサポートされているDTDのパブリックIDです。
     */
    public static final String PUBLIC_ID23 = "-//SEASAR//DTD S2Container 2.3//EN";

    /**
     * Seasar2.4以降でサポートされているDTDのパブリックIDです。
     */
    public static final String PUBLIC_ID24 = "-//SEASAR//DTD S2Container 2.4//EN";

    /**
     * diconファイルの検証に利用されるバージョン2.0のDTDのパスです。
     */
    public static final String DTD_PATH = "org/seasar/framework/container/factory/components.dtd";

    /**
     * diconファイルの検証に利用されるバージョン2.1のDTDのパスです。
     */
    public static final String DTD_PATH21 = "org/seasar/framework/container/factory/components21.dtd";

    /**
     * diconファイルの検証に利用されるバージョン2.3のDTDのパスです。
     */
    public static final String DTD_PATH23 = "org/seasar/framework/container/factory/components23.dtd";

    /**
     * diconファイルの検証に利用されるバージョン2.4のDTDのパスです。
     */
    public static final String DTD_PATH24 = "org/seasar/framework/container/factory/components24.dtd";

    /**
     * タグと<code>TagHandler</code>のマッピング情報です。
     */
    protected S2ContainerTagHandlerRule rule = new S2ContainerTagHandlerRule();

    /**
     * 公開DTDのIDとDTDのパスのマッピング情報です。
     */
    protected Map dtdMap = new HashMap();

    /**
     * <code>XmlS2ContainerBuilder</code>を構築します。
     * <p>
     * DTDマッピング情報を定義します。
     * </p>
     */
    public XmlS2ContainerBuilder() {
        dtdMap.put(PUBLIC_ID, DTD_PATH);
        dtdMap.put(PUBLIC_ID21, DTD_PATH21);
        dtdMap.put(PUBLIC_ID23, DTD_PATH23);
        dtdMap.put(PUBLIC_ID24, DTD_PATH24);
    }

    /**
     * タグと<code>TagHandler</code>のマッピング情報を返します。
     * 
     * @return タグと<code>TagHandler</code>のマッピング情報
     */
    public S2ContainerTagHandlerRule getRule() {
        return rule;
    }

    /**
     * タグと<code>TagHandler</code>のマッピング情報を設定します。
     * 
     * @param rule
     *            タグと<code>TagHandler</code>のマッピング情報
     */
    public void setRule(final S2ContainerTagHandlerRule rule) {
        this.rule = rule;
    }

    /**
     * diconの検証で使用するDTDマッピング情報を追加します。
     * 
     * @param publicId
     *            DTDのパブリックID
     * @param systemId
     *            DTDのパス
     */
    public void addDtd(final String publicId, final String systemId) {
        dtdMap.put(publicId, systemId);
    }

    /**
     * DTDマッピング情報を消去します。デフォルトのDTDを使用しない場合に呼び出します。
     */
    public void clearDtd() {
        dtdMap.clear();
    }

    public S2Container build(final String path) {
        return parse(null, path);
    }

    public S2Container include(final S2Container parent, final String path) {
        final S2Container child = parse(parent, path);
        parent.include(child);
        return child;
    }

    /**
     * diconファイルを解析します。
     * 
     * @param parent
     *            親となるS2コンテナ
     * @param path
     *            設定ファイルのパス
     * @return 構築したS2コンテナ
     */
    protected S2Container parse(final S2Container parent, final String path) {
        final SaxHandlerParser parser = createSaxHandlerParser(parent, path);
        final InputStream is = getInputStream(path);
        try {
            return (S2Container) parser.parse(is, path);
        } finally {
            InputStreamUtil.close(is);
        }
    }

    /**
     * {@link org.seasar.framework.xml.SaxHandlerParser}を生成します。
     * 
     * @param parent
     *            親となるS2コンテナ
     * @param path
     *            設定ファイルのパス
     * @return 生成された<code>SaxHandlerParser</code>
     */
    protected SaxHandlerParser createSaxHandlerParser(final S2Container parent,
            final String path) {
        final SAXParserFactory factory = SAXParserFactoryUtil.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        SAXParserFactoryUtil.setXIncludeAware(factory, true);

        final SAXParser saxParser = SAXParserFactoryUtil.newSAXParser(factory);

        final SaxHandler handler = new SaxHandler(rule);
        for (final Iterator it = dtdMap.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final String publicId = (String) entry.getKey();
            final String systemId = (String) entry.getValue();
            handler.registerDtdPath(publicId, systemId);
        }

        final TagHandlerContext ctx = handler.getTagHandlerContext();
        ctx.addParameter("parent", parent);
        ctx.addParameter("path", path);

        return new SaxHandlerParser(handler, saxParser);
    }
}
