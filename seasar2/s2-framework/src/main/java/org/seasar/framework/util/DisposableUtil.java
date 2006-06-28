/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author koichik
 * 
 */
public class DisposableUtil {

    protected static final Set disposables = new LinkedHashSet();

    public static synchronized void add(final Disposable disposable) {
        disposables.add(disposable);
    }

    public static synchronized void remove(final Disposable disposable) {
        disposables.remove(disposable);
    }

    public static synchronized void dispose() {
        for (final Iterator it = disposables.iterator(); it.hasNext();) {
            final Disposable disposable = (Disposable) it.next();
            try {
                disposable.dispose();
            } catch (final Throwable t) {
                t.printStackTrace(); // must not use Logger.
            }
        }
        disposables.clear();
    }
}
