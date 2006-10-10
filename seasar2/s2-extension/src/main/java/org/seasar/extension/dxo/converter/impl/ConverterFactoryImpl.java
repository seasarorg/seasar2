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
package org.seasar.extension.dxo.converter.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.seasar.extension.dxo.converter.Converter;
import org.seasar.extension.dxo.converter.ConverterFactory;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.Disposable;
import org.seasar.framework.util.DisposableUtil;

/**
 * @author koichik
 * 
 */
public class ConverterFactoryImpl implements ConverterFactory, Disposable {

    protected static final Converter[] BUILDTIN_CONVERTERS = new Converter[] {
            new ArrayConverter(), new BeanConverter(),
            new BigDecimalConverter(), new BigIntegerConverter(),
            new BooleanConverter(), new ByteConverter(),
            new CalendarConverter(), new CharacterConverter(),
            new CharArrayConverter(), new DateConverter(),
            new DoubleConverter(), new FloatConverter(),
            new IntegerConverter(), new ListConverter(), new LongConverter(),
            new SetConverter(), new ShortConverter(), new SqlDateConverter(),
            new SqlTimeConverter(), new SqlTimestampConverter(),
            new StringConverter(), };

    protected static Map PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY = new HashMap();
    static {
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(boolean[].class, Boolean[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(char[].class, Character[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(byte[].class, Byte[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(short[].class, Short[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(int[].class, Integer[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(long[].class, Long[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(float[].class, Float[].class);
        PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY.put(double[].class, Double[].class);
    }

    protected boolean initialized;

    protected S2Container container;

    protected Converter[] converters;

    protected Map converterCache = new HashMap();

    public ConverterFactoryImpl() {
    }

    public void setContainer(final S2Container container) {
        this.container = container;
    }

    public void initialize() {
        if (initialized) {
            return;
        }
        converters = (Converter[]) ArrayUtil.add(BUILDTIN_CONVERTERS, container
                .findAllComponents(Converter.class));
        DisposableUtil.add(this);
        initialized = true;
    }

    public void dispose() {
        converters = null;
        converterCache.clear();
        initialized = false;
    }

    public Converter getConverter(final Class sourceClass, final Class destClass) {
        initialize();
        final Class destType = ClassUtil.getWrapperClassIfPrimitive(destClass);
        final String cacheKey = sourceClass.getName() + destType.getName();
        final Converter converter = (Converter) converterCache.get(cacheKey);
        if (converter != null) {
            return converter;
        }
        return detectConverter(sourceClass, destType);
    }

    protected Converter detectConverter(final Class sourceClass,
            final Class destClass) {
        final Converter converter = getDistanceTable(sourceClass, destClass);
        final String cacheKey = sourceClass.getName() + destClass.getName();
        converterCache.put(cacheKey, converter);
        return converter;
    }

    private Converter getDistanceTable(final Class sourceClass,
            final Class destClass) {
        final Map distanceTable = new TreeMap();
        for (int i = 0; i < converters.length; ++i) {
            final Converter converter = converters[i];
            if (!canConvert(sourceClass, destClass, converter)) {
                continue;
            }
            final double distance = getDistance(converter.getDestClass(),
                    destClass);
            distanceTable.put(new Double(distance), converter);
        }
        if (distanceTable.isEmpty()) {
            throw new IllegalStateException("converter　was not found."); // TODO
        }
        return (Converter) distanceTable.values().iterator().next();
    }

    private boolean canConvert(final Class sourceClass, final Class destClass,
            final Converter converter) {
        if (!converter.getDestClass().isAssignableFrom(destClass)) {
            final Class wrapperArray = (Class) PRIMITIVE_ARRAY_TO_WRAPPER_ARRAY
                    .get(destClass);
            if (wrapperArray != null) {
                return canConvert(sourceClass, wrapperArray, converter);
            }
            return false;
        }

        final Class[] sourceClasses = converter.getSourceClasses();
        for (int i = 0; i < sourceClasses.length; i++) {
            if (sourceClasses[i].isAssignableFrom(sourceClass)) {
                return true;
            }
        }

        return false;
    }

    private double getDistance(final Class assigner, final Class assignee) {
        if (assignee.isArray() && assigner.isArray()) {
            return getDistance(assigner.getComponentType(), assignee
                    .getComponentType(), 0);
        } else if (assignee.isArray()) {
            return getDistance(assigner, assignee, 10); // TODO 一方だけ配列の時のクラス間の距離
            // 一先ず10にしておく
        }
        return getDistance(assigner, assignee, 0);
    }

    private double getDistance(final Class assigner, final Class assignee,
            final double distance) {
        if (assignee.equals(assigner)) {
            return distance;
        }
        if (isImplements(assigner, assignee)) {
            return distance + 0.5;
        }

        final Class superClass = assigner.getSuperclass();
        if (superClass == null) {
            return distance + 1;
        }
        return getDistance(superClass, assignee, distance + 1);
    }

    private boolean isImplements(final Class assigner, final Class assignee) {
        return !assigner.isInterface() && assignee.isInterface()
                && assignee.isAssignableFrom(assigner);
    }

}
