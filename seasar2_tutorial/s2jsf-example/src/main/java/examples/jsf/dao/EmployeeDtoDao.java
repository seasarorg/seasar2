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
package examples.jsf.dao;

import java.util.List;

import examples.jsf.dto.EmployeeDto;
import examples.jsf.dto.EmployeeSearchDto;

public interface EmployeeDtoDao {

	public Class BEAN = EmployeeDto.class;

	public String searchEmployeeDtoList_ARGS = "dto";

	public List searchEmployeeDtoList(EmployeeSearchDto dto);
	
	public String getSearchCount_ARGS = "dto";

	public int getSearchCount(EmployeeSearchDto dto);
	
	public String getEmployeeDto_ARGS = "empno";
	
	public EmployeeDto getEmployeeDto(Integer empno);
	
	public void insert(EmployeeDto dto);
	
	public void update(EmployeeDto dto);
	
	public void delete(EmployeeDto dto);
}
