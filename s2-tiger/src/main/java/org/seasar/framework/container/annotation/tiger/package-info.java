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
/**
 * diconファイルの設定に相当するアノテーション及び列挙型を提供します。
 * <p>
 * 以下にdiconファイルの要素とアノテーションの対応を示します。
 * </p>
 * <table border="1">
 * <tr>
 * <th>要素</th>
 * <th>アノテーション</th>
 * </tr>
 * <tr>
 * <td><code>&lt;component&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.Component}</td>
 * </tr>
 * <tr>
 * <td><code>&lt;property&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.Binding}</td>
 * </tr>
 * <tr>
 * <td><code>&lt;initMethod&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.InitMethod}</td>
 * </tr>
 * <tr>
 * <td><code>&lt;destroyMethod&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.DestroyMethod}</td>
 * </tr>
 * <tr>
 * <td><code>&lt;aspect&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.Aspect}</td>
 * </tr>
 * <tr>
 * <td><code>&lt;interType&gt;</code></td>
 * <td>{@link org.seasar.framework.container.annotation.tiger.InterType}</td>
 * </tr>
 * </table>
 */
package org.seasar.framework.container.annotation.tiger;

