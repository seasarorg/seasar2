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

import examples.jsf.action.EmployeeSearchAction;
import examples.jsf.common.Constants;
import examples.jsf.dto.EmployeeSearchDto;
import examples.jsf.dto.ProcessModeDto;
import examples.jsf.exception.BadCriteriaRuntimeException;
import examples.jsf.logic.EmployeeLogic;

public class EmployeeSearchActionImpl implements EmployeeSearchAction {

	private EmployeeLogic employeeLogic;
	
	private EmployeeSearchDto employeeSearchDto;
	
	private ProcessModeDto processModeDto;

	public void setEmployeeSearchDto(EmployeeSearchDto employeeSearchDto) {
		this.employeeSearchDto = employeeSearchDto;
	}
	
	public void setProcessModeDto(ProcessModeDto processModeDto) {
		this.processModeDto = processModeDto;
	}
	
	public void setEmployeeLogic(EmployeeLogic employeeLogic) {
		this.employeeLogic = employeeLogic;
	} 

	public String checkSearchCount() {
		if (employeeLogic.getSearchCount(employeeSearchDto) == 0) {
			throw new BadCriteriaRuntimeException();
		}
		return "employeeList";
	}
	
	public String goEditForCreate() {
		processModeDto.setProcessMode(Constants.CREATE_MODE);
		return "employeeEdit";
	}
}