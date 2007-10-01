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
package org.seasar.extension.jdbc.util;

import org.seasar.extension.jdbc.util.ConditionUtil;

import junit.framework.TestCase;

/**
 * @author higa
 * 
 */
public class ConditionUtilTest extends TestCase {

	/**
	 * 
	 */
	public void testGetEqCondition() {
		assertEquals("_T1.AAA_ID = ?", ConditionUtil.getEqCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetNeCondition() {
		assertEquals("_T1.AAA_ID <> ?", ConditionUtil.getNeCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetLtCondition() {
		assertEquals("_T1.AAA_ID < ?", ConditionUtil.getLtCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetLeCondition() {
		assertEquals("_T1.AAA_ID <= ?", ConditionUtil.getLeCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetGtCondition() {
		assertEquals("_T1.AAA_ID > ?", ConditionUtil.getGtCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetGeCondition() {
		assertEquals("_T1.AAA_ID >= ?", ConditionUtil.getGeCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetInCondition() {
		assertEquals("_T1.AAA_ID in (?)", ConditionUtil.getInCondition("_T1",
				"AAA_ID", 1));
		assertEquals("_T1.AAA_ID in (?, ?)", ConditionUtil.getInCondition(
				"_T1", "AAA_ID", 2));
	}

	/**
	 * 
	 */
	public void testGetNotInCondition() {
		assertEquals("_T1.AAA_ID not in (?)", ConditionUtil.getNotInCondition(
				"_T1", "AAA_ID", 1));
		assertEquals("_T1.AAA_ID not in (?, ?)", ConditionUtil
				.getNotInCondition("_T1", "AAA_ID", 2));
	}

	/**
	 * 
	 */
	public void testGetLikeCondition() {
		assertEquals("_T1.AAA_ID like ?", ConditionUtil.getLikeCondition("_T1",
				"AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetIsNullCondition() {
		assertEquals("_T1.AAA_ID is null", ConditionUtil.getIsNullCondition(
				"_T1", "AAA_ID"));
	}

	/**
	 * 
	 */
	public void testGetIsNotNullCondition() {
		assertEquals("_T1.AAA_ID is not null", ConditionUtil
				.getIsNotNullCondition("_T1", "AAA_ID"));
	}
}
