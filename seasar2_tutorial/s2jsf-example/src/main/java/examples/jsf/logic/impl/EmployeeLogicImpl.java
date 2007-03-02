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
package examples.jsf.logic.impl;

import java.util.List;

import examples.jsf.dao.DepartmentDtoDao;
import examples.jsf.dao.EmployeeDtoDao;
import examples.jsf.dto.EmployeeDto;
import examples.jsf.dto.EmployeeSearchDto;
import examples.jsf.logic.EmployeeLogic;

public class EmployeeLogicImpl implements EmployeeLogic {

	private EmployeeDtoDao employeeDtoDao;
	
	private DepartmentDtoDao departmentDtoDao;

	public void setEmployeeDtoDao(EmployeeDtoDao employeeDtoDao) {
		this.employeeDtoDao = employeeDtoDao;
	}
	
	public void setDepartmentDtoDao(DepartmentDtoDao departmentDtoDao) {
		this.departmentDtoDao = departmentDtoDao;
	}
	
	public int getSearchCount(EmployeeSearchDto dto) {
		return employeeDtoDao.getSearchCount(dto);
	}
	
	public List searchEmployeeDtoList(EmployeeSearchDto dto) {
		return employeeDtoDao.searchEmployeeDtoList(dto);
	}
	
	public EmployeeDto getEmployeeDto(Integer empno) {
		return employeeDtoDao.getEmployeeDto(empno);
	}
	
	public List getAllDepartments() {
		return departmentDtoDao.getAllDepartments();
	}
	
	public String getDname(Integer deptno) {
		return departmentDtoDao.getDname(deptno);
	}
	
	public void insert(EmployeeDto dto) {
		employeeDtoDao.insert(dto);
	}
	
	public void update(EmployeeDto dto) {
		employeeDtoDao.update(dto);
	}
	
	public void delete(EmployeeDto dto) {
		employeeDtoDao.delete(dto);
	}
	
	public boolean existEmployee(Integer empno) {
		return employeeDtoDao.getEmployeeDto(empno) != null;
	}
}
