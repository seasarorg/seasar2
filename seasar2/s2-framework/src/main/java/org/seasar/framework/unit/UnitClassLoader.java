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
package org.seasar.framework.unit;

/**
 * {@link S2FrameworkTestCase}を使用した単体テスト等で使用する{@link ClassLoader クラスローダ}です。
 * <p>
 * アスペクトが適用されたクラスを大量に使用するテストを連続して実行する際に、 {@link OutOfMemoryError}
 * の頻発を回避する目的で使用します。
 * </p>
 * <p>
 * クラスは通常、 システムクラスローダによりVMのパーマネント領域にロードされますが、 新たにクラスをロードする領域がなくなると、
 * <code>OutOfMemoryError</code>が発生します。 <code>S2FrameworkTestCase</code>では、
 * テストメソッド毎に、 この<code>UnitClassLoader</code>を生成、 使用、 消滅させることにより、
 * アスペクトが適用されたクラスがGCされることで、 パーマネント領域が不足する問題を回避しています。
 * </p>
 * 
 * @author higa
 * @author belltree
 * @author goto
 */
public class UnitClassLoader extends ClassLoader {

    /**
     * 親クラスローダを指定して<code>UnitClassLoader</code>を構築します。
     * 
     * @param parent
     *            親クラスローダ
     */
    public UnitClassLoader(ClassLoader parent) {
        super(parent);
    }
}