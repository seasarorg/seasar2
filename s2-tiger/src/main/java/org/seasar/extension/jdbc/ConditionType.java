/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc;

/**
 * where句の条件タイプです。
 * 
 * @author higa
 * 
 */
public enum ConditionType {

	/**
	 * =です。
	 */
	EQ,
	/**
	 * &lt;&gt;です。
	 */
	NE,
	/**
	 * &ltです。
	 */
	LT,
	/**
	 * &lt=です。
	 */
	LE,
	/**
	 * &gtです。
	 */
	GT,
	/**
	 * &gt=です。
	 */
	GE,
	/**
	 * inです。
	 */
	IN,
	/**
	 * not inです。
	 */
	NOT_IN,
	/**
	 * likeです。
	 */
	LIKE,
	/**
	 * like '?%'です。
	 */
	STARTS,
	/**
	 * like '%?'です。
	 */
	ENDS,
	/**
	 * like '%?%'です。
	 */
	CONTAINS,
	/**
	 * is nullです。
	 */
	IS_NULL,
	/**
	 * is not nullです。
	 */
	IS_NOT_NULL
}
