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
package org.seasar.extension.jdbc.gen.desc;


/**
 * @author taedium
 * 
 */
public class InverseAssociationDesc {

    protected String name;

    protected EntityDesc referencingEntityDesc;

    protected AssociationType associationType;

    /** 関連の所有者側の名前 */
    protected String mappedBy;

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
     * @return Returns the relationshipEntityDesc.
     */
    public EntityDesc getReferencingEntityDesc() {
        return referencingEntityDesc;
    }

    /**
     * @param relationshipEntityDesc
     *            The relationshipEntityDesc to set.
     */
    public void setReferencingEntityDesc(EntityDesc relationshipEntityDesc) {
        this.referencingEntityDesc = relationshipEntityDesc;
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
}
