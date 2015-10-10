/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.it.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * @author taedium
 * 
 */
@Entity
public class LargeObject {

    /** */
    @Id
    public Integer id;

    /** */
    public String name;

    /** */
    @Lob
    public String largeName;

    /** */
    public byte[] bytes;

    /** */
    @Lob
    public byte[] largeBytes;

    /** */
    public MyDto dto;

    /** */
    @Lob
    public MyDto largeDto;

    /**
     * 
     * @author taedium
     * 
     */
    public static class MyDto implements Serializable {

        private static final long serialVersionUID = 1L;

        private String value;

        /**
         * 
         * @param value
         */
        public MyDto(String value) {
            if (value == null) {
                throw new NullPointerException("value");
            }
            this.value = value;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof MyDto))
                return false;
            MyDto castOther = (MyDto) other;
            return this.value.equals(castOther.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

    }
}
