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
package org.seasar.framework.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;


public final class LikeUtil {

	private static Map patterns = Collections.synchronizedMap(new HashMap());

	private LikeUtil() {
	}

	public static final boolean match(String patternStr, String value) {
		if (StringUtil.isEmpty(patternStr)) {
			return false;
		}
		Pattern pattern = (Pattern) patterns.get(patternStr);
		if (pattern == null) {
			String regexp = StringUtil.replace(patternStr, "_", ".");
			regexp = StringUtil.replace(regexp, "%", ".*");
			pattern = Pattern.compile(regexp);
			patterns.put(patternStr, pattern);
		}
		return pattern.matcher(value).matches();
	}
}