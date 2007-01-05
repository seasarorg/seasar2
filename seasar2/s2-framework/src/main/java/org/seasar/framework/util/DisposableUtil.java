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
package org.seasar.framework.util;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedList;

import org.seasar.framework.exception.SQLRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * @author koichik
 * 
 */
public class DisposableUtil {

    protected static final LinkedList disposables = new LinkedList();

    public static synchronized void add(final Disposable disposable) {
        disposables.add(disposable);
    }

    public static synchronized void remove(final Disposable disposable) {
        disposables.remove(disposable);
    }

    public static synchronized void dispose() {
        while (!disposables.isEmpty()) {
            final Disposable disposable = (Disposable) disposables.removeLast();
            try {
                disposable.dispose();
            } catch (final Throwable t) {
                t.printStackTrace(); // must not use Logger.
            }
        }
        disposables.clear();
        Logger.dispose();
    }

    public static synchronized void deregisterAllDrivers() {
        try {
            for (Enumeration e = DriverManager.getDrivers(); e
                    .hasMoreElements();) {
                DriverManager.deregisterDriver((Driver) e.nextElement());
            }
        } catch (SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }
}
