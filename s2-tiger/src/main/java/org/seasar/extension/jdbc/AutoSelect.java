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
package org.seasar.extension.jdbc;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Map;

import org.seasar.extension.jdbc.parameter.Parameter;

/**
 * SQLを自動生成する検索です。
 * 
 * @author higa
 * @param <T>
 *            エンティティの型です。
 * 
 */
public interface AutoSelect<T> extends Select<T, AutoSelect<T>> {

    /**
     * 指定のプロパティのみを検索結果に含めます。
     * 
     * @param propertyNames
     *            検索結果に含めるプロパティ名の配列
     * @return このインスタンス自身
     */
    AutoSelect<T> includes(CharSequence... propertyNames);

    /**
     * 指定のプロパティを検索結果から除外します。
     * 
     * @param propertyNames
     *            検索結果から除外するプロパティ名の配列
     * @return このインスタンス自身
     */
    AutoSelect<T> excludes(CharSequence... propertyNames);

    /**
     * 内部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> innerJoin(CharSequence name);

    /**
     * 内部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> innerJoin(CharSequence name, String condition,
            Object... params);

    /**
     * 内部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> innerJoin(CharSequence name, Where condition);

    /**
     * 内部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> innerJoin(CharSequence name, Where... conditions);

    /**
     * 内部結合するプロパティを指定します。
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> innerJoin(CharSequence name, boolean fetch);

    /**
     * 内部結合するプロパティを指定します。
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> innerJoin(CharSequence name, boolean fetch, String condition,
            Object... params);

    /**
     * 内部結合するプロパティを指定します。
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> innerJoin(CharSequence name, boolean fetch, Where condition);

    /**
     * 内部結合するプロパティを指定します。
     * 
     * @param name
     *            内部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> innerJoin(CharSequence name, boolean fetch, Where... conditions);

    /**
     * 左外部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name);

    /**
     * 左外部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, String condition,
            Object... params);

    /**
     * 左外部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, Where condition);

    /**
     * 左外部結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     * @see JoinMeta
     * @see #Join(CharSequence, JoinType)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, Where... conditions);

    /**
     * 左外部結合するプロパティを指定します。
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, boolean fetch);

    /**
     * 左外部結合するプロパティを指定します。
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, boolean fetch,
            String condition, Object... params);

    /**
     * 左外部結合するプロパティを指定します。
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, boolean fetch,
            Where condition);

    /**
     * 左外部結合するプロパティを指定します。
     * 
     * @param name
     *            左外部結合する関連のプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> leftOuterJoin(CharSequence name, boolean fetch,
            Where... conditions);

    /**
     * 結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param joinType
     *            結合タイプ
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType);

    /**
     * 結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param joinType
     *            結合タイプ
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, String condition,
            Object... params);

    /**
     * 結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param joinType
     *            結合タイプ
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, Where condition);

    /**
     * 結合するプロパティを指定します。
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param joinType
     *            結合タイプ
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     * @see #Join(CharSequence, JoinType, boolean)
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, Where... conditions);

    /**
     * 結合するプロパティを指定します。
     * 
     * @param name
     *            <p>
     *            結合するプロパティ名。
     *            </p>
     *            <p>
     *            ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     *            </p>
     *            <p>
     *            <code>aaa.bbb</code>を指定する場合は、先にベースの結合(<code>aaa</code>
     *            )を指定する必要があります。
     *            </p>
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @return このインスタンス自身
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, boolean fetch);

    /**
     * 結合するプロパティを指定します。
     * 
     * @param name
     *            <p>
     *            結合するプロパティ名。
     *            </p>
     *            <p>
     *            ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     *            </p>
     *            <p>
     *            <code>aaa.bbb</code>を指定する場合は、先にベースの結合(<code>aaa</code>
     *            )を指定する必要があります。
     *            </p>
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @param params
     *            付加的な結合条件のパラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, boolean fetch,
            String condition, Object... params);

    /**
     * 結合するプロパティを指定します。
     * 
     * @param name
     *            <p>
     *            結合するプロパティ名。
     *            </p>
     *            <p>
     *            ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     *            </p>
     *            <p>
     *            <code>aaa.bbb</code>を指定する場合は、先にベースの結合(<code>aaa</code>
     *            )を指定する必要があります。
     *            </p>
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param condition
     *            付加的な結合条件
     * @return このインスタンス自身
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, boolean fetch,
            Where condition);

    /**
     * 結合するプロパティを指定します。
     * 
     * @param name
     *            <p>
     *            結合するプロパティ名。
     *            </p>
     *            <p>
     *            ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     *            </p>
     *            <p>
     *            <code>aaa.bbb</code>を指定する場合は、先にベースの結合(<code>aaa</code>
     *            )を指定する必要があります。
     *            </p>
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @param conditions
     *            付加的な結合条件の配列
     * @return このインスタンス自身
     */
    AutoSelect<T> join(CharSequence name, JoinType joinType, boolean fetch,
            Where... conditions);

    /**
     * where句の条件にIdプロパティ(主キー)を指定します。
     * 
     * @param idProperties
     *            主キーの値の並び
     * @return このインスタンス自身
     */
    AutoSelect<T> id(Object... idProperties);

    /**
     * where句の条件にバージョンプロパティを指定します。
     * 
     * @param versionProperty
     *            バージョン
     * @return このインスタンス自身
     */
    AutoSelect<T> version(Object versionProperty);

