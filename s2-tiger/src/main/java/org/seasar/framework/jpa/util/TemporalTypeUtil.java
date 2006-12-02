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
package org.seasar.framework.jpa.util;

import java.sql.Types;

import javax.persistence.TemporalType;

/**
 * @author koichik
 */
public class TemporalTypeUtil {

    public static TemporalType fromSqlTypeToTemporalType(final int sqlType) {
        switch (sqlType) {
        case Types.DATE:
            return TemporalType.DATE;
        case Types.TIME:
            return TemporalType.TIME;
        case Types.TIMESTAMP:
            return TemporalType.TIMESTAMP;
        }
        return null;
    }

    public static int fromTemporalTypeToSqlType(final TemporalType temporalType) {
        if (temporalType == TemporalType.DATE) {
            return Types.DATE;
        } else if (temporalType == TemporalType.TIME) {
            return Types.TIME;
        } else if (temporalType == TemporalType.TIMESTAMP) {
            return Types.TIMESTAMP;
        }
        return Types.OTHER;
    }

}
