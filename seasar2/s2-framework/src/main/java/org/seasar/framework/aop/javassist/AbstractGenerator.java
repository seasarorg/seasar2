/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.aop.javassist;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import org.seasar.framework.exception.CannotCompileRuntimeException;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.IllegalAccessRuntimeException;
import org.seasar.framework.exception.InvocationTargetRuntimeException;
import org.seasar.framework.exception.NoSuchMethodRuntimeException;
import org.seasar.framework.exception.NotFoundRuntimeException;
import org.seasar.framework.util.ClassPoolUtil;
import org.seasar.framework.util.ClassUtil;

/**
 * バイトコードを生成するための抽象クラスです。
 * 
 * @author koichik
 */
public class AbstractGenerator {
    /**
     * defineClassです。
     */
    protected static final String DEFINE_CLASS_METHOD_NAME = "defineClass";

    /**
     * 保護ドメインです。
     */
    protected static final ProtectionDomain protectionDomain;

    /**
     * defineClassメソッドです。
     */
    protected static Method defineClassMethod;

    // static initializer
    static {
        protectionDomain = (ProtectionDomain) AccessController
                .doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        return AspectWeaver.class.getProtectionDomain();
                    }
                });

        AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
                final Class[] paramTypes = new Class[] { String.class,
                        byte[].class, int.class, int.class,
                        ProtectionDomain.class };
                try {
                    final Class loader = ClassUtil.forName(ClassLoader.class
                            .getName());
                    defineClassMethod = loader.getDeclaredMethod(
                            DEFINE_CLASS_METHOD_NAME, paramTypes);
                    defineClassMethod.setAccessible(true);
                } catch (final NoSuchMethodException e) {
                    throw new NoSuchMethodRuntimeException(ClassLoader.class,
                            DEFINE_CLASS_METHOD_NAME, paramTypes, e);
                }
                return null;
            }
        });
    }

    /**
     * クラスプールです。
     */
    protected final ClassPool classPool;

    /**
     * オブジェクトの表現から文字列表現に変換します。
     * 
     * @param type
     *            型
     * @param expr
     *            値
     * @return 文字列表現
     */
    protected static String fromObject(final Class type, final String expr) {
        if (type.equals(void.class) || type.equals(Object.class)) {
            return expr;
        }
        if (type.equals(boolean.class) || type.equals(char.class)) {
            final Class wrapper = ClassUtil.getWrapperClass(type);
            return "((" + wrapper.getName() + ") " + expr + ")."
                    + type.getName() + "Value()";
        }
        if (type.isPrimitive()) {
            return "((java.lang.Number) " + expr + ")." + type.getName()
                    + "Value()";
        }
        return "(" + ClassUtil.getSimpleClassName(type) + ") " + expr;
    }

    /**
     * オブジェクトの文字列表現に変換します。
     * 
     * @param type
     *            型
     * @param expr
     *            値
     * @return 文字列表現
     */
    protected static String toObject(final Class type, final String expr) {
        if (type.isPrimitive()) {
            final Class wrapper = ClassUtil.getWrapperClass(type);
            return "new " + wrapper.getName() + "(" + expr + ")";
        }
        return expr;
    }

    /**
     * {@link AbstractGenerator}を作成します。
     * 
     * @param classPool
     *            クラスプール
     */
    protected AbstractGenerator(final ClassPool classPool) {
        this.classPool = classPool;
    }

    /**
     * コンパイル時のクラスに変換します。
     * 
     * @param clazz
     *            元のクラス
     * @return コンパイル時のクラス
     */
    protected CtClass toCtClass(final Class clazz) {
        return ClassPoolUtil.toCtClass(classPool, clazz);
    }

    /**
     * コンパイル時のクラスに変換します。
     * 
     * @param className
     *            クラス名
     * @return コンパイル時のクラス
     */
    protected CtClass toCtClass(final String className) {
        return ClassPoolUtil.toCtClass(classPool, className);
    }

    /**
     * コンパイル時のクラスの配列に変換します。
     * 
     * @param classNames
     *            元のクラス名の配列
     * @return コンパイル時のクラスの配列
     */
    protected CtClass[] toCtClassArray(final String[] classNames) {
        return ClassPoolUtil.toCtClassArray(classPool, classNames);
    }

    /**
     * コンパイル時のクラスの配列に変換します。
     * 
     * @param classes
     *            元のクラスの配列
     * @return コンパイル時のクラスの配列
     */
    protected CtClass[] toCtClassArray(final Class[] classes) {
        return ClassPoolUtil.toCtClassArray(classPool, classes);
    }

    /**
     * コンパイル時のクラスを作成します。
     * 
     * @param name
     *            クラス名
     * @return コンパイル時のクラス
     */
    protected CtClass createCtClass(final String name) {
        return ClassPoolUtil.createCtClass(classPool, name);
    }

    /**
     * コンパイル時のクラスを作成します。
     * 
     * @param name
     *            クラス名
     * @param superClass
     *            親クラス
     * @return コンパイル時のクラス
     */
    protected CtClass createCtClass(final String name, final Class superClass) {
        return ClassPoolUtil.createCtClass(classPool, name, superClass);
    }

    /**
     * コンパイル時のクラスを作成します。
     * 
     * @param name
     *            クラス名
     * @param superClass
     *            親クラス
     * @return コンパイル時のクラス
     */
    protected CtClass createCtClass(final String name, final CtClass superClass) {
        return ClassPoolUtil.createCtClass(classPool, name, superClass);
    }

    /**
     * コンパイル時のクラスを取得して名前を変えます。
     * 
     * @param orgClass
     *            元のクラス
     * @param newName
     *            新しい名前
     * @return コンパイル時のクラス
     */
    protected CtClass getAndRenameCtClass(final Class orgClass,
            final String newName) {
        return getAndRenameCtClass(ClassUtil.getSimpleClassName(orgClass),
                newName);
    }

    /**
     * コンパイル時のクラスを取得して名前を変えます。
     * 
     * @param orgName
     *            元の名前
     * @param newName
     *            新しい名前
     * @return コンパイル時のクラス
     */
    protected CtClass getAndRenameCtClass(final String orgName,
            final String newName) {
        try {
            return classPool.getAndRename(orgName, newName);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    /**
     * <code>CtClass</code>を<code>Class</code>に変更します。
     * 
     * @param classLoader
     *            クラスローダ
     * @param ctClass
     *            コンパイル時のクラス
     * @return クラス
     */
    public Class toClass(final ClassLoader classLoader, final CtClass ctClass) {
        try {
            final byte[] bytecode = ctClass.toBytecode();
            return (Class) defineClassMethod.invoke(classLoader, new Object[] {
                    ctClass.getName(), bytecode, new Integer(0),
                    new Integer(bytecode.length), protectionDomain });
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(ClassLoader.class, e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(ClassLoader.class, e);
        }
    }

    /**
     * インターフェースを設定します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @param interfaceType
     *            インターフェース
     */
    protected void setInterface(final CtClass clazz, final Class interfaceType) {
        clazz.setInterfaces(new CtClass[] { toCtClass(interfaceType) });
    }

    /**
     * インターフェースの配列を設定します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @param interfaces
     *            インターフェースの配列
     */
    protected void setInterfaces(final CtClass clazz, final Class[] interfaces) {
        clazz.setInterfaces(toCtClassArray(interfaces));
    }

    /**
     * デフォルトコンストラクタを作成します。
     * 
     * @param clazz
     *            元のクラス
     * @return コンパイル時コンストラクタ
     */
    protected CtConstructor createDefaultConstructor(final Class clazz) {
        return createDefaultConstructor(toCtClass(clazz));
    }

    /**
     * デフォルトコンストラクタを作成します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @return コンパイル時コンストラクタ
     */
    protected CtConstructor createDefaultConstructor(final CtClass clazz) {
        try {
            final CtConstructor ctConstructor = CtNewConstructor
                    .defaultConstructor(clazz);
            clazz.addConstructor(ctConstructor);
            return ctConstructor;
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    /**
     * コンストラクタを作成します。
     * 
     * @param clazz
     *            対象となるコンパイル時クラス
     * @param constructor
     *            元のコンストラクタ
     * @return コンパイル時コンストラクタ
     */
    protected CtConstructor createConstructor(final CtClass clazz,
            final Constructor constructor) {
        return createConstructor(clazz, toCtClassArray(constructor
                .getParameterTypes()), toCtClassArray(constructor
                .getExceptionTypes()));
    }

    /**
     * コンストラクタを作成します。
     * 
     * @param clazz
     *            対象となるコンパイル時クラス
     * @param parameterTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @return コンパイル時コンストラクタ
     */
    protected CtConstructor createConstructor(final CtClass clazz,
            final CtClass[] parameterTypes, final CtClass[] exceptionTypes) {
        try {
            final CtConstructor ctConstructor = CtNewConstructor.make(
                    parameterTypes, exceptionTypes, clazz);
            clazz.addConstructor(ctConstructor);
            return ctConstructor;
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    /**
     * 宣言されているメソッドを返します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @param name
     *            メソッド名
     * @param argTypes
     *            パラメータの型の配列
     * @return コンパイル時メソッド
     */
    protected CtMethod getDeclaredMethod(final CtClass clazz,
            final String name, final CtClass[] argTypes) {
        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (final NotFoundException e) {
            throw new NotFoundRuntimeException(e);
        }
    }

    /**
     * メソッドを作成します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @param src
     *            ソース
     * @return コンパイル時メソッド
     */
    protected CtMethod createMethod(final CtClass clazz, final String src) {
        try {
            final CtMethod ctMethod = CtNewMethod.make(src, clazz);
            clazz.addMethod(ctMethod);
            return ctMethod;
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    /**
     * メソッドを作成します。
     * 
     * @param clazz
     *            対象のコンパイル時クラス
     * @param method
     *            元のメソッド
     * @param body
     *            メソッドの中身
     * @return コンパイル時メソッド
     */
    protected CtMethod createMethod(final CtClass clazz, final Method method,
            final String body) {
        return createMethod(clazz, method.getModifiers(), method
                .getReturnType(), method.getName(), method.getParameterTypes(),
                method.getExceptionTypes(), body);
    }

    /**
     * メソッドを作成します。
     * 
     * @param clazz
     *            対象となるコンパイル時クラス
     * @param modifier
     *            アクセス修飾子
     * @param returnType
     *            戻り値の型
     * @param methodName
     *            メソッド名
     * @param parameterTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @param body
     *            メソッドの中身
     * @return コンパイル時メソッド
     */
    protected CtMethod createMethod(final CtClass clazz, final int modifier,
            final Class returnType, final String methodName,
            final Class[] parameterTypes, final Class[] exceptionTypes,
            final String body) {
        try {
            final CtMethod ctMethod = CtNewMethod.make(modifier
                    & ~(Modifier.ABSTRACT | Modifier.NATIVE),
                    toCtClass(returnType), methodName,
                    toCtClassArray(parameterTypes),
                    toCtClassArray(exceptionTypes), body, clazz);
            clazz.addMethod(ctMethod);
            return ctMethod;
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    /**
     * メソッドの中身を設定します。
     * 
     * @param method
     *            コンパイル時メソッド
     * @param src
     *            ソース
     */
    protected void setMethodBody(final CtMethod method, final String src) {
        try {
            method.setBody(src);
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }
}