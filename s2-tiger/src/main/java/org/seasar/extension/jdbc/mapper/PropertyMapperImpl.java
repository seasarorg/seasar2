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

import java.lang.reflect.Field;

import org.seasar.extension.jdbc.PropertyMapper;
import org.seasar.framework.util.FieldUtil;

/**
 * {@link PropertyMapper}の実装クラスです。
 * 
 * @author higa
 * 
 */
public class PropertyMapperImpl implements PropertyMapper {

	/**
	 * フィールドです。
	 */
	protected Field field;

	/**
	 * プロパティのインデックスです。
	 */
	protected int propertyIndex;

	/**
	 * {@link PropertyMapperImpl}を作成します。
	 * 
	 * @param field
	 *            フィールド
	 * @param propertyIndex
	 *            プロパティインデックス
	 */
	public PropertyMapperImpl(Field field, int propertyIndex) {
		field.setAccessible(true);
		this.field = field;
		this.propertyIndex = propertyIndex;
	}

	public void map(Object entity, Object[] values) {
		setFieldValue(entity, values[propertyIndex]);
	}

	/**
	 * フィールドの値を設定します。
	 * 
	 * @param entity
	 *            エンティティ
	 * @param value
	 *            プロパティの値
	 */
	protected void setFieldValue(Object entity, Object value) {
		if (value == null) {
			return;
		}
		FieldUtil.set(field, entity, value);
	}

	/**
	 * フィールドを返します。
	 * 
	 * @return フィールド
	 */
	public Field getField() {
		return field;
	}

	/**
	 * プロパティインデックスを返します。
	 * 
	 * @return プロパティインデックス
	 */
	public int getPropertyIndex() {
		return propertyIndex;
	}
}
