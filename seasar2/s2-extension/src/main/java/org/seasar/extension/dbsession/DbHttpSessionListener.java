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
package org.seasar.extension.dbsession;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

/**
 * セッションが破棄されたときデータベースに格納されたセッション情報を削除するためのHttpSessionListenerです。
 * 
 * @author higa
 * 
 */
public class DbHttpSessionListener implements HttpSessionListener {

    private DbSessionStateManager sessionStateManager;

    public void sessionCreated(HttpSessionEvent event) {
    }

    public void sessionDestroyed(HttpSessionEvent event) {
        DbSessionStateManager ssm = getSessionStateManager();
        ssm.removeState(event.getSession().getId());
    }

    protected DbSessionStateManager getSessionStateManager() {
        if (sessionStateManager == null) {
            S2Container container = SingletonS2ContainerFactory.getContainer();
            sessionStateManager = (DbSessionStateManager) container
                    .getComponent(DbSessionStateManager.class);
        }
        return sessionStateManager;
    }
}
