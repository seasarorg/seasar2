/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package org.seasar.extension.httpsession;

/**
 * セッション状態を管理するインターフェースです。
 * 
 * @author higa
 * 
 */
public interface SessionStateManager {

    /**
     * セッション状態をロードします。
     * 
     * @param sessionId
     *            セッション識別子
     * 
     * @return セッション状態
     */
    SessionState loadState(String sessionId);

    /**
     * セッション状態を格納します。
     * 
     * @param sessionId
     *            セッション識別子
     * @param sessionState
     *            セッション状態
     */
    void updateState(String sessionId, SessionState sessionState);

    /**
     * セッションの状態を削除します。
     * 
     * @param sessionId
     *            セッション識別子
     */
    void removeState(String sessionId);
}
