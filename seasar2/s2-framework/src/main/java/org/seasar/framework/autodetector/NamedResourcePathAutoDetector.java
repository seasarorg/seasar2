/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
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
package org.seasar.framework.autodetector;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.seasar.framework.traverser.ResourceTraverser;
import org.seasar.framework.traverser.Traverser;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * @author taedium
 * 
 */
public class NamedResourcePathAutoDetector extends AbstractAutoDetector implements
        ResourcePathAutoDetector {

    private final List resourceNamePatterns = new ArrayList();

    private final List ignoreResourceNamePatterns = new ArrayList();

    public void addResourceNamePattern(final String resourceName) {
        resourceNamePatterns.add(Pattern.compile(resourceName));
    }

    public void addIgnoreResourceNamePattern(final String resourceName) {
        ignoreResourceNamePatterns.add(Pattern.compile(resourceName));
    }

    public Pattern getResourceNamePattern(final int index) {
        return (Pattern) resourceNamePatterns.get(index);
    }

    public void getResourceNamePatternSize() {
        resourceNamePatterns.size();
    }

    public Pattern getIgnoreResourceNamePattern(final int index) {
        return (Pattern) ignoreResourceNamePatterns.get(index);
    }

    public int getIgnoreResourceNamePatternSize() {
        return ignoreResourceNamePatterns.size();
    }

    public String[] detect() {
        final Set result = new HashSet();

        for (int i = 0; i < getReferenceClassSize(); i++) {
            final Class referenceClass = getReferenceClass(i);
            final String baseClassPath = ResourceUtil
                    .getResourcePath(referenceClass);
            final URL url = ResourceUtil.getResource(baseClassPath);
            final Strategy strategy = getStrategy(url.getProtocol());
            final Traverser traverser = new ResourceTraverser(
                    new ResourceHandler() {
                        public void processResource(String path) {
                            if (isApplied(path) && !isIgnored(path)) {
                                result.add(path);
                            }
                        }
                    });
            strategy.detect(referenceClass, url, traverser);
        }

        return (String[]) result.toArray(new String[] {});
    }

    protected boolean isApplied(final String resourceName) {
        if (getReferenceClassSize() == 0) {
            return true;
        }
        for (int i = 0; i < getReferenceClassSize(); i++) {
            final Pattern pattern = getResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }

    protected boolean isIgnored(final String resourceName) {
        for (int i = 0; i < getIgnoreResourceNamePatternSize(); i++) {
            final Pattern pattern = getIgnoreResourceNamePattern(i);
            if (pattern.matcher(resourceName).matches()) {
                return true;
            }
        }
        return false;
    }
}
