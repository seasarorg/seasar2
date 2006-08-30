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
package org.seasar.framework.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import org.seasar.framework.autodetector.ResourceAutoDetector;
import org.seasar.framework.autodetector.impl.AbstractResourceAutoDetector;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InitMethod;
import org.seasar.framework.convention.NamingConvention;
import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.ResourceTraversal.ResourceHandler;
import org.seasar.framework.util.tiger.CollectionsUtil;

/**
 * @author taedium
 * 
 */
@Component
public class MappingFileAutoDetector extends AbstractResourceAutoDetector {

    protected NamingConvention namingConvention;

    public MappingFileAutoDetector() {
        addTargetDirPath("META-INF");
        addResourceNamePattern("META-INF/orm.xml");
        addResourceNamePattern(".*Orm.xml");
    }

    @InitMethod
    public void init() {
        if (namingConvention != null) {
            final String entityPackageName = namingConvention
                    .getEntityPackageName();
            for (final String rootPackageName : namingConvention
                    .getRootPackageNames()) {
                final String packageName = ClassUtil.concatName(
                        rootPackageName, entityPackageName);
                final String path = packageName.replace(".", "/");
                addTargetDirPath(path);
            }
        }
    }

    @Binding(bindingType = BindingType.MAY)
    public void setNamingConvention(final NamingConvention namingConvention) {
        this.namingConvention = namingConvention;
    }

    public ResourceAutoDetector.Entry[] detect() {
        final List<ResourceAutoDetector.Entry> result = CollectionsUtil
                .newArrayList();

        for (int i = 0; i < getTargetDirPathSize(); i++) {
            final String targetDirPath = getTargetDirPath(i);
            Enumeration<URL> urls = null;
            try {
                final ClassLoader loader = Thread.currentThread()
                        .getContextClassLoader();
                urls = loader.getResources(targetDirPath);
            } catch (IOException ignore) {
            }
            while (urls != null && urls.hasMoreElements()) {
                final URL targetDirUrl = urls.nextElement();
                detect(result, targetDirPath, targetDirUrl);
            }
        }

        return (ResourceAutoDetector.Entry[]) result.toArray(new Entry[result
                .size()]);
    }

    protected void detect(final List<ResourceAutoDetector.Entry> result,
            final String targetDirPath, final URL targetDirUrl) {
        final Set<ResourceAutoDetector.Entry> entries = CollectionsUtil
                .newHashSet();

        for (final String rootPackageName : namingConvention
                .getRootPackageNames()) {
            final String rootDirPath = rootPackageName.replace(".", "/");
            final URL rootDirUrl = ResourceUtil.getResource(rootDirPath);
            if (!rootDirUrl.getProtocol().equals(targetDirUrl.getProtocol())) {
                continue;
            }
            final Strategy strategy = getStrategy(rootDirUrl.getProtocol());
            final String rootDirBaseName = strategy.getBaseName(rootDirPath,
                    rootDirUrl);
            final String targetDirBaseName = strategy.getBaseName(
                    targetDirPath, targetDirUrl);
            if (!rootDirBaseName.equals(targetDirBaseName)) {
                continue;
            }

            strategy.detect(targetDirPath, targetDirUrl, new ResourceHandler() {
                public void processResource(final String path,
                        final InputStream is) {
                    if (path.startsWith(targetDirPath) && isApplied(path)
                            && !isIgnored(path)) {
                        final Entry entry = new Entry(path, is);
                        entries.add(entry);
                    }
                }
            });
        }

        result.addAll(entries);
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

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof Entry)) {
                return false;
            }
            Entry castOther = Entry.class.cast(other);
            return path.equals(castOther.path);
        }

        @Override
        public int hashCode() {
            return path.hashCode();
        }

        public String toString() {
            return path;
        }
    }
}
