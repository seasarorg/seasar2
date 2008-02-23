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
package org.seasar.extension.jdbc.gen;

/**
 * Javaファイルを生成するインタフェースです。
 * 
 * @author taedium
 */
public interface JavaFileGenerator {

    /**
     * {@link JavaCode}からJavaファイルを生成します。
     * 
     * <p>
     * このメソッドの呼び出しは次のメソッドの実行に相当します。
     * </p>
     * <blockquote>
     * 
     * <pre>
     *  generate(javaCode, false)
     * </pre>
     * 
     * </blockquote>
     * 
     * @param javaCode
     *            Javaコード
     */
    void generate(JavaCode javaCode);

    /**
     * {@link JavaCode}からJavaファイルを生成します。
     * 
     * @param javaCode
     *            Javaコード
     * @param overwrite
     *            すでに存在する場合に上書きするならば{@code true}、何もしないならば{@code false}
     */
    void generate(JavaCode javaCode, boolean overwrite);

}
