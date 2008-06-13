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

import org.seasar.extension.jdbc.gen.command.JpaEntityGenCommand;

/**
 * JPA用のエンティティを生成するタスクです。
 * 
 * @author taedium
 */
public class JpaEntityGenTask extends AbstractEntityGenTask {

    /** データソース名 */
    protected String dataSourceName;

    /** 方言名 */
    protected String dialectName;

    /**
     * インスタンスを構築します。
     */
    public JpaEntityGenTask() {
        this(JpaEntityGenCommand.class.getName());
    }

    /**
     * インスタンスを構築します。
     * 
     * @param commandClassName
     *            コマンドクラス名
     */
    public JpaEntityGenTask(String commandClassName) {
        super(commandClassName);
    }

    /**
     * データソース名を返します。
     * 
     * @return データソース名
     */
    public String getDataSourceName() {
        return dataSourceName;
    }

    /**
     * データソース名を設定します。
     * 
     * @param dataSourceName
     *            データソース名
     */
    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    /**
     * 方言名を返します。
     * 
     * @return 方言名
     */
    public String getDialectName() {
        return dialectName;
    }

    /**
     * 方言名を設定します。
     * 
     * @param dialectName
     *            方言名
     */
    public void setDialectName(String dialectName) {
        this.dialectName = dialectName;
    }

}
