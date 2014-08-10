/*
 * Copyright 2004-2014 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

/**
 * 論理パスから物理パスを取得するためのインターフェースです。
 * <p>
 * <dl>
 * <dt>論理パス</dt>
 * <dd>コンテナの生成時やインクルード時に、 指定されたパス名です。
 * <dt>物理パス</dt>
 * <dd>{@link ResourceResolver}に渡されるパスです。</dd>
 * </dl>
 * </p>
 * 
 * @author koichik
 * @author jundu
 */
public interface PathResolver {

    /**
     * 論理パスを、 物理パスに変換して返します。
     * <p>
     * <code>path</code>に指定するパス名がインクルードで指定されたパスの場合、 <code>context</code>はインクルード先となる親コンテナの設定ファイルの物理パスです。
     * ルートコンテナの作成時などインクルード先の親コンテナがない場合や、 インクルード先の親コンテナが設定ファイルを持っていない場合は、
     * <code>context</code>は<code>null</code>です。
     * </p>
     * 
     * @param context
     *            コンテキスト
     * @param path
     *            論理パス
     * @return 物理パス
     */
    String resolvePath(String context, String path);
}
