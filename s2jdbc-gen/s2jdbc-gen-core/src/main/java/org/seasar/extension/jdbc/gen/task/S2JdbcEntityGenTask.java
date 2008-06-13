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
package org.seasar.extension.jdbc.gen.task;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.gen.command.S2JdbcEntityGenCommand;

/**
 * S2JDBC用のエンティティを生成するタスクです。
 * 
 * @author taedium
 */
public class S2JdbcEntityGenTask extends AbstractEntityGenTask {

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /**
     * インスタンスを構築します。
     */
    public S2JdbcEntityGenTask() {
        this(S2JdbcEntityGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public S2JdbcEntityGenTask(String commandClassName) {
        super(commandClassName);
    }

    /**
     * {@link JdbcManager}のコンポーネント名です。
     * 
     * @return {@link JdbcManager}のコンポーネント名
     */
    public String getJdbcManagerName() {
        return jdbcManagerName;
    }

    /**
     * {@link JdbcManager}のコンポーネント名を設定します。
     * 
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     */
    public void setJdbcManagerName(String jdbcManagerName) {
        this.jdbcManagerName = jdbcManagerName;
    }

}
