/*
 * Copyright 2004-2005 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.intertype;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import org.seasar.framework.aop.InterType;
import org.seasar.framework.aop.javassist.CannotCompileRuntimeException;
import org.seasar.framework.aop.javassist.ClassPoolUtil;
import org.seasar.framework.aop.javassist.NotFoundRuntimeException;

public abstract class AbstractInterType implements InterType {
    public static final String COMPONENT = "instance = prototype";

    protected Class targetClass;

    protected CtClass enhancedClass;

    protected ClassPool classPool;

    public void introduce(final Class targetClass, final CtClass enhancedClass) {
        this.targetClass = targetClass;
        this.enhancedClass = enhancedClass;
        this.classPool = enhancedClass.getClassPool();
        try {
            introduce();
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    protected abstract void introduce() throws CannotCompileException,
            NotFoundException;

    protected Class getTargetClass() {
        return targetClass;
    }

    protected CtClass getEnhancedClass() {
        return enhancedClass;
    }

    protected ClassPool getClassPool() {
        return classPool;
    }

    protected void addInterface(final Class clazz) {
        enhancedClass.addInterface(toCtClass(clazz));
    }

    protected void addField(final Class type, final String name) {
        addField(Modifier.PRIVATE, type, name);
    }

    protected void addField(final Class type, final String name,
            final String init) {
        addField(Modifier.PRIVATE, type, name, init);
    }

    protected void addStaticField(final Class type, final String name) {
        addStaticField(Modifier.PRIVATE, type, name);
    }

    protected void addStaticField(final Class type, final String name,
            final String init) {
        addStaticField(Modifier.PRIVATE, type, name, init);
    }

    protected void addConstant(final Class type, final String name,
            final String init) {
        addStaticField(Modifier.PUBLIC | Modifier.FINAL, type, name, init);
    }

    protected void addStaticField(final int modifiers, final Class type,
            final String name) {
        addField(Modifier.STATIC | modifiers, type, name);
    }

    protected void addStaticField(final int modifiers, final Class type,
            final String name, final String init) {
        addField(Modifier.STATIC | modifiers, type, name, init);
    }

    protected void addStaticField(final int modifiers, final Class type,
            final String name, final CtField.Initializer init) {
        addField(Modifier.STATIC | modifiers, type, name, init);
    }

    protected void addField(final int modifiers, final Class type,
            final String name) {
        try {
            final CtField field = new CtField(toCtClass(type), name,
                    enhancedClass);
            field.setModifiers(modifiers);
            enhancedClass.addField(field);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    protected void addField(final int modifiers, final Class type,
            final String name, final String init) {
        try {
            final CtField field = new CtField(toCtClass(type), name,
                    enhancedClass);
            field.setModifiers(modifiers);
            enhancedClass.addField(field, init);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    protected void addField(final int modifiers, final Class type,
            final String name, final CtField.Initializer init) {
        try {
            final CtField field = new CtField(toCtClass(type), name,
                    enhancedClass);
            field.setModifiers(modifiers);
            enhancedClass.addField(field, init);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    protected void addMethod(final String name, final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, null, null, src);
    }

    protected void addMethod(final String name, final Class[] paramTypes,
            final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, paramTypes, null, src);
    }

    protected void addMethod(final String name, final Class[] paramTypes,
            Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, paramTypes,
                exceptionTypes, src);
    }

    protected void addMethod(final Class returnType, final String name,
            final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, null, null, src);
    }

    protected void addMethod(final Class returnType, final String name,
            final Class[] paramTypes, final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, paramTypes, null, src);
    }

    protected void addMethod(final Class returnType, final String name,
            final Class[] paramTypes, Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, paramTypes,
                exceptionTypes, src);
    }

    protected void addStaticMethod(final String name, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name, null, null, src);
    }

    protected void addStaticMethod(final String name, final Class[] paramTypes,
            final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name, paramTypes, null, src);
    }

    protected void addStaticMethod(final String name, final Class[] paramTypes,
            Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name, paramTypes,
                exceptionTypes, src);
    }

    protected void addStaticMethod(final Class returnType, final String name,
            final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name, null, null, src);
    }

    protected void addStaticMethod(final Class returnType, final String name,
            final Class[] paramTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name, paramTypes, null, src);
    }

    protected void addStaticMethod(final Class returnType, final String name,
            final Class[] paramTypes, Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name, paramTypes,
                exceptionTypes, src);
    }

    protected void addMethod(final int modifiers, final Class returnType,
            final String name, final Class[] paramTypes,
            Class[] exceptionTypes, final String src) {
        try {
            final CtMethod ctMethod = CtNewMethod.make(modifiers,
                    toCtClass(returnType), name, toCtClassArray(paramTypes),
                    toCtClassArray(exceptionTypes), src, enhancedClass);
            enhancedClass.addMethod(ctMethod);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    protected CtClass toCtClass(final Class clazz) {
        return ClassPoolUtil.toCtClass(classPool, clazz);
    }

    protected CtClass[] toCtClassArray(final Class[] classes) {
        return ClassPoolUtil.toCtClassArray(classPool, classes);
    }
}
