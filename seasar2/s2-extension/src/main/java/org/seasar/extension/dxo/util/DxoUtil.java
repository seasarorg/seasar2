/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.util;

import org.seasar.extension.dxo.DxoConstants;
import org.seasar.framework.util.OgnlUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author koichik
 * 
 */
public class DxoUtil {

    public static Object parseMap(final String expression) {
        if (StringUtil.isEmpty(expression)) {
            return null;
        }
        return OgnlUtil.parseExpression(DxoConstants.OGNL_MAP_PREFIX
                + expression + DxoConstants.OGNL_MAP_SUFFIX);
    }

}
