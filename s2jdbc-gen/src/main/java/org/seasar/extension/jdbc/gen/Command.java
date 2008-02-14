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

import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
public interface Command {

    static String DEFAULT_GAP_CLASS_NAME_SUFFIX = "Abstract";

    static String DEFAULT_ROOT_PACKAGE_NAME = "";

    static String DEFAULT_ENTITY_PACKAGE_NAME = "entity";

    static String DEFAULT_JDBC_MANAGER_NAME = "jdbcManager";

    static File DEFAULT_TEMPLATE_FILE = ResourceUtil
            .getResourceAsFile("templates");

    static File DEFAULT_DEST_DIR = new File("src/main/java");

    static String DEFAULT_ENCODING = "UTF-8";

    static String DEFAULT_VERSION_COLUMN = "versionColumn";

    static String DEFAULT_ENTITY_TEMPLATE = "entityCode.ftl";

    static String DEFAULT_ENTITU_GAP_TEMPLATE = "entityGapCode.ftl";

    static String DEFAULT_DICON = "s2jdbc.dicon";

    static String DEFAULT_SCHEMA = null;

}
