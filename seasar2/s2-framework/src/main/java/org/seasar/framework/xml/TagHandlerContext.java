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
package org.seasar.framework.xml;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Locator;

/**
 * XMLを処理するときのコンテキストを保持するクラスです。
 * 
 * @author higa
 * 
 */
public class TagHandlerContext {

    private static final Integer ONE = new Integer(1);

    private StringBuffer body = null;

    private StringBuffer characters = new StringBuffer();

    private Stack bodyStack = new Stack();

    private StringBuffer path = new StringBuffer();

    private StringBuffer detailPath = new StringBuffer();

    private String qName = "";

    private Stack qNameStack = new Stack();

    private Object result;

    private Stack stack = new Stack();

    private Map pathCounts = new HashMap();

    private Map parameters = new HashMap();

    private Locator locator = new Locator() {
        public int getColumnNumber() {
            return 0;
        }

        public int getLineNumber() {
            return 0;
        }

        public String getPublicId() {
            return null;
        }

        public String getSystemId() {
            return null;
        }
    };

    /**
     * コンテキストに情報を追加します。
     * 
     * @param o
     */
    public void push(Object o) {
        if (stack.empty()) {
            result = o;
        }
        stack.push(o);
    }

    /**
     * 結果を返します。
     * 
     * @return 結果
     */
    public Object getResult() {
        return result;
    }

    /**
     * コンテキストに積まれている情報の最も上のものを取り出します。 取り出した情報はコンテキストから削除されます。
     * 
     * @return 最も上の情報
     */
    public Object pop() {
        return stack.pop();
    }

    /**
     * コンテキストに積まれている情報の最も上のものを取り出します。 取り出した情報はコンテキストに残ったままです。
     * 
     * @return 最も上の情報
     */
    public Object peek() {
        return stack.peek();
    }

    /**
     * コンテキストに積まれている情報で上から指定されたインデックスのものを取り出します。 取り出した情報はコンテキストに残ったままです。
     * 
     * @param n
     * @return 上から指定されたインデックスの情報
     */
    public Object peek(final int n) {
        return stack.get(stack.size() - n - 1);
    }

    /**
     * コンテキストに積まれている情報で指定されたクラスのインスタンスを取り出します。 取り出した情報はコンテキストに残ったままです。
     * 
     * @param clazz
     * @return 指定されたクラスのインスタンス
     */
    public Object peek(final Class clazz) {
        for (int i = stack.size() - 1; i >= 0; --i) {
            Object o = stack.get(i);
            if (clazz.isInstance(o)) {
                return o;
            }
        }
        return null;
    }

    /**
     * 最初にコンテキストに積まれた情報を返します。 取り出した情報はコンテキストに残ったままです。
     * 
     * @return 最初にコンテキストに積まれた情報
     */
    public Object peekFirst() {
        return stack.get(0);
    }

    /**
     * コンテキストのスタックが空かどうかを返します。
     * 
     * @return コンテキストのスタックが空かどうか
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * パラメータを返します。
     * 
     * @param name
     * @return パラメータ
     */
    public Object getParameter(String name) {
        return parameters.get(name);
    }

    /**
     * パラメータを追加します。
     * 
     * @param name
     * @param parameter
     */
    public void addParameter(String name, Object parameter) {
        parameters.put(name, parameter);
    }

    /**
     * {@link Locator}を返します。
     * 
     * @return {@link Locator}
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * {@link Locator}を設定します。
     * 
     * @param locator
     */
    public void setLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * 要素(タグ)の処理を開始します。
     * 
     * @param qName
     */
    public void startElement(String qName) {
        bodyStack.push(body);
        body = new StringBuffer();
        characters = new StringBuffer();
        qNameStack.push(this.qName);
        this.qName = qName;
        path.append("/");
        path.append(qName);
        int pathCount = incrementPathCount();
        detailPath.append("/");
        detailPath.append(qName);
        detailPath.append("[");
        detailPath.append(pathCount);
        detailPath.append("]");
    }

    /**
     * SAXのParserから呼び出されたcharacters()を処理します。
     * 
     * @param buffer
     * @param start
     * @param length
     */
    public void characters(char[] buffer, int start, int length) {
        body.append(buffer, start, length);
        characters.append(buffer, start, length);
    }

    /**
     * {@link #characters(char[], int, int)}の処理結果を返します。
     * 
     * @return {@link #characters(char[], int, int)}の処理結果
     */
    public String getCharacters() {
        return characters.toString().trim();
    }

    /**
     * ボディを返します。
     * 
     * @return ボディ
     */
    public String getBody() {
        return body.toString().trim();
    }

    /**
     * charactersの最後が行の終わりかどうかを返します。
     * 
     * @return charactersの最後が行の終わりかどうか
     */
    public boolean isCharactersEol() {
        if (characters.length() == 0) {
            return false;
        }
        return characters.charAt(characters.length() - 1) == '\n';
    }

    /**
     * charactersをクリアします。
     */
    public void clearCharacters() {
        characters = new StringBuffer();
    }

    /**
     * 要素(タグ)の終了処理を行ないます。
     */
    public void endElement() {
        body = (StringBuffer) bodyStack.pop();
        remoteLastPath(path);
        remoteLastPath(detailPath);
        qName = (String) qNameStack.pop();
    }

    private static void remoteLastPath(StringBuffer path) {
        path.delete(path.lastIndexOf("/"), path.length());
    }

    /**
     * タグのパスを返します。
     * 
     * @return タグのパス
     */
    public String getPath() {
        return path.toString();
    }

    /**
     * 詳細(何番目に登場したのかも含む)なタグのパスを返します。
     * 
     * @return 詳細(何番目に登場したのかも含む)なタグのパス
     */
    public String getDetailPath() {
        return detailPath.toString();
    }

    /**
     * qNameを返します。
     * 
     * @return qName
     */
    public String getQName() {
        return qName;
    }

    private int incrementPathCount() {
        String path = getPath();
        Integer pathCount = (Integer) pathCounts.get(path);
        if (pathCount == null) {
            pathCount = ONE;
        } else {
            pathCount = new Integer(pathCount.intValue() + 1);
        }
        pathCounts.put(path, pathCount);
        return pathCount.intValue();
    }
}