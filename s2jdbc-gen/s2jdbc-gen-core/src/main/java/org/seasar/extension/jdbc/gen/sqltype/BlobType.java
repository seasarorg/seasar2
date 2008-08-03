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
package org.seasar.extension.jdbc.gen.sqltype;

import java.io.ByteArrayInputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.seasar.framework.util.Base64Util;

/**
 * @author taedium
 * 
 */
public class BlobType extends AbstractSqlType {

    protected static byte[] EMPTY_BYTES = new byte[] {};

    public BlobType() {
        this("blob");
    }

    public BlobType(String columnDefinition) {
        super(columnDefinition);
    }

    public void bindValue(PreparedStatement ps, int index, String value)
            throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.BLOB);
        }
        byte[] bytes = Base64Util.decode(value);
        ps
                .setBinaryStream(index, new ByteArrayInputStream(bytes),
                        bytes.length);
    }

    public String getValue(ResultSet resultSet, int index) throws SQLException {
        Blob blob = resultSet.getBlob(index);
        if (blob == null) {
            return null;
        }
        final long length = blob.length();
        if (length == 0) {
            return Base64Util.encode(EMPTY_BYTES);
        }
        if (length > Integer.MAX_VALUE) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return Base64Util.encode(blob.getBytes(1L, (int) length));
    }
}
