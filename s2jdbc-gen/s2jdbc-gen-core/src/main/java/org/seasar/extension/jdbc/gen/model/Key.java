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

/**
 * @author taedium
 * 
 */
public class Key {

    protected Object[] values;

    protected int hashCode;

    public Key(Object[] values) {
        if (values == null) {
            throw new NullPointerException("values");
        }
        this.values = values;
        for (int i = 0; i < this.values.length; ++i) {
            hashCode += values[i].hashCode();
        }
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Key)) {
            return false;
        }
        Object[] otherValues = ((Key) o).values;
        if (values.length == 0) {
            return false;
        }
        if (values.length != otherValues.length) {
            return false;
        }
        for (int i = 0; i < values.length; ++i) {
            if (!values[i].equals(otherValues[i])) {
                return false;
            }
        }
        return true;
    }
}
