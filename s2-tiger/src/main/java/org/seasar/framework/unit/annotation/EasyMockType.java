/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.annotation;

/**
 * モックの種類です。
 * 
 * @author koichik
 * @author taedium
 */
public enum EasyMockType {
    /**
     * デフォルトモードのモックです。
     * <p>
     * デフォルトモードのモックは指定されていないメソッドが呼び出されると例外をスローしますが，メソッドの呼び出し順は無視します。
     * </p>
     */
    DEFAULT,

    /**
     * Strictモードのモックです。
     * <p>
     * Strictモードのモックは指定されていないメソッドが呼び出されると例外をスローします。
     * 指定されたメソッドであっても，指定された通りの順でメソッドが呼び出されないと例外をスローします。
     * </p>
     */
    STRICT,

    /**
     * Niceモードのモックです。
     * <p>
     * Niceモードのモックは指定されていないメソッド呼び出しが行われても例外をスローしません。
     * </p>
     */
    NICE
}
