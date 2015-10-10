/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
 * トランザクションの振る舞いです。
 * 
 * @author taedium
 * 
 */
public enum TxBehaviorType {
    /**
     * トランザクションを開始し終了時はロールバックすることを表します。
     */
    ROLLBACK,

    /**
     * トランザクションを開始し終了時はコミットすることを表します。
     * <p>
     * ただし、コミットはテストが成功する場合にのみ行います。テストが失敗する場合はロールバックを行います。
     * </p>
     */
    COMMIT,

    /**
     * トランザクションを開始しないことを表します。
     */
    NONE
}
