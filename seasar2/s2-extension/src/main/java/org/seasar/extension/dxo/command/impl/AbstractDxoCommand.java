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
package org.seasar.extension.dxo.command.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.extension.dxo.IllegalSignatureRuntimeException;
import org.seasar.extension.dxo.command.DxoCommand;

/**
 * @author koichik
 * 
 */
public abstract class AbstractDxoCommand implements DxoCommand {

    protected ConversionHelper conversionHelper;

    public AbstractDxoCommand(final Method method) {
        this.conversionHelper = getConversionHelper(method);
    }

    public Object execute(final Object[] args) {
        return conversionHelper.convert(args);
    }

    protected abstract Object convertScalar(Object source);

    protected abstract Class getDestElementType();

    protected abstract void copy(Object src, Object dest);

    protected Object[] createArray(final int length) {
        return (Object[]) Array.newInstance(getDestElementType(), length);
    }

    protected ConversionHelper getConversionHelper(final Method method) {
        final Class[] parameterTypes = method.getParameterTypes();
        final int parameterSize = parameterTypes.length;
        if (parameterSize != 1 && parameterSize != 2) {
            throw new IllegalSignatureRuntimeException(method
                    .getDeclaringClass(), method);
        }
        final Class sourceType = parameterTypes[0];
        final Class destType = parameterSize == 1 ? method.getReturnType()
                : parameterTypes[1];

        if (sourceType.isArray()) {
            if (destType.isArray()) {
                return new ArrayToArrayConversionHelper();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ArrayToListConversionHelper();
            }
        } else if (List.class.isAssignableFrom(sourceType)) {
            if (destType.isArray()) {
                return new ListToArrayConversionHelper();
            } else if (List.class.isAssignableFrom(destType)) {
                return new ListToListConvertsionHelper();
            }
        } else {
            return new ScalarConversionHelper();
        }

        throw new IllegalSignatureRuntimeException(method.getDeclaringClass(),
                method);
    }

    public interface ConversionHelper {
        Object convert(Object[] args);
    }

    public class ScalarConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final Object dest = convertScalar(args[0]);
            if (args.length > 1) {
                copy(dest, args[1]);
            }
            return dest;
        }
    }

    public class ArrayToArrayConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final Object[] dest = args.length == 1 ? createArray(src.length)
                    : (Object[]) args[1];
            for (int i = 0; i < src.length && i < dest.length; ++i) {
                dest[i] = convertScalar(src[i]);
            }
            return dest;
        }
    }

    public class ArrayToListConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final Object[] src = (Object[]) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            dest.clear();
            for (int i = 0; i < src.length; ++i) {
                dest.add(convertScalar(src[i]));
            }
            return dest;
        }
    }

    public class ListToArrayConversionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final List src = (List) args[0];
            final Object[] dest = args.length == 1 ? createArray(src.size())
                    : (Object[]) args[1];
            int i = 0;
            for (final Iterator it = src.iterator(); it.hasNext()
                    && i < dest.length; ++i) {
                dest[i] = convertScalar(it.next());
            }
            return dest;
        }
    }

    public class ListToListConvertsionHelper implements ConversionHelper {
        public Object convert(final Object[] args) {
            final List src = (List) args[0];
            final List dest = args.length == 1 ? new ArrayList()
                    : (List) args[1];
            dest.clear();
            for (final Iterator it = src.iterator(); it.hasNext();) {
                dest.add(convertScalar(it.next()));
            }
            return dest;
        }
    }
}
