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

import examples.jsf.action.EmployeeListAction;
import examples.jsf.common.Constants;
import examples.jsf.dto.EmployeeDto;
import examples.jsf.dto.ProcessModeDto;
import examples.jsf.logic.EmployeeLogic;

public class EmployeeListActionImpl implements EmployeeListAction {
	
	private int processMode;

	private ProcessModeDto processModeDto;
	
	private Integer empno;
	
	private EmployeeLogic employeeLogic;
	
	private EmployeeDto employeeDto;
	
	public void setProcessMode(int processMode) {
		this.processMode = processMode;
	}
	
	public void setProcessModeDto(ProcessModeDto processModeDto) {
		this.processModeDto = processModeDto;
	}
	
	public void setEmpno(Integer empno) {
		this.empno = empno;
	}

	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	}

	public EmployeeDto getEmployeeDto() {
		return employeeDto;
	}
	
	public String goNext() {
		processModeDto.setProcessMode(processMode);
		switch (processModeDto.getProcessMode()) {
		case Constants.UPDATE_MODE :
			return "employeeEdit";
		default :
			employeeDto = employeeLogic.getEmployeeDto(empno);
			return "employeeConfirm";
		}
	}
	
}