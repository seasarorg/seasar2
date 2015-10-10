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

import org.seasar.framework.exception.SIllegalStateException;

/**
 * タイムアウトを管理するクラスです。
 * 
 * @author higa
 * 
 */
public class TimeoutTask {

    private final static int ACTIVE = 0;

    private final static int STOPPED = 1;

    private final static int CANCELED = 2;

    private final TimeoutTarget timeoutTarget_;

    private final long timeoutMillis_;

    private final boolean permanent_;

    private long startTime_;

    private int status_ = ACTIVE;

    TimeoutTask(TimeoutTarget timeoutTarget, int timeout, boolean permanent) {
        timeoutTarget_ = timeoutTarget;
        timeoutMillis_ = timeout * 1000L;
        permanent_ = permanent;
        startTime_ = System.currentTimeMillis();
    }

    /**
     * 期限切れかどうかを返します。
     * 
     * @return 期限切れかどうか
     */
    public boolean isExpired() {
        return System.currentTimeMillis() >= startTime_ + timeoutMillis_;
    }

    /**
     * 永続的かどうかを返します。
     * 
     * @return 永続的かどうか
     */
    public boolean isPermanent() {
        return permanent_;
    }

    /**
     * キャンセルされているかどうかを返します。
     * 
     * @return キャンセルされているか
     */
    public boolean isCanceled() {
        return status_ == CANCELED;
    }

    /**
     * キャンセルします。
     */
    public void cancel() {
        status_ = CANCELED;
    }

    /**
     * 止まっているかどうか返します。
     * 
     * @return 止まっているかどうか
     */
    public boolean isStopped() {
        return status_ == STOPPED;
    }

    /**
     * タイマーをとめます。
     */
    public void stop() {
        if (status_ != ACTIVE) {
            throw new SIllegalStateException("ESSR0026", new Object[] { String
                    .valueOf(status_) });
        }
        status_ = STOPPED;
    }

    /**
     * タイマーを再開始します。
     */
    public void restart() {
        status_ = ACTIVE;
        startTime_ = System.currentTimeMillis();
    }

    void expired() {
        timeoutTarget_.expired();
    }
}
