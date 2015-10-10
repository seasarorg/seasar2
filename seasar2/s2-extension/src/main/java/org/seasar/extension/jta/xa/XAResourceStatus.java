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
package org.seasar.extension.jta.xa;

import javax.transaction.xa.XAResource;

/**
 * {@link XAResource}のステータスを定義したインターフェースです。
 * 
 * @author higa
 * 
 */
public interface XAResourceStatus {

    /**
     * None
     */
    int RS_NONE = 0;

    /**
     * Active
     */
    int RS_ACTIVE = 1;

    /**
     * Suspended
     */
    int RS_SUSPENDED = 2;

    /**
     * Fail
     */
    int RS_FAIL = 3;

    /**
     * Success
     */
    int RS_SUCCESS = 4;

    /**
     * Prepared
     */
    int RS_PREPARED = 5;
}
