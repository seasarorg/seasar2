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
package org.seasar.framework.jpa.util;

import java.util.EventListener;

/**
 * クラスローダイベントは、クラスローダがクラスを定義する度にトリガされます。
 * <p>
 * クラスが定義される度に通知を出すように、<code>ClassLoaderListener</code>をソースクラスローダに登録できます。
 * </p>
 * 
 * @author koichik
 */
public interface ClassLoaderListener extends EventListener {

    /**
     * クラスの定義時に呼び出されます。
     * 
     * @param event
     *            イベントソースおよび定義されたクラスを記述する<code>ClassLoaderEvent</code>オブジェクト
     */
    void classFinded(ClassLoaderEvent event);

}
