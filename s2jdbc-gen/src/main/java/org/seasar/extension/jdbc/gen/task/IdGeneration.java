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
package org.seasar.extension.jdbc.gen.task;

import org.apache.tools.ant.types.EnumeratedAttribute;

/**
 * エンティティの識別子の生成方法を示す列挙型です。
 * 
 * @author taedium
 */
public class IdGeneration extends EnumeratedAttribute {

    /** 自動 */
    public static String AUTO = "auto";

    /** IDENTITYカラム */
    public static String IDENTITY = "identity";

    /** シーケンス */
    public static String SEQUENCE = "sequence";

    /** テーブル */
    public static String TABLE = "table";

    /** 割り当て */
    public static String ASSIGNED = "assigned";

    @Override
    public String[] getValues() {
        return new String[] { AUTO, IDENTITY, SEQUENCE, TABLE, ASSIGNED };
    }

}
