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

    /**
     * シングルトンのためのインスタンスです。
     */
    protected static final TimeoutManager instance = new TimeoutManager();

    /**
     * Timerのための{@link Thread}です。
     */
    protected Thread thread;

    /**
     * {@link TimeoutTask}管理用のリストです。
     */
    protected final SLinkedList timeoutTaskList = new SLinkedList();

    private TimeoutManager() {
    }

    /**
     * シングルトン用のインスタンスを返します。
     * 
     * @return シングルトン用のインスタンス
     */
    public static TimeoutManager getInstance() {
        return instance;
    }

    /**
     * 処理を開始します。
     */
    public synchronized void start() {
        if (thread == null) {
            thread = new Thread(this, "Seasar2-TimeoutManager");
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * 処理を停止します。
     */
    public synchronized void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    /**
     * スレッドに割り込みを行い、終了するまで待機します。
     * 
     * @param timeoutMillis
     *            待機する時間(ミリ秒単位)
     * @return スレッドが終了した場合は<code>true</code>
     * @throws InterruptedException
     *             待機中に割り込まれた場合
     */
    public boolean stop(long timeoutMillis) throws InterruptedException {
        Thread thread = this.thread;
        synchronized (this) {
            if (thread == null) {
                return true;
            }
            this.thread = null;
        }
        thread.interrupt();
        thread.join(timeoutMillis);
        return !thread.isAlive();
    }

    /**
     * 管理している {@link TimeoutTask}をクリアします。
     */
    public synchronized void clear() {
        timeoutTaskList.clear();
    }

    /**
     * {@link TimeoutTarget}を追加します。
     * 
     * @param timeoutTarget
     * @param timeout
     * @param permanent
     * @return {@link TimeoutTask}
     */
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

    /**
     * 管理している {@link TimeoutTask}の数を返します。
     * 
     * @return 管理している {@link TimeoutTask}の数
     */
    public synchronized int getTimeoutTaskCount() {
        return timeoutTaskList.size();
    }

    public void run() {
        boolean interrupted = false;
        for (;;) {
            final List expiredTask = getExpiredTask();
            for (final Iterator it = expiredTask.iterator(); it.hasNext();) {
                final TimeoutTask task = (TimeoutTask) it.next();
                task.expired();
                if (task.isPermanent()) {
                    task.restart();
                }
            }
            if (interrupted || thread.isInterrupted() || stopIfLeisure()) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                interrupted = true;
            }
        }
    }

    /**
     * 期限の切れた {@link TimeoutTask}のリストを返します。
     * 
     * @return 期限の切れた {@link TimeoutTask}のリスト
     */
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

    /**
     * 管理しているタスクが無いなら処理を停止します。
     * 
     * @return 停止したかどうか
     */
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
