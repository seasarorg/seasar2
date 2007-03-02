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
package examples.jsf.action.impl;

import java.util.List;

import examples.jsf.action.EmployeeEditInitAction;
import examples.jsf.common.Constants;
import examples.jsf.dto.EmployeeDto;
import examples.jsf.dto.ProcessModeDto;
import examples.jsf.logic.EmployeeLogic;

public class EmployeeEditInitActionImpl implements EmployeeEditInitAction {

	private EmployeeLogic employeeLogic;

	private Integer empno;
	
	private ProcessModeDto processModeDto;
	
	private EmployeeDto employeeDto;
	
	private List departmentDtoList;

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}
	
	public void setEmpno(Integer empno) {
		this.empno = empno;
	}
	
	public void setProcessModeDto(ProcessModeDto processModeDto) {
		this.processModeDto = processModeDto;
	}
	
	public EmployeeDto getEmployeeDto() {
		return employeeDto;
	}
	
	public List getDepartmentDtoList() {
		return departmentDtoList;
	}

	public String initialize() {
		departmentDtoList = employeeLogic.getAllDepartments();
		if (processModeDto.getProcessMode() == Constants.UPDATE_MODE) {
			employeeDto = employeeLogic.getEmployeeDto(empno);
		}
		return null;
	}
	
}