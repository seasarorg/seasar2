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
package org.seasar.extension.jdbc.gen;

import java.io.File;
import java.sql.Statement;
import java.util.List;

import org.seasar.extension.jdbc.gen.exception.SqlFailedException;

/**
 * @author taedium
 * 
 */
public interface SqlExecutionContext {

    String getSql();

    void setSql(String sql);

    File getSqlFile();

    void setSqlFile(File file);

    Statement getStatement();

    List<SqlFailedException> getExceptionList();

    void addException(SqlFailedException exception);

    public void destroy();

}
