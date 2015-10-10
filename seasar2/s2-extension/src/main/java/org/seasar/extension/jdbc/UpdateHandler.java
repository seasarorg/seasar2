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
package org.seasar.extension.jdbc;

import org.seasar.framework.exception.SQLRuntimeException;

/**
 * 更新のためのインターフェースです。
 * 
 * @author higa
 * 
 */
public interface UpdateHandler {

    /**
     * 更新を行ないます。
     * 
     * @param args
     *            引数
     * @return 更新した行数
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    int execute(Object[] args) throws SQLRuntimeException;

    /**
     * 更新を行ないます。
     * 
     * @param args
     *            引数
     * @param argTypes
     *            引数の型
     * @return 更新した行数
     * @throws SQLRuntimeException
     *             SQL例外が発生した場合
     */
    int execute(Object[] args, Class[] argTypes) throws SQLRuntimeException;
}