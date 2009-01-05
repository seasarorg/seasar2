/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
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
import org.seasar.framework.exception.CannotCompileRuntimeException;
import org.seasar.framework.exception.NotFoundRuntimeException;
import org.seasar.framework.util.ClassPoolUtil;

/**
 * {@link InterType}の抽象クラスです。
 */
public abstract class AbstractInterType implements InterType {

    /**
     * Componentアノテーションです。
     */
    public static final String COMPONENT = "instance = prototype";

    /**
     * ターゲットクラスです。
     */
    protected Class targetClass;

    /**
     * エンハンスされたクラスです。
     */
    protected CtClass enhancedClass;

    /**
     * クラスプールです。
     */
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
        } finally {
            this.targetClass = null;
            this.enhancedClass = null;
            this.classPool = null;
        }
    }

    /**
     * クラス構造を変更します。
     * 
     * @throws CannotCompileException
     *             コンパイルできない場合
     * @throws NotFoundException
     *             何かが見つからない場合
     */
    protected abstract void introduce() throws CannotCompileException,
            NotFoundException;

    /**
     * ターゲットクラスを返します。
     * 
     * @return
     */
    protected Class getTargetClass() {
        return targetClass;
    }

    /**
     * エンハンスされたクラスを返します。
     * 
     * @return エンハンスされたクラス
     */
    protected CtClass getEnhancedClass() {
        return enhancedClass;
    }

    /**
     * クラスプールを返します。
     * 
     * @return クラスプール
     */
    protected ClassPool getClassPool() {
        return classPool;
    }

    /**
     * インターフェースを追加します。
     * 
     * @param clazz
     *            インターフェース
     */
    protected void addInterface(final Class clazz) {
        enhancedClass.addInterface(toCtClass(clazz));
    }

    /**
     * フィールドを追加します。
     * 
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     */
    protected void addField(final Class type, final String name) {
        addField(Modifier.PRIVATE, type, name);
    }

    /**
     * フィールドを追加します。
     * 
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
    protected void addField(final Class type, final String name,
            final String init) {
        addField(Modifier.PRIVATE, type, name, init);
    }

    /**
     * 静的フィールドを追加します。
     * 
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     */
    protected void addStaticField(final Class type, final String name) {
        addStaticField(Modifier.PRIVATE, type, name);
    }

    /**
     * 静的フィールドを追加します。
     * 
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
    protected void addStaticField(final Class type, final String name,
            final String init) {
        addStaticField(Modifier.PRIVATE, type, name, init);
    }

    /**
     * 定数を追加します。
     * 
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
    protected void addConstant(final Class type, final String name,
            final String init) {
        addStaticField(Modifier.PUBLIC | Modifier.FINAL, type, name, init);
    }

    /**
     * 静的フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     */
    protected void addStaticField(final int modifiers, final Class type,
            final String name) {
        addField(Modifier.STATIC | modifiers, type, name);
    }

    /**
     * 静的フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
    protected void addStaticField(final int modifiers, final Class type,
            final String name, final String init) {
        addField(Modifier.STATIC | modifiers, type, name, init);
    }

    /**
     * 静的フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
    protected void addStaticField(final int modifiers, final Class type,
            final String name, final CtField.Initializer init) {
        addField(Modifier.STATIC | modifiers, type, name, init);
    }

    /**
     * フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     */
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

    /**
     * フィールドを追加します。
     * 
     * @param src
     *            ソース
     */
    protected void addField(final String src) {
        try {
            enhancedClass.addField(CtField.make(src, enhancedClass));
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
    }

    /**
     * フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールドの型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
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

    /**
     * フィールドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param type
     *            フィールド型
     * @param name
     *            フィールド名
     * @param init
     *            初期化情報
     */
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

    /**
     * メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param src
     *            ソース
     */
    protected void addMethod(final String name, final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, null, null, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param src
     *            ソース
     */
    protected void addMethod(final String name, final Class[] paramTypes,
            final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, paramTypes, null, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外クラスの配列
     * @param src
     *            ソース
     */
    protected void addMethod(final String name, final Class[] paramTypes,
            Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC, void.class, name, paramTypes,
                exceptionTypes, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param src
     *            ソース
     */
    protected void addMethod(final Class returnType, final String name,
            final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, null, null, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param src
     *            ソース
     */
    protected void addMethod(final Class returnType, final String name,
            final Class[] paramTypes, final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, paramTypes, null, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @param src
     *            ソース
     */
    protected void addMethod(final Class returnType, final String name,
            final Class[] paramTypes, Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC, returnType, name, paramTypes,
                exceptionTypes, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final String name, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name, null,
                null, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final String name, final Class[] paramTypes,
            final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name,
                paramTypes, null, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final String name, final Class[] paramTypes,
            Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, void.class, name,
                paramTypes, exceptionTypes, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final Class returnType, final String name,
            final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name, null,
                null, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final Class returnType, final String name,
            final Class[] paramTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name,
                paramTypes, null, src);
    }

    /**
     * 静的メソッドを追加します。
     * 
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @param src
     *            ソース
     */
    protected void addStaticMethod(final Class returnType, final String name,
            final Class[] paramTypes, Class[] exceptionTypes, final String src) {
        addMethod(Modifier.PUBLIC | Modifier.STATIC, returnType, name,
                paramTypes, exceptionTypes, src);
    }

    /**
     * メソッドを追加します。
     * 
     * @param modifiers
     *            アクセス修飾子
     * @param returnType
     *            戻り値の型
     * @param name
     *            メソッド名
     * @param paramTypes
     *            パラメータの型の配列
     * @param exceptionTypes
     *            例外の型の配列
     * @param src
     *            ソース
     */
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

    /**
     * メソッドを追加します。
     * 
     * @param src
     *            ソース
     */
    protected void addMethod(final String src) {
        try {
            enhancedClass.addMethod(CtNewMethod.make(src, enhancedClass));
        } catch (final CannotCompileException e) {
            throw new CannotCompileRuntimeException(e);
        }
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
     * コンパイル時のクラスの配列に変換します。
     * 
     * @param classes
     *            元のクラスの配列
     * @return コンパイル時のクラスの配列
     */
    protected CtClass[] toCtClassArray(final Class[] classes) {
        return ClassPoolUtil.toCtClassArray(classPool, classes);
    }
}
