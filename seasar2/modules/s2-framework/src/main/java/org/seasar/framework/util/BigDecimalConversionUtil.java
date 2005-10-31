/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public final class BigDecimalConversionUtil {

	private BigDecimalConversionUtil() {
	}

	public static BigDecimal toBigDecimal(Object o) {
		return toBigDecimal(o, null);
	}
	
	public static BigDecimal toBigDecimal(Object o, String pattern) {
		if (o == null) {
			return null;
		} else if (o instanceof BigDecimal) {
			return (BigDecimal) o;
		} else if (o instanceof Integer) {
			return new BigDecimal(((Integer) o).intValue());
		} else if (o instanceof String) {
			return new BigDecimal((String) o);
		} else if (o instanceof Double) {
			return new BigDecimal(((Double) o).doubleValue());
		} else if (o instanceof Long) {
			return new BigDecimal(((Long) o).longValue());
		} else if (o instanceof Short) {
			return new BigDecimal(((Short) o).shortValue());
		} else if (o instanceof Float) {
			return new BigDecimal(((Float) o).floatValue());
		} else if (o instanceof java.util.Date) {
			if (pattern != null) {
				return new BigDecimal(new SimpleDateFormat(pattern).format(o));
			}
			return new BigDecimal(((java.util.Date) o).getTime());
		} else {
			return new BigDecimal(o.toString());
		}
	}	
}
