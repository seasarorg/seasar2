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

import examples.jsf.action.ForEachResultInitAction;
import examples.jsf.dto.ForEachDto;

public class ForEachResultInitActionImpl implements ForEachResultInitAction {

	private int index;

	private List forEachDtoList;
	
	private ForEachDto forEachDto;
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public void setForEachDtoList(List forEachDtoList) {
		this.forEachDtoList = forEachDtoList;
	}
	
	public ForEachDto getForEachDto() {
		return forEachDto;
	}

	public String initialize() {
		forEachDto = (ForEachDto) forEachDtoList.get(index);
		return null;
	}
	
}