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

import javax.persistence.TemporalType;

/**
 * @author taedium
 * 
 */
public class PropertyModel {

    protected String name;

    protected Class<?> propertyClass;

    protected boolean id;

    protected TemporalType temporalType;

    protected boolean version;

    protected boolean trnsient;

    protected boolean lob;

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
     * @return Returns the propertyClass.
     */
    public Class<?> getPropertyClass() {
        return propertyClass;
    }

    /**
     * @param propertyClass
     *            The propertyClass to set.
     */
    public void setPropertyClass(Class<?> propertyClass) {
        this.propertyClass = propertyClass;
    }

    /**
     * @return Returns the id.
     */
    public boolean isId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(boolean id) {
        this.id = id;
    }

    /**
     * @return Returns the temporalType.
     */
    public TemporalType getTemporalType() {
        return temporalType;
    }

    /**
     * @param temporalType
     *            The temporalType to set.
     */
    public void setTemporalType(TemporalType temporalType) {
        this.temporalType = temporalType;
    }

    /**
     * @return Returns the version.
     */
    public boolean isVersion() {
        return version;
    }

    /**
     * @param version
     *            The version to set.
     */
    public void setVersion(boolean version) {
        this.version = version;
    }

    /**
     * @return Returns the trnsient.
     */
    public boolean isTransient() {
        return trnsient;
    }

    /**
     * @param trnsient
     *            The trnsient to set.
     */
    public void setTrnsient(boolean trnsient) {
        this.trnsient = trnsient;
    }

    /**
     * @return Returns the lob.
     */
    public boolean isLob() {
        return lob;
    }

    /**
     * @param lob
     *            The lob to set.
     */
    public void setLob(boolean lob) {
        this.lob = lob;
    }

}
