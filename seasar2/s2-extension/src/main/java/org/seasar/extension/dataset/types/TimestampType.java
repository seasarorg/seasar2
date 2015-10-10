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
package org.seasar.extension.dataset.types;

import java.sql.Timestamp;

import org.seasar.extension.dataset.ColumnType;
import org.seasar.framework.util.TimestampConversionUtil;

/**
 * 日付用の {@link ColumnType}です。
 * 
 * @author higa
 * 
 */
public class TimestampType extends ObjectType {

    TimestampType() {
    }

    public Object convert(Object value, String formatPattern) {
        return TimestampConversionUtil.toTimestamp(value, formatPattern);
    }

    public Class getType() {
        return Timestamp.class;
    }
}