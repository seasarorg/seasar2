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
package org.seasar.extension.jdbc;


/**
 * 問い合わせ結果を反復するコンテキストです。
 * 
 * @author koichik
 */
public class IterationContext {

    /**
     * 反復を終了する場合<code>true</code>
     */
    protected boolean exit;

    /**
     * 反復を終了する場合は<code>true</code>を設定します。
     * <p>
     * {@link IterationCallback#iterate(Object, IterationContext)}の中でこのプロパティを<code>true</code>に設定すると、
     * 問い合わせ結果の残りは無視されて反復は終了します。
     * </p>
     * 
     * @param exit
     *            反復を終了する場合は<code>true</code>
     */
    public void setExit(final boolean exit) {
        this.exit = exit;
    }

    /**
     * 反復を終了する場合<code>true</code>を返します。
     * 
     * @return 反復を終了する場合<code>true</code>
     */
    public boolean isExit() {
        return exit;
    }

}
