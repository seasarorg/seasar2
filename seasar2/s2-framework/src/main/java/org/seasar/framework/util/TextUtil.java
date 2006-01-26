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

import java.io.InputStream;
import java.io.Reader;

/**
 * @author higa
 *
 */
public final class TextUtil {

	private TextUtil() {
	}

	public static String readText(String path) {
		InputStream is = ResourceUtil.getResourceAsStream(path);
		Reader reader = InputStreamReaderUtil.create(is);
		return ReaderUtil.readText(reader);
	}

}
