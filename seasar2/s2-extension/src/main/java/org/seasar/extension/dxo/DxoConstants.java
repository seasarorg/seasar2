/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo;

/**
 * S2Dxoで使用する定数です。
 * 
 * @author Satsohi Kimura
 * @author koichik
 */
public interface DxoConstants {

    /**
     * <code>Date</code>と<code>String</code>の変換フォーマットを指定する定数アノテーションの名前です。
     */
    String DATE_PATTERN = "DATE_PATTERN";

    /**
     * <code>Time</code>と<code>String</code>の変換フォーマットを指定する定数アノテーションの名前です。
     */
    String TIME_PATTERN = "TIME_PATTERN";

    /**
     * <code>Timestamp</code>と<code>String</code>の変換フォーマットを指定する定数アノテーションの名前です。
     */
    String TIMESTAMP_PATTERN = "TIMESTAMP_PATTERN";

    /**
     * 変換ルールを指定する定数アノテーションの名前です。
     */
    String CONVERSION_RULE = "CONVERSION_RULE";

    /**
     * 変換元プロパティの値が<code>null</code>の場合に変換先プロパティに値を設定しないことを指定する定数アノテーションの名前です。
     */
    String EXCLUDE_NULL = "EXCLUDE_NULL";

    /**
     * 変換元プロパティのprefixを指定する定数アノテーションの名前です。
     */
    String SOURCE_PREFIX = "SOURCE_PREFIX";

    /**
     * 変換先プロパティのprefixを指定する定数アノテーションの名前です。
     */
    String DEST_PREFIX = "DEST_PREFIX";

}
