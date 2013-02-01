/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo.command;

/**
 * Dxoのメソッドに応じた変換を行うコマンドのインターフェースです。
 * 
 * @author higa
 * @author koichik
 */
public interface DxoCommand {

    /**
     * Dxoのメソッドに応じた変換を行います。
     * <p>
     * Dxoのメソッドは次の形式のいずれかになります。
     * </p>
     * <ul>
     * <li><code><var>Dest convert(Src src)</var></code></li>
     * <li><code><var>void convert(Src src, Dest dest)</var></code></li>
     * </ul>
     * 
     * @param args
     *            Dxoメソッドの引数の配列
     * @return Dxoメソッドの戻り値を返します。
     */
    Object execute(Object[] args);

}
