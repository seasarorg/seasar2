/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.model;

import org.seasar.extension.jdbc.gen.desc.AssociationType;

/**
 * @author taedium
 * 
 */
public class InverseAssociationModel {

    /** 名前 */
    protected String name;

    protected String shortClassName;

    protected String mappedBy;

    protected AssociationType associationType;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the associationClassName.
     */
    public String getShortClassName() {
        return shortClassName;
    }

    /**
     * @param associationClassName
     *            The associationClassName to set.
     */
    public void setShortClassName(String associationClassName) {
        this.shortClassName = associationClassName;
    }

    /**
     * @return Returns the mappedBy.
     */
    public String getMappedBy() {
        return mappedBy;
    }

    /**
     * @param mappedBy
     *            The mappedBy to set.
     */
    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    /**
     * @return Returns the associationType.
     */
    public AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * @param associationType
     *            The associationType to set.
     */
    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

}
