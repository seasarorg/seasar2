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
package org.seasar.extension.httpsession.impl;

import java.util.HashMap;

import org.seasar.extension.httpsession.SessionState;
import org.seasar.extension.unit.S2TestCase;

/**
 * @author higa
 * 
 */
public class DbSessionStateManagerImplTest extends S2TestCase {

    public void setUp() {
        include("j2ee.dicon");
    }

    /**
     * @throws Exception
     */
    public void testLoadState() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        SessionState sessionState = manager.loadState("hoge");
        assertNotNull(sessionState);
    }

    /**
     * @throws Exception
     */
    public void testUpdateState_insertTx() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        long time = System.currentTimeMillis();
        SessionState sessionState = new SessionState(new HashMap(), time);
        sessionState.setAttribute("aaa", new Integer(1));
        sessionState.setAttribute("bbb", new Integer(2));
        Thread.sleep(100);
        manager.updateState("hoge", sessionState);

        SessionState sessionState2 = manager.loadState("hoge");
        assertEquals(new Integer(1), sessionState2.getAttribute("aaa"));
        assertEquals(new Integer(2), sessionState2.getAttribute("bbb"));
        assertTrue(sessionState2.getLastAccessedTime() > time);
    }

    /**
     * @throws Exception
     */
    public void testUpdateState_updateTx() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        long time = System.currentTimeMillis();
        SessionState sessionState = new SessionState(new HashMap(), time);
        sessionState.setAttribute("aaa", new Integer(1));
        sessionState.setAttribute("bbb", new Integer(2));
        Thread.sleep(100);
        manager.updateState("hoge", sessionState);

        SessionState sessionState2 = manager.loadState("hoge");
        sessionState2.setAttribute("aaa", new Integer(3));
        sessionState2.setAttribute("bbb", new Integer(4));
        assertTrue(sessionState2.getLastAccessedTime() > time);
        Thread.sleep(100);
        manager.updateState("hoge", sessionState2);

        SessionState sessionState3 = manager.loadState("hoge");
        assertEquals(new Integer(3), sessionState3.getAttribute("aaa"));
        assertEquals(new Integer(4), sessionState3.getAttribute("bbb"));
        assertTrue(sessionState3.getLastAccessedTime() > sessionState2
                .getLastAccessedTime());
    }

    /**
     * @throws Exception
     */
    public void testUpdateState_deleteTx() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        long time = System.currentTimeMillis();
        SessionState sessionState = new SessionState(new HashMap(), time);
        sessionState.setAttribute("aaa", new Integer(1));
        sessionState.setAttribute("bbb", new Integer(2));
        Thread.sleep(100);
        manager.updateState("hoge", sessionState);

        SessionState sessionState2 = manager.loadState("hoge");
        sessionState2.setAttribute("aaa", null);
        sessionState2.setAttribute("bbb", null);
        assertTrue(sessionState2.getLastAccessedTime() > time);
        Thread.sleep(100);
        manager.updateState("hoge", sessionState2);

        SessionState sessionState3 = manager.loadState("hoge");
        assertNull(sessionState3.getAttribute("aaa"));
        assertNull(sessionState3.getAttribute("bbb"));
        assertTrue(sessionState3.getLastAccessedTime() > sessionState2
                .getLastAccessedTime());
    }

    /**
     * @throws Exception
     */
    public void testRemoveStateTx() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        long time = System.currentTimeMillis();
        SessionState sessionState = new SessionState(new HashMap(), time);
        sessionState.setAttribute("aaa", new Integer(1));
        sessionState.setAttribute("bbb", new Integer(2));
        Thread.sleep(100);
        manager.updateState("hoge", sessionState);
        manager.removeState("hoge");

        SessionState sessionState2 = manager.loadState("hoge");
        assertNull(sessionState2.getAttribute("aaa"));
        assertNull(sessionState2.getAttribute("bbb"));
        assertTrue(sessionState2.getLastAccessedTime() > time);
    }

    /**
     * 
     * @throws Exception
     */
    public void testUpdateStateBatchDisabled_updateTx() throws Exception {
        DbSessionStateManagerImpl manager = new DbSessionStateManagerImpl(
                getDataSource());
        manager.setBatchUpdateDisabled(true);
        long time = System.currentTimeMillis();
        SessionState sessionState = new SessionState(new HashMap(), time);
        sessionState.setAttribute("aaa", new Integer(1));
        sessionState.setAttribute("bbb", new Integer(2));
        Thread.sleep(100);
        manager.updateState("hoge", sessionState);

        SessionState sessionState2 = manager.loadState("hoge");
        sessionState2.setAttribute("aaa", new Integer(3));
        sessionState2.setAttribute("bbb", new Integer(4));
        assertTrue(sessionState2.getLastAccessedTime() > time);
        Thread.sleep(100);
        manager.updateState("hoge", sessionState2);

        SessionState sessionState3 = manager.loadState("hoge");
        assertEquals(new Integer(3), sessionState3.getAttribute("aaa"));
        assertEquals(new Integer(4), sessionState3.getAttribute("bbb"));
        assertTrue(sessionState3.getLastAccessedTime() > sessionState2
                .getLastAccessedTime());
    }
}
