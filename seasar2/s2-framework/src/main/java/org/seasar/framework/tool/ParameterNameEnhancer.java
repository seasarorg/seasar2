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
package org.seasar.framework.tool;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

import org.seasar.framework.exception.CannotCompileRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.NotFoundRuntimeException;
import org.seasar.framework.util.ClassPoolUtil;

/**
 * 
 * @author koichik
 */
public class ParameterNameEnhancer {

    protected static final String PARAMETER_NAME_ANNOTATION_FQCN = "org.seasar.framework.beans.annotation.ParameterName";

    protected final ClassPool pool;

    protected final String className;

    protected final CtClass clazz;

    public ParameterNameEnhancer(final String className) {
        this(className, Thread.currentThread().getContextClassLoader());
    }

    public ParameterNameEnhancer(final String className,
            final ClassLoader loader) {
        this(className, ClassPoolUtil.getClassPool(loader));
    }

    public ParameterNameEnhancer(final String className, final ClassPool pool) {
        this.className = className;
        this.pool = pool;
        try {
            clazz = pool.get(className);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    public void setConstructorParameterNames(final String[] parameterTypeNames,
            final String[] parameterNames) {
        assert parameterTypeNames.length == parameterNames.length;
        if (parameterTypeNames.length == 0) {
            return;
        }
        try {
            setParameterNames(clazz.getDeclaredConstructor(ClassPoolUtil
                    .toCtClassArray(pool, parameterTypeNames)), parameterNames);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    public void setMethodParameterNames(final String methodName,
            final String[] parameterTypeNames, final String[] parameterNames) {
        assert parameterTypeNames.length == parameterNames.length;
        if (parameterTypeNames.length == 0) {
            return;
        }

        try {
            final CtMethod method = clazz.getDeclaredMethod(methodName,
                    ClassPoolUtil.toCtClassArray(pool, parameterTypeNames));
            setParameterNames(method, parameterNames);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    public void save() {
        try {
            final String[] names = className.split("\\.");
            final URL url = pool.find(className);
            File file = new File(new URI(url.toString()));
            for (int i = 0; i < names.length; ++i) {
                file = file.getParentFile();
            }
            save(file.getAbsolutePath());
        } catch (final URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(final String baseDirectoryName) {
        try {
            clazz.writeFile(baseDirectoryName);
            clazz.detach();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    protected void setParameterNames(final CtBehavior behavior,
            final String[] parameterNames) throws NotFoundException {
        final ParameterAnnotationsAttribute annotationsAttribute = getParameterAnnotationsAttribute(behavior);
        final Annotation[][] annotationsArray = annotationsAttribute
                .getAnnotations();
        assert annotationsArray.length == parameterNames.length;

        for (int i = 0; i < annotationsArray.length; ++i) {
            final Annotation[] annotations = createParameterAnnotations(
                    behavior, annotationsArray[i], parameterNames[i]);
            annotationsArray[i] = annotations;
        }

        annotationsAttribute.setAnnotations(annotationsArray);
        behavior.getMethodInfo().addAttribute(annotationsAttribute);
    }

    protected ParameterAnnotationsAttribute getParameterAnnotationsAttribute(
            final CtBehavior behavior) throws NotFoundException {
        final ParameterAnnotationsAttribute annotationsAttribute = (ParameterAnnotationsAttribute) behavior
                .getMethodInfo().getAttribute(
                        ParameterAnnotationsAttribute.visibleTag);
        if (annotationsAttribute != null) {
            return annotationsAttribute;
        }
        return createParameterAnnotationsAttribute(behavior);
    }

    protected ParameterAnnotationsAttribute createParameterAnnotationsAttribute(
            final CtBehavior behavior) throws NotFoundException {
        final ParameterAnnotationsAttribute annotationsAttribute = new ParameterAnnotationsAttribute(
                behavior.getMethodInfo().getConstPool(),
                ParameterAnnotationsAttribute.visibleTag);
        final int length = behavior.getParameterTypes().length;
        final Annotation[][] annotations = new Annotation[length][];
        for (int i = 0; i < length; ++i) {
            annotations[i] = new Annotation[0];
        }
        annotationsAttribute.setAnnotations(annotations);
        return annotationsAttribute;
    }

    protected Annotation[] createParameterAnnotations(
            final CtBehavior behavior, final Annotation[] annotations,
            final String parameterName) {
        for (int i = 0; i < annotations.length; ++i) {
            final Annotation annotation = annotations[i];
            if (PARAMETER_NAME_ANNOTATION_FQCN.equals(annotation.getTypeName())) {
                return annotations;
            }
        }

        final Annotation[] newAnnotations = new Annotation[annotations.length + 1];
        System.arraycopy(annotations, 0, newAnnotations, 0, annotations.length);
        newAnnotations[annotations.length] = createAnnotation(behavior,
                parameterName);
        return newAnnotations;
    }

    protected Annotation createAnnotation(final CtBehavior behavior,
            final String parameterName) {
        final ConstPool constPool = behavior.getMethodInfo().getConstPool();
        final Annotation annotation = new Annotation(
                PARAMETER_NAME_ANNOTATION_FQCN, constPool);
        annotation.addMemberValue("value", new StringMemberValue(parameterName,
                constPool));
        return annotation;
    }

    protected String getSimpleClassName(final Class clazz) {
        if (clazz.isArray()) {
            return getSimpleClassName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }
}
