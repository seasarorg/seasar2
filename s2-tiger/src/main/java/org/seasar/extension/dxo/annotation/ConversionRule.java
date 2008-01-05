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
package org.seasar.extension.dxo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 変換ルールを指定します。
 * 
 * @author koichik
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConversionRule {

    /**
     * 変換ルールです。
     * <p>
     * 指定の方法は2種類あります。
     * </p>
     * <dl>
     * <dt>簡易記法</dt>
     * <dd>
     * <p>
     * 変換先のプロパティ名と変換元のプロパティ名のペアをコロン区切りで指定します。 カンマ区切りで複数のペアを指定することができます。
     * </p>
     * 
     * <pre>
     * &#64;ConversionRule("aaa : xxx, bbb : yyy, ccc : zzz")
     * </pre>
     * 
     * <p>
     * 変換元および変換先のプロパティは、ピリオド区切りでネストしたプロパティを指定することができます。
     * ただし、演算子を含むような複雑なOGNL式を指定することはできません。
     * </p>
     * </dd>
     * <dt>OGNL記法</dt>
     * <dd>
     * <p>
     * 変換先のプロパティ名とその値となるOGNL式のペアをコロン区切りで指定します。 カンマ区切りで複数のペアを指定することができます。
     * 変換先のプロパティ名は文字列で指定します．そのため，シングルクオートまたはダブルクオートで囲む必要があります。
     * </p>
     * 
     * <pre>
     * &#64;ConversionRule("'aaa' : xxx, 'bbb' : yyy == null ? '' : yyy.toString()")
     * </pre>
     * 
     * </dd>
     * </dl>
     * 
     * @return 変換ルール
     */
    String value();

}