    /**
     * where句の条件を指定します。
     * 
     * @param conditions
     *            <p>
     *            where句の条件です。
     *            </p>
     *            <p>
     *            Mapのキーにはプロパティ名、値には条件値を指定します。
     *            </p>
     *            <p>
     *            例えば、<code>map.put("id", 1)</code>と指定した場合、 <code>id = ?</code>
     *            という条件になり、バインド変数が1になります。
     *            </p>
     *            <p>
     *            値がnullならwhere句には追加されません。
     *            </p>
     *            <p>
     *            <code>map.put("name", null)</code>と指定した場合、
     *            where句に条件は追加されないという意味です。
     *            </p>
     *            <p>
     *            なぜこのような仕様になっているかというと検索条件の入力画面で 条件を組み立てるときに、入力値があるものだけ
     *            条件に追加するケースを便利に扱うためです。
     *            </p>
     *            <p>
     *            複数の条件が指定された場合は、<code>and</code>で結合されます。
     *            </p>
     *            <p>
     *            結合先のプロパティはキーに<b>結合名.プロパティ名</b>と指定します。 ネストした結合も指定することができます。
     *            </p>
     *            <p>
     *            例えば、Departmentの検索で一対多の関連であるemployeesのnameプロパティを指定する場合、
     *            キーに"employees.name"と指定します。
     *            </p>
     *            <p>
     *            Employeeの検索で多対一であるdepartmentのnameプロパティを指定する場合、
     *            キーに"department.name"と指定します。
     *            </p>
     *            <p>
     *            等価(=)以外の条件を指定する場合、キーにプロパティ名_サフィックスを指定します。
     *            サフィックスによってどのような条件になるのかが決まります。
     *            </p>
     *            <p>
     *            サフィックスにはEQ(=)、NE(&lt;&gt;)、LT(&lt;)、LE(&lt;=)、
     *            GT(&gt;)、GE(&gt;=)、IN(in)、NOT_IN(not in)、LIKE(like '?')、
     *            STARTS(like '?%')、ENDS(like '%?')、CONTAINS(like '%?%')、
     *            IS_NULL(is null)、IS_NOT_NULL(is not null)を指定することができます。
     *            </p>
     *            <p>
     *            IN、NOT_INの場合、値には配列、リストを指定します。 値がnullもしくは要素の数がゼロの場合条件には追加されません。
     *            </p>
     *            <p>
     *            IS_NULL、IS_NOT_NULLの場合、値にはBooleanを指定します。 例えば、
     *            <code>map.put("hoge_IS_NOT_NULL", true)</code>の場合、 条件は
     *            <code>hoge is not null</code>になります。
     *            値がnullもしくはfalseだった場合、条件には追加されません。
     *            </p>
     * 
     * @return このインスタンス自身
     */
    AutoSelect<T> where(Map<String, ? extends Object> conditions);

    /**
     * where句の条件を指定します。
     * 
     * @param criteria
     *            クライテリア
     * @param params
     *            パラメータの配列
     *            <p>
     *            パラメータの配列の要素が{@link Date}、{@link Calendar}のいずれか場合、
     *            {@link Parameter}に定義されたメソッドによりパラメータの時制を指定できます。
     *            </p>
     *            <p>
     *            パラメータの配列の要素が{@link String}、<code>ｂyte[]</code>、
     *            {@link Serializable}のいずれかの場合、{@link Parameter}
     *            に定義されたメソッドによりパラメータをラージオブジェクトとして扱えます。
     *            </p>
     * @return このインスタンス自身
     * @see Parameter
     */
    AutoSelect<T> where(String criteria, Object... params);

    /**
     * where句の条件を指定します。
     * 
     * @param where
     *            where句のビルダー
     * 
     * @return このインスタンス自身
     */
    AutoSelect<T> where(Where where);

    /**
     * where句の条件を指定します。
     * 
     * @param wheres
     *            where句のビルダーの並び
     * 
     * @return このインスタンス自身
     */
    AutoSelect<T> where(Where... wheres);

    /**
     * ソート順を指定します。
     * 
     * @param orderBy
     * @return このインスタンス自身
     */
    AutoSelect<T> orderBy(String orderBy);

    /**
     * ソート順を指定します。
     * 
     * @param orderByItems
     * @return このインスタンス自身
     */
    AutoSelect<T> orderBy(OrderByItem... orderByItems);

    /**
     * FOR UPDATEを追加します。
     * 
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdate();

    /**
     * FOR UPDATEを追加します。
     * 
     * @param propertyNames
     *            ロック対象のプロパティ名の並び
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdate(CharSequence... propertyNames);

    /**
     * FOR UPDATE NOWAITを追加します。
     * 
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdateNowait();

    /**
     * FOR UPDATE NOWAITを追加します。
     * 
     * @param propertyNames
     *            ロック対象のプロパティ名の並び
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdateNowait(CharSequence... propertyNames);

    /**
     * FOR UPDATE WAITを追加します。
     * 
     * @param seconds
     *            ロックを獲得できるまでの最大待機時間(秒単位)
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdateWait(int seconds);

    /**
     * FOR UPDATE WAITを追加します。
     * 
     * @param seconds
     *            ロックを獲得できるまでの最大待機時間(秒単位)
     * @param propertyNames
     *            ロック対象のプロパティ名の並び
     * @return このインスタンス自身
     * @throws UnsupportedOperationException
     *             DBMSがこの操作をサポートしていない場合
     */
    AutoSelect<T> forUpdateWait(int seconds, CharSequence... propertyNames);

    /**
     * EAGERフェッチするプロパティを追加します。
     * 
     * @param propertyNames
     *            EAGERフェッチするプロパティ名の並び
     * @return このインスタンス自身
     */
    AutoSelect<T> eager(CharSequence... propertyNames);

    /**
     * ヒントを設定します。
     * 
     * @param hint
     *            ヒント
     * @return このインスタンス自身
     */
    AutoSelect<T> hint(String hint);

    /**
     * SELECT COUNT(*)の結果である行数を返します。
     * 
     * @return SELECT COUNT(*)の結果である行数
     */
    long getCount();

}
