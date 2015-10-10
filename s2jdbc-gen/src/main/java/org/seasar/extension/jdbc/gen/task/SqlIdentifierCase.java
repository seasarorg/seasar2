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
 * SQLの識別子の大文字小文字を変換するかどうかを示す列挙型です。
 * 
 * @author taedium
 */
public class SqlIdentifierCase extends EnumeratedAttribute {

    /** 大文字 */
    public static String UPPERCASE = "uppercase";

    /** 小文字 */
    public static String LOWERCASE = "lowercase";

    /** 元のまま */
    public static String ORIGINALCASE = "originalcase";

    @Override
    public String[] getValues() {
        return new String[] { UPPERCASE, LOWERCASE, ORIGINALCASE };
    }

}
