/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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

import java.util.Collection;
import java.util.Iterator;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 循環インクルードが発見された時にスローされます。
 * <p>
 * 例えば、 以下のような場合に循環インクルードとなります。 （この例では、 aaa.diconが循環インクルードされています）
 * </p>
 * 
 * <pre>
 * aaa.dicon --include--&gt; bbb.dicon --include--&gt; ccc.dicon --include--&gt; aaa.dicon
 * </pre>
 * 
 * @author koichik
 * @author jundu
 */
public class CircularIncludeRuntimeException extends SRuntimeException {
    private static final long serialVersionUID = -8674493688526055877L;

    /**
     * 循環インクルードされた設定ファイルのパス
     */
    protected String path;

    /**
     * 循環インクルードしているパスまでの経路を表すコレクション
     */
    protected Collection paths;

    /**
     * <code>CircularIncludeRuntimeException</code>を構築します。
     * 
     * @param path
     *            循環インクルードされた設定ファイルのパス
     * @param paths
     *            循環インクルードしているパスまでの経路を表すコレクション
     */
    public CircularIncludeRuntimeException(final String path,
            final Collection paths) {
        super("ESSR0076", new Object[] { path, toString(path, paths) });
        this.path = path;
        this.paths = paths;
    }

    /**
     * 循環インクルードされた設定ファイルのパスを返します。
     * 
     * @return 設定ファイルのパス
     */
    public String getPath() {
        return path;
    }

    /**
     * 循環インクルードしているパスまでの経路を表すコレクションを返します。
     * 
     * @return 循環インクルードしているパスまでの経路を表すコレクション
     */
    public Collection getPaths() {
        return paths;
    }

    /**
     * 循環インクルードが発生した設定ファイルまでのインクルード経路を表す文字列を返します。
     * 
     * @param path
     *            循環インクルードが発生した設定ファイルのパス
     * @param paths
     *            循環インクルードしているパスまでの経路を表すコレクション
     * @return インクルード経路を表す文字列
     */
    protected static String toString(final String path, final Collection paths) {
        final StringBuffer buf = new StringBuffer(200);
        for (final Iterator it = paths.iterator(); it.hasNext();) {
            buf.append("\"").append(it.next()).append("\" includes ");
        }
        buf.append("\"").append(path).append("\"");
        return new String(buf);
    }
}
