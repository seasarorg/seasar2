/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.extension.dbcp.impl;

import org.seasar.extension.timer.TimeoutManager;
import org.seasar.framework.util.DriverManagerUtil;

/**
 * S2DBCPが利用した環境の後片付けを行うクラスです。
 * 
 * <ul>
 * <li>このクラスをロードしたクラスローダによってロードされたJDBCドライバの登録を解除します。</li>
 * <li>{@link TimeoutManager}タスクを停止します．</li>
 * </ul>
 * 
 * @author koichik
 */
public class S2DbcpCleaner {
    /**
     * S2DBCPが利用した環境の後片付けを行います。
     */
    public void cleanup() {
        DriverManagerUtil.deregisterAllDrivers();
        try {
            TimeoutManager.getInstance().stop(1000);
        } catch (InterruptedException ignore) {
        }
    }
}
