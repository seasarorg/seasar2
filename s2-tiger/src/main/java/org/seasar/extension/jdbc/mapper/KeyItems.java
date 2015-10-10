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
package org.seasar.extension.jdbc.mapper;

/**
 * 複数のキー項目をあらわすクラスです。
 * 
 * @author higa
 * 
 */
public class KeyItems {

	private Object[] values;

	private int hashCode;

	/**
	 * {@link KeyItems}を作成します。
	 * 
	 * @param values
	 *            キーの値の配列
	 */
	public KeyItems(Object[] values) {
		this.values = values;
		for (int i = 0; i < values.length; ++i) {
			hashCode += values[i].hashCode();
		}
	}

	/**
	 * キーの値の配列を返します。
	 * 
	 * @return キーの値の配列
	 */
	public Object[] getValues() {
		return values;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof KeyItems)) {
			return false;
		}
		Object[] otherValues = ((KeyItems) o).values;
		if (values.length != otherValues.length) {
			return false;
		}
		for (int i = 0; i < values.length; ++i) {
			if (!values[i].equals(otherValues[i])) {
				return false;
			}
		}
		return true;
	}
}