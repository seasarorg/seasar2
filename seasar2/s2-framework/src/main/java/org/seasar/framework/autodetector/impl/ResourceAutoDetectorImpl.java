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
package org.seasar.framework.autodetector.impl;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.traverser.ResourceTraverser;
import org.seasar.framework.traverser.Traverser;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;

/**
 * @author taedium
 * 
 */
public class ResourceAutoDetectorImpl extends AbstractAutoDetector implements
        ResourceAutoDetector {

    public static final String INIT_METHOD = "initialize";

    private static final String ROOT = "";

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

    public int getResourceNamePatternSize() {
        return resourceNamePatterns.size();
    }

    public Pattern getIgnoreResourceNamePattern(final int index) {
        return (Pattern) ignoreResourceNamePatterns.get(index);
    }

    public int getIgnoreResourceNamePatternSize() {
        return ignoreResourceNamePatterns.size();
    }

    public void initialize() {
        if (getDirectoryNameSize() == 0) {
            addDirectoryName(ROOT);
        }
    }

    public ResourceAutoDetector.Entry[] detect(String path, URL url) {
        final List result = new ArrayList();
        final Strategy strategy = getStrategy(url.getProtocol());
        final Traverser traverser = new ResourceTraverser(
                new ResourceHandler() {
                    public void processResource(final String path,
                            final InputStream is) {
                        if (isApplied(path) && !isIgnored(path)) {
                            final Entry entry = new Entry(path, is);
                            result.add(entry);
                        }
                    }
                });
        strategy.detect(path, url, traverser);
        return (ResourceAutoDetector.Entry[]) result.toArray(new Entry[result
                .size()]);
    }

    protected boolean isApplied(final String resourceName) {
        for (int i = 0; i < getResourceNamePatternSize(); i++) {
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

    public static class Entry implements ResourceAutoDetector.Entry {

        private String path;

        private InputStream is;

        Entry(String path, InputStream is) {
            this.path = path;
            this.is = is;
        }

        public String getPath() {
            return path;
        }

        public InputStream getInputStream() {
            return is;
        }
    }

}
