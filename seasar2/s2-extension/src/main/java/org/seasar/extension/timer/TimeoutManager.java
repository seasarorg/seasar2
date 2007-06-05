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
package org.seasar.extension.timer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.seasar.framework.util.SLinkedList;

/**
 * Timerを扱うクラスです。
 * 
 * @author higa
 * 
 */
public class TimeoutManager implements Runnable {

    protected static final TimeoutManager instance = new TimeoutManager();

    protected Thread thread;

    protected final SLinkedList timeoutTaskList = new SLinkedList();

    private TimeoutManager() {
    }

    public static TimeoutManager getInstance() {
        return instance;
    }

    public synchronized void start() {
        if (thread == null) {
            thread = new Thread(this, "Seasar2-TimeoutManager");
            thread.setDaemon(true);
            thread.start();
        }
    }

    public synchronized void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public synchronized void clear() {
        timeoutTaskList.clear();
    }

    public synchronized TimeoutTask addTimeoutTarget(
            final TimeoutTarget timeoutTarget, final int timeout,
            final boolean permanent) {
        final TimeoutTask task = new TimeoutTask(timeoutTarget, timeout,
                permanent);
        timeoutTaskList.addLast(task);
        if (timeoutTaskList.size() == 1) {
            start();
        }
        return task;
    }

    public synchronized int getTimeoutTaskCount() {
        return timeoutTaskList.size();
    }

    public void run() {
        for (;;) {
            final List expiredTask = getExpiredTask();
            for (final Iterator it = expiredTask.iterator(); it.hasNext();) {
                final TimeoutTask task = (TimeoutTask) it.next();
                task.expired();
                if (task.isPermanent()) {
                    task.restart();
                }
            }
            if (stopIfLeisure()) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException ignore) {
            }
        }
    }

    protected synchronized List getExpiredTask() {
        final List expiredTask = new ArrayList();
        try {
            if (timeoutTaskList == null || timeoutTaskList.isEmpty()) {
                return expiredTask;
            }
        } catch (NullPointerException e) {
            return expiredTask;
        }
        for (SLinkedList.Entry e = timeoutTaskList.getFirstEntry(); e != null; e = e
                .getNext()) {
            final TimeoutTask task = (TimeoutTask) e.getElement();
            if (task.isCanceled()) {
                e.remove();
                continue;
            }
            if (task.isStopped()) {
                continue;
            }
            if (task.isExpired()) {
                expiredTask.add(task);
                if (!task.isPermanent()) {
                    e.remove();
                }
            }
        }
        return expiredTask;
    }

    protected synchronized boolean stopIfLeisure() {
        try {
            if (timeoutTaskList == null || timeoutTaskList.isEmpty()) {
                thread = null;
                return true;
            }
        } catch (NullPointerException e) {
            return true;
        }
        return false;
    }

}
