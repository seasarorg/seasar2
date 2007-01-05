/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.framework.container;

import org.seasar.framework.exception.SRuntimeException;

/**
 * 指定されたパスのファイル名に、 拡張子が付いていなかった場合にスローされます。
 * <p>
 * {@link org.seasar.framework.container.factory.S2ContainerFactory S2コンテナファクトリ}は、
 * S2コンテナを構築しようとした際に、 拡張子に応じて{@link org.seasar.framework.container.factory.S2ContainerBuilder S2コンテナビルダー}を切り替えます。
 * このため、 指定された設定ファイル(diconファイルなど)のファイル名に拡張子が付いていない場合には、 この例外が発生します。
 * </p>
 * 
 * @author higa
 * @author belltree
 */
public class ExtensionNotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 4105296013672747434L;

    private String path_;

    /**
     * パスを指定して<code>ExtensionNotFoundRuntimeException</code>を構築します。
     * 
     * @param path
     *            指定されたパス
     */
    public ExtensionNotFoundRuntimeException(String path) {
        super("ESSR0074", new Object[] { path });
        path_ = path;
    }

    /**
     * 指定されたパスを返します。
     * 
     * @return 指定されたパス
     */
    public String getPath() {
        return path_;
    }
}