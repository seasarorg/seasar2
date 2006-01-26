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

import java.text.SimpleDateFormat;

public final class FloatConversionUtil {

	private FloatConversionUtil() {
	}

	public static Float toFloat(Object o) {
		return toFloat(o, null);
	}
	
	public static Float toFloat(Object o, String pattern) {
		if (o == null) {
			return null;
		} else if (o instanceof Float) {
			return (Float) o;
		} else if (o instanceof Number) {
			return new Float(((Number) o).floatValue());
		} else if (o instanceof String) {
			return toFloat((String) o);
		} else if (o instanceof java.util.Date) {
			if (pattern != null) {
				return new Float(new SimpleDateFormat(pattern).format(o));
			}
			return new Float(((java.util.Date) o).getTime());
		} else {
			return toFloat(o.toString());
		}
	}
	
	private static Float toFloat(String s) {
		return new Float(DecimalFormatUtil.normalize(s));
	}
	
	public static float toPrimitiveFloat(Object o) {
		return toPrimitiveFloat(o, null);
	}

	public static float toPrimitiveFloat(Object o, String pattern) {
		if (o == null) {
			return 0;
		} else if (o instanceof Number) {
			return ((Number) o).floatValue();
		} else if (o instanceof String) {
			return toPrimitiveFloat((String) o);
		} else if (o instanceof java.util.Date) {
			if (pattern != null) {
				return Float.parseFloat(new SimpleDateFormat(pattern).format(o));
			}
			return ((java.util.Date) o).getTime();
		} else {
			return toPrimitiveFloat(o.toString());
		}
	}
	
	private static float toPrimitiveFloat(String s) {
		return Float.parseFloat(DecimalFormatUtil.normalize(s));
	}
}
