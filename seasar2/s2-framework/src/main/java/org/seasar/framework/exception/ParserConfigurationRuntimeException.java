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

import javax.xml.parsers.ParserConfigurationException;

/**
 * {@link ParserConfigurationException}をラップする例外です。
 * 
 * @author higa
 * 
 */
public class ParserConfigurationRuntimeException extends
        SRuntimeException {

    private static final long serialVersionUID = -4610465906028959083L;

    /**
     * {@link ParserConfigurationRuntimeException}を作成します。
     * 
     * @param cause
     */
    public ParserConfigurationRuntimeException(
            ParserConfigurationException cause) {
        super("ESSR0053", new Object[] { cause }, cause);
    }
}
