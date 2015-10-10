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
package org.seasar.framework.message;

/**
 * プロパティファイルに登録されているメッセージを扱うためのインターフェースです。
 * 
 * @author shot
 */
public interface MessageResourceBundle {

    /**
     * キーに対応するメッセージを返します。
     * 
     * @param key
     * @return キーに対応するメッセージ
     */
    public String get(String key);

    /**
     * 親を返します。
     * 
     * @return 親
     */
    public MessageResourceBundle getParent();

    /**
     * 親を設定します。
     * 
     * @param parent
     */
    public void setParent(MessageResourceBundle parent);

}
