/*
 * Copyright 2004-2011 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.autoregister;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.StringUtil;

/**
 * AutoNamingの抽象クラスです。
 * 
 * @author koichik
 */
public abstract class AbstractAutoNaming implements AutoNaming {

    /**
     * Implのsuffixです。
     */
    protected static final String IMPL = "Impl";

    /**
     * Beanのsuffixです。
     */
    protected static final String BEAN = "Bean";

    /**
     * decapitalizeするかどうか。
     */
    protected boolean decapitalize = true;

    /**
     * カスタマイズされた名前です。
     */
    protected Map customizedNames = new HashMap();

    /**
     * 置換するルールです。
     */
    protected Map replaceRules = new LinkedHashMap();

    /**
     * デフォルトのコンストラクタです。
     */
    public AbstractAutoNaming() {
        addIgnoreClassSuffix(IMPL);
        addIgnoreClassSuffix(BEAN);
    }

    /**
     * AutoNamingのルールにあわない場合に、FQCNに対するコンポーネント名を設定します。
     * 
     * @param fqcn
     * @param name
     */
    public void setCustomizedName(final String fqcn, final String name) {
        customizedNames.put(fqcn, name);
    }

    /**
     * コンポーネント名から取り除くサフィックスを追加します。 例えば、Implを追加した場合、hogeImplは、hogeという名前になります。
     * 
     * @param classSuffix
     */
    public void addIgnoreClassSuffix(final String classSuffix) {
        addReplaceRule(classSuffix + "$", "");
    }

    /**
     * コンポーネント名を置き換えるためのルールを追加します。 reqexの正規表現に一致した部分をreplacementで置き換えます。
     * 
     * @param regex
     * @param replacement
     */
    public void addReplaceRule(final String regex, final String replacement) {
        replaceRules.put(Pattern.compile(regex), replacement);
    }

    /**
     * コンポーネント名を置き換えるためのルールをクリアします。
     */
    public void clearReplaceRule() {
        customizedNames.clear();
        replaceRules.clear();
    }

    /**
     * コンポーネント名をデキャピタライズするかどうかを設定します。デフォルトはtrueです。
     * 
     * @param decapitalize
     */
    public void setDecapitalize(final boolean decapitalize) {
        this.decapitalize = decapitalize;
    }

    public String defineName(final String packageName,
            final String shortClassName) {
        final String customizedName = getCustomizedName(packageName,
                shortClassName);
        if (customizedName != null) {
            return customizedName;
        }
        return makeDefineName(packageName, shortClassName);
    }

    /**
     * カスタマイズされた名前を返します。
     * 
     * @param packageName
     * @param shortClassName
     * @return カスタマイズされた名前
     */
    protected String getCustomizedName(final String packageName,
            final String shortClassName) {
        final String fqn = ClassUtil.concatName(packageName, shortClassName);
        return (String) customizedNames.get(fqn);
    }

    /**
     * コンポーネント名を定義します。
     * 
     * @param packageName
     * @param shortClassName
     * @return コンポーネント名
     */
    protected abstract String makeDefineName(final String packageName,
            final String shortClassName);

    /**
     * ルールを適用します。
     * 
     * @param name
     * @return ルールが適用された結果
     */
    protected String applyRule(String name) {
        for (Iterator it = replaceRules.entrySet().iterator(); it.hasNext();) {
            final Entry entry = (Entry) it.next();
            final Pattern pattern = (Pattern) entry.getKey();
            final String replacement = (String) entry.getValue();
            final Matcher matcher = pattern.matcher(name);
            name = matcher.replaceAll(replacement);
        }
        name = normalize(name);
        if (decapitalize) {
            name = StringUtil.decapitalize(name);
        }
        return name;
    }

    /**
     * 正規化します。
     * 
     * @param name
     * @return 正規化された名前
     */
    protected String normalize(final String name) {
        final String[] names = name.split("\\.");
        final StringBuffer buf = new StringBuffer(name.length());
        for (int i = 0; i < names.length; ++i) {
            buf.append(StringUtil.capitalize(names[i]));
        }
        return new String(buf);
    }
}
