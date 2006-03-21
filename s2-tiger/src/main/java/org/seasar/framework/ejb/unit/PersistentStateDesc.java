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
package org.seasar.framework.ejb.unit;


/**
 * @author taedium
 *
 */
public interface PersistentStateDesc {
    
    String getTableName();
    
    PersistentColumn getColumn();
 
    void setColumn(PersistentColumn column);
    
	String getStateName();
    
    String getPathName();

	Class<?> getPersistentStateType();

	boolean isCollection();
	
	Class<?> getCollectionType();

	Object getValue(Object target);

	void setValue(Object target, Object value);

	boolean isEmbedded();

    PersistentClassDesc getEmbeddedClassDesc();
    
    boolean isToOneRelationship();
    
    boolean isToManyRelationship();

    void setRelationshipClassDesc(PersistentClassDesc persistentClassDesc);
    
    PersistentClassDesc getRelationshipClassDesc();

	boolean isProperty();
    
    boolean isIdentifier();

    boolean hasReferencedColumn(String columnName);

    void setupForeignKeyColumns();

    void addForeignKeyColumn(PersistentColumn column);
    
    int getForeignKeyColumnSize();
    
    PersistentColumn getForeignKeyColumn(int index); 
}
