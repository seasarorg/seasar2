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

import examples.jsf.action.EmployeeConfirmInitAction;
import examples.jsf.dto.EmployeeDto;
import examples.jsf.logic.EmployeeLogic;

public class EmployeeConfirmInitActionImpl implements EmployeeConfirmInitAction {

	private EmployeeLogic employeeLogic;
	
	private EmployeeDto employeeDto;

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}
	
	public void setEmployeeDto(EmployeeDto employeeDto) {
		this.employeeDto = employeeDto;
	}

	public String initialize() {
		if (employeeDto.getDeptno() != null) {
			String dname = employeeLogic.getDname(employeeDto.getDeptno());
			employeeDto.setDname(dname);
		}
		return null;
	}
	
}