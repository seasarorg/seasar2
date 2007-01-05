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
 * 
 * @author koichik
 */
public abstract class AbstractAutoNaming implements AutoNaming {

    protected static final String IMPL = "Impl";

    protected static final String BEAN = "Bean";

    protected boolean decapitalize = true;

    protected Map customizedNames = new HashMap();

    protected Map replaceRules = new LinkedHashMap();

    public AbstractAutoNaming() {
        addIgnoreClassSuffix(IMPL);
        addIgnoreClassSuffix(BEAN);
    }

    public void setCustomizedName(final String fqcn, final String name) {
        customizedNames.put(fqcn, name);
    }

    public void addIgnoreClassSuffix(final String classSuffix) {
        addReplaceRule(classSuffix + "$", "");
    }

    public void addReplaceRule(final String regex, final String replacement) {
        replaceRules.put(Pattern.compile(regex), replacement);
    }

    public void clearReplaceRule() {
        customizedNames.clear();
        replaceRules.clear();
    }

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

    protected String getCustomizedName(final String packageName,
            final String shortClassName) {
        final String fqn = ClassUtil.concatName(packageName, shortClassName);
        return (String) customizedNames.get(fqn);
    }

    protected abstract String makeDefineName(final String packageName,
            final String shortClassName);

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

    protected String normalize(final String name) {
        final String[] names = name.split("\\.");
        final StringBuffer buf = new StringBuffer(name.length());
        for (int i = 0; i < names.length; ++i) {
            buf.append(StringUtil.capitalize(names[i]));
        }
        return new String(buf);
    }
}
