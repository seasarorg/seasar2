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
package org.seasar.framework.exception;

/**
 * 空の場合にスローされる例外です。
 * 
 * @author higa
 * 
 */
public class EmptyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 4625805280526951642L;

    private String targetName;

    /**
     * {@link EmptyRuntimeException}を作成します。
     * 
     * @param targetName
     */
    public EmptyRuntimeException(String targetName) {
        super("ESSR0007", new Object[] { targetName });
        this.targetName = targetName;
    }

    /**
     * ターゲット名を返します。
     * 
     * @return
     */
    public String getTargetName() {
        return targetName;
    }
}
