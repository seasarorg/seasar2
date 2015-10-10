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
package oracle.sql;

import java.sql.Timestamp;

/**
 * Oracle JDBC ドライバが提供する{@literal DATE}型のダミーです。
 * <p>
 * このクラスは再配布が許可されていないOracle JDBC ドライバに依存しないでビルドするために用意されています。
 * S2-TigerのJarファイルには含まれないように{@literal pom.xml}で設定されます。
 * </p>
 * 
 * @author koichik
 */
public class DATE {

    /** タイムスタンプ */
    protected final Timestamp timestamp;

    /**
     * インスタンスを構築します。
     * 
     * @param timestamp
     *            タイムスタンプ
     */
    public DATE(final Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * タイムスタンプを返します。
     * 
     * @return タイムスタンプ
     */
    public Timestamp timestampValue() {
        return timestamp;
    }

}
