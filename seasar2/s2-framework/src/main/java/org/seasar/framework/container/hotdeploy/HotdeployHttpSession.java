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
package org.seasar.framework.container.hotdeploy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.exception.SessionObjectNotSerializableRuntimeException;
import org.seasar.framework.log.Logger;

/**
 * HOT deploy用の{@link HttpSession}です。
 * 
 * @author koichik
 */
public class HotdeployHttpSession implements HttpSession {

    private static final Logger logger = Logger
            .getLogger(HotdeployHttpSession.class);

    /** このインスタンスを所有する{@link HttpServletRequest}です。 */
    protected final HotdeployHttpServletRequest request;

    /** オリジナルの{@link HttpSession}です。 */
    protected final HttpSession originalSession;

    /** セッションオブジェクトの{@link Map}です。 */
    protected final Map attributes = new HashMap();

    /** このセッションオブジェクトが有効なら<code>true</code>です。 */
    protected boolean active = true;

    /**
     * インスタンスを構築します。
     * 
     * @param request
     *            このインスタンスを所有する{@link HttpServletRequest}
     * @param originalSession
     *            オリジナルの{@link HttpSession}
     */
    public HotdeployHttpSession(final HttpSession originalSession) {
        this(null, originalSession);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param request
     *            このインスタンスを所有する{@link HttpServletRequest}
     * @param originalSession
     *            オリジナルの{@link HttpSession}
     */
    public HotdeployHttpSession(final HotdeployHttpServletRequest request,
            final HttpSession originalSession) {
        this.request = request;
        this.originalSession = originalSession;
    }

    /**
     * セッションオブジェクトを{@link HttpSession}に設定します。
     */
    public void flush() {
        if (active) {
            for (Iterator it = attributes.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();
                try {
                    originalSession.setAttribute((String) entry.getKey(),
                            new SerializedObjectHolder(entry.getValue()));
                } catch (final IllegalStateException e) {
                    return;
                } catch (final Exception e) {
                    logger.log("ESSR0017", new Object[] { e }, e);
                }
            }
        }
    }

    public Object getAttribute(final String name) {
        assertActive();
        if (attributes.containsKey(name)) {
            return attributes.get(name);
        }
        Object value = originalSession.getAttribute(name);
        if (value instanceof SerializedObjectHolder) {
            value = ((SerializedObjectHolder) value)
                    .getDeserializedObject(name);
            if (value != null) {
                attributes.put(name, value);
            } else {
                originalSession.removeAttribute(name);
            }
        }
        return value;
    }

    public void setAttribute(final String name, final Object value) {
        assertActive();
        if (value == null) {
            originalSession.setAttribute(name, value);
            return;
        }
        if (!(value instanceof Serializable)) {
            throw new SessionObjectNotSerializableRuntimeException(value
                    .getClass());
        }
        attributes.put(name, value);
        originalSession.setAttribute(name, value);
    }

    public void removeAttribute(final String name) {
        attributes.remove(name);
        originalSession.removeAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return originalSession.getAttributeNames();
    }

    public long getCreationTime() {
        return originalSession.getCreationTime();
    }

    public String getId() {
        return originalSession.getId();
    }

    public long getLastAccessedTime() {
        return originalSession.getLastAccessedTime();
    }

    public int getMaxInactiveInterval() {
        return originalSession.getMaxInactiveInterval();
    }

    public ServletContext getServletContext() {
        return originalSession.getServletContext();
    }

    public HttpSessionContext getSessionContext() {
        return originalSession.getSessionContext();
    }

    public Object getValue(final String name) {
        return getAttribute(name);
    }

    public String[] getValueNames() {
        return originalSession.getValueNames();
    }

    public void invalidate() {
        originalSession.invalidate();
        if (request != null) {
            request.invalidateSession();
        }
        active = false;
    }

    public boolean isNew() {
        return originalSession.isNew();
    }

    public void putValue(final String name, final Object value) {
        setAttribute(name, value);
    }

    public void removeValue(final String name) {
        removeAttribute(name);
    }

    public void setMaxInactiveInterval(final int interval) {
        originalSession.setMaxInactiveInterval(interval);
    }

    /**
     * シリアライズされたセッションオブジェクトを保持するクラスです。
     * 
     * @author koichik
     */
    public static class SerializedObjectHolder implements Serializable {

        private static final long serialVersionUID = 1L;

        /** セッションオブジェクトをシリアライズしたバイト列です。 */
        protected byte[] bytes;

        /**
         * インスタンスを構築します。
         * 
         * @param sessionObject
         *            {@link HttpSession}に保持するオブジェクト
         */
        public SerializedObjectHolder(final Object sessionObject) {
            try {
                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(sessionObject);
                oos.close();
                bytes = baos.toByteArray();
            } catch (final NotSerializableException e) {
                throw new SessionObjectNotSerializableRuntimeException(e);
            } catch (final IOException e) {
                throw new IORuntimeException(e);
            }
        }

        /**
         * セッションオブジェクトをデシリアライズして返します。
         * <p>
         * 返されるオブジェクトは現在のスレッドのコンテキストクラスローダからロードされたクラスのインスタンスです。
         * </p>
         * 
         * @param name
         *            オブジェクトの名前
         * @return デシリアライズされたオブジェクト
         */
        public Object getDeserializedObject(final String name) {
            try {
                return HotdeployUtil.deserializeInternal(bytes);
            } catch (final Exception e) {
                logger.log("ISSR0008", new Object[] { name }, e);
                return null;
            }
        }

    }

    /**
     * このセッションオブジェクトが有効であることをチェックします。
     * 
     * @throws IllegalStateException
     *             このセッションが無効の場合
     */
    protected void assertActive() {
        if (!active) {
            throw new IllegalStateException("session invalidated");
        }
    }
}
