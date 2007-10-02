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
package org.seasar.extension.jdbc;

import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.exception.OrderByNotFoundRuntimeException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;

/**
 * SQLを自動生成する検索です。
 * 
 * @author higa
 * @param <T>
 *            エンティティの型です。
 * 
 */
public interface AutoSelect<T> {

    /**
     * =です。
     */
    int EQ = 1;

    /**
     * &lt;&gt;です。
     */
    int NE = 2;

    /**
     * &ltです。
     */
    int LT = 3;

    /**
     * &lt=です。
     */
    int LE = 4;

    /**
     * &gtです。
     */
    int GT = 5;

    /**
     * &gt=です。
     */
    int GE = 6;

    /**
     * inです。
     */
    int IN = 7;

    /**
     * not inです。
     */
    int NOT_IN = 8;

    /**
     * likeです。
     */
    int LIKE = 9;

    /**
     * like '?%'です。
     */
    int STARTS = 10;

    /**
     * like '%?'です。
     */
    int ENDS = 11;

    /**
     * like '%?%'です。
     */
    int CONTAINS = 12;

    /**
     * is nullです。
     */
    int IS_NULL = 13;

    /**
     * is not nullです。
     */
    int IS_NOT_NULL = 14;

    /**
     * 検索を呼び出すクラスを設定します。デフォルトは {@link AutoSelect}の実装クラスです。
     * 
     * @param callerClass
     *            検索を呼び出すクラス
     * @return 自動検索
     */
    AutoSelect<T> callerClass(Class<?> callerClass);

    /**
     * 検索を呼び出すメソッド名を設定します。デフォルトはgetResultListあるいはgetSingleResultになります。
     * 
     * @param callerMethodName
     *            検索を呼び出すメソッド名
     * @return 自動検索
     */
    AutoSelect<T> callerMethodName(String callerMethodName);

    /**
     * 最大行数を設定します。
     * 
     * @param maxRows
     *            最大行数
     * @return 自動検索
     */
    AutoSelect<T> maxRows(int maxRows);

    /**
     * フェッチ数を設定します。
     * 
     * @param fetchSize
     *            フェッチ数
     * @return 自動検索
     */
    AutoSelect<T> fetchSize(int fetchSize);

    /**
     * クエリタイムアウトの秒数を設定します。
     * 
     * @param queryTimeout
     *            クエリタイムアウトの秒数
     * @return 自動検索
     */
    AutoSelect<T> queryTimeout(int queryTimeout);

    /**
     * リミットを設定します。
     * 
     * @param limit
     *            リミット
     * @return 自動検索
     */
    AutoSelect<T> limit(int limit);

    /**
     * オフセットを設定します。
     * 
     * @param offset
     *            オフセット
     * @return 自動検索
     */
    AutoSelect<T> offset(int offset);

    /**
     * <p>
     * 結合するプロパティを指定します。
     * </p>
     * <p>
     * デフォルトの結合タイプは左外部結合で、 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @return 自動検索
     * @see JoinMeta
     * @see #join(String, JoinType)
     */
    AutoSelect<T> join(String name);

    /**
     * <p>
     * 結合するプロパティを指定します。
     * </p>
     * <p>
     * 指定したエンティティはフェッチされます。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param joinType
     *            結合タイプ
     * @return 自動検索
     * @see #join(String, JoinType, boolean)
     */
    AutoSelect<T> join(String name, JoinType joinType);

    /**
     * <p>
     * 結合するプロパティを指定します。
     * </p>
     * <p>
     * デフォルトの結合タイプは左外部結合です。
     * </p>
     * 
     * @param name
     *            結合するプロパティ名
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @return 自動検索
     * @see #join(String, JoinType, boolean)
     */
    AutoSelect<T> join(String name, boolean fetch);

    /**
     * <p>
     * 結合するプロパティを指定します。
     * </p>
     * 
     * @param name
     *            <p>
     *            結合するプロパティ名。
     *            </p>
     *            <p>
     *            ネストしている場合は、<code>aaa.bbb</code>のように.で区切ります。
     *            </p>
     *            <p>
     *            <code>aaa.bbb</code>を指定する場合は、先にベースの結合(<code>aaa</code>)を指定する必要があります。
     *            </p>
     * @param joinType
     *            結合タイプ
     * @param fetch
     *            関連するエンティティをフェッチするかどうか。
     * @return 自動検索
     */
    AutoSelect<T> join(String name, JoinType joinType, boolean fetch);

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
     *            例えば、<code>map.put("id", 1)</code>と指定した場合、
     *            <code>id = ?</code>という条件になり、バインド変数が1になります。
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
     *            IS_NULL、IS_NOT_NULLの場合、値にはBooleanを指定します。 例えば、<code>map.put("hoge_IS_NOT_NULL", true)</code>の場合、
     *            条件は<code>hoge is not null</code>になります。
     *            値がnullもしくはfalseだった場合、条件には追加されません。
     *            </p>
     * 
     * @return 自動検索
     */
    AutoSelect<T> where(Map<String, Object> conditions);

    /**
     * 検索してベースオブジェクトのリストを返します。
     * 
     * @return
     * <p>
     * ベースオブジェクトのリスト。
     * </p>
     * <p>
     * 1件も対象がないときはnullではなく空のリストを返します。
     * </p>
     * @throws OrderByNotFoundRuntimeException
     *             ページング処理で<code>order by</code>が見つからない場合
     */
    List<T> getResultList() throws OrderByNotFoundRuntimeException;

    /**
     * 検索してベースオブジェクトを返します。
     * 
     * @return
     * <p>
     * ベースオブジェクト。
     * </p>
     * <p>
     * 1件も対象がないときはnullを返します。
     * </p>
     * @throws SNonUniqueResultException
     *             検索結果がユニークでない場合。
     */
    T getSingleResult() throws SNonUniqueResultException;
}