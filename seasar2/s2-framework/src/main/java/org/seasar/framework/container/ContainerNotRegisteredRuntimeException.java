/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
 * diconファイルなどの設定情報に対応するS2コンテナが、 コンテナツリーに登録されていなかった場合にスローされます。
 * 
 * @author higa
 * @author belltree
 */
public class ContainerNotRegisteredRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 6752085937214047111L;

    private String path_;

    /**
     * 登録されていなかった設定情報のパスを指定して、
     * <code>ContainerNotRegisteredRuntimeException</code>を構築します。
     * 
     * @param path
     *            登録されていなかった設定情報のパス
     */
    public ContainerNotRegisteredRuntimeException(String path) {
        super("ESSR0075", new Object[] { path });
        path_ = path;
    }

    /**
     * コンテナツリーに登録されていなかった設定情報のパスを返します。
     * 
     * @return path 登録されていなかった設定情報のパス
     */
    public String getPath() {
        return path_;
    }
}