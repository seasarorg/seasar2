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
package org.seasar.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.seasar.framework.exception.ClassNotFoundRuntimeException;
import org.seasar.framework.exception.IORuntimeException;

/**
 * オブジェクトをシリアライズするためのユーティリティです。
 * 
 * @author higa
 * 
 */
public class SerializeUtil {

    private static final int BYTE_ARRAY_SIZE = 8 * 1024;

    /**
     * インスタンスを構築します。
     */
    protected SerializeUtil() {
    }

    /**
     * オブジェクトをシリアライズできるかテストします。
     * 
     * @param o
     * @return
     * @throws IORuntimeException
     * @throws ClassNotFoundRuntimeException
     */
    public static Object serialize(final Object o) throws IORuntimeException,
            ClassNotFoundRuntimeException {

        byte[] binary = fromObjectToBinary(o);
        return fromBinaryToObject(binary);
    }

    /**
     * オブジェクトをbyteの配列に変換します。
     * 
     * @param o
     * @return
     */
    public static byte[] fromObjectToBinary(Object o) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(
                    BYTE_ARRAY_SIZE);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(o);
            } finally {
                oos.close();
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * byteの配列をオブジェクトに変換します。
     * 
     * @param binary
     * @return
     */
    public static Object fromBinaryToObject(byte[] binary) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(binary);
            ObjectInputStream ois = new ObjectInputStream(bais);
            try {
                return ois.readObject();
            } finally {
                ois.close();
            }
        } catch (IOException ex) {
            throw new IORuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new ClassNotFoundRuntimeException(ex);
        }
    }

}
