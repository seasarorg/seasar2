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
package org.seasar.extension.dxo.converter;

import java.text.ParseException;
import java.util.Date;

import org.seasar.framework.beans.PropertyDesc;

/**
 * 日時を表すプロパティの情報を保持するクラスです。
 * <p>
 * このクラスは、 複数の文字列型のプロパティから{@link java.util.Date}あるいはその派生クラス、
 * {@link java.util.Calendar}型のプロパティへ変換するための情報を保持します。
 * </p>
 * 
 * @author koichik
 */
public class DatePropertyInfo {

    /** 変換コンテキスト * */
    ConversionContext context;

    /** 日時のフォーマット */
    protected String format;

    /** 変換元となるプロパティの配列 */
    protected PropertyDesc[] propertyDescs;

    /**
     * <code>DatePropertyInfo</code>のインスタンスを構築します。
     * 
     * @param context
     *            変換コンテキスト
     * @param format
     *            フォーマット
     * @param propertyDescs
     *            プロパティ記述子の配列
     */
    public DatePropertyInfo(final ConversionContext context,
            final String format, final PropertyDesc[] propertyDescs) {
        this.context = context;
        this.format = format;
        this.propertyDescs = propertyDescs;
    }

    /**
     * ソースオブジェクトの文字列型のプロパティから{@link java.util.Date}オブジェクトに変換して返します。
     * 変換できなかった場合は<code>null</code>を返します。
     * 
     * @param source
     *            変換元のオブジェクト
     * @return 変換された{@link java.util.Date}オブジェクト
     */
    public Date getValue(final Object source) {
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < propertyDescs.length; ++i) {
            buf.append(propertyDescs[i].getValue(source));
        }
        final String stringValue = new String(buf);
        try {
            return context.getDateFormat(format).parse(stringValue);
        } catch (final ParseException ignore) {
        }
        return null;
    }

}
