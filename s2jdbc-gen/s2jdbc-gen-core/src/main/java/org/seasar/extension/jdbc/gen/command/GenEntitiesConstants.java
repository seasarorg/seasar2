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
package org.seasar.extension.jdbc.gen.command;

import java.io.File;

import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
public class GenEntitiesConstants {

    public static String GAP_CLASS_NAME_SUFFIX = "Abstract";

    public static String ROOT_PACKAGE_NAME = "";

    public static String ENTITY_PACKAGE_NAME = "entity";

    public static String ENTITY_BASE_PACKAGE_NAME = "entity";

    public static String JDBC_MANAGER_NAME = "jdbcManager";

    public static File TEMPLATE_DIR = ResourceUtil
            .getResourceAsFile("templates");

    public static File DEST_DIR = new File("src/main/java");

    public static String TEMPLATE_ENCODING = "UTF-8";

    public static String JAVA_CODE_ENCODING = "UTF-8";

    public static String VERSION_COLUMN_NAME = "versionColumn";

    public static String ENTITY_TEMPLATE_NAME = "entityCode.ftl";

    public static String ENTITU_BASE_TEMPLATE_NAME = "entityBaseCode.ftl";

    public static String DICON_FILE = "s2jdbc.dicon";

    public static String SCHEMA_NAME = null;
}
