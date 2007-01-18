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
package org.seasar.extension.persistence.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author higa
 * 
 */
public class Employee {

	private Long employeeId;

	private String employeeName;

	private BigDecimal salary;

	private Long managerId;

	private Long departmentId;

	private Integer employeeVersion;

	private Timestamp employeeInserted;

	private Timestamp employeeUpdated;

	private Employee manager;

	private List<Employee> assistants;

	private Department department;
}