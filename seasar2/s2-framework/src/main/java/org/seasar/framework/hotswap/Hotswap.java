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
package org.seasar.framework.hotswap;

import java.io.Externalizable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.seasar.framework.util.ClassUtil;
import org.seasar.framework.util.ResourceUtil;

public class Hotswap implements Externalizable {

    final static long serialVersionUID = 0L;

    private transient Class targetClass;

    private transient String path;

    private transient File file;

    private transient long lastModified;

    public Hotswap() {
    }

    public Hotswap(Class targetClass) {
        this.targetClass = targetClass;
        setPath(ClassUtil.getResourcePath(targetClass));
        lastModified = file.lastModified();
    }

    public Class getTargetClass() {
        return targetClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        file = ResourceUtil.getResourceAsFile(path);
    }

    public File getFile() {
        return file;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isModified() {
        return lastModified > 0 && file.lastModified() > lastModified;
    }

    public Class updateTargetClass() {
        if (isModified()) {
            HotswapClassLoader classLoader = new HotswapClassLoader();
            targetClass = classLoader.defineClass(targetClass.getName(), file);
            lastModified = file.lastModified();
        }
        return targetClass;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(targetClass);
        out.writeUTF(path);
        out.writeLong(lastModified);
    }

    public void readExternal(ObjectInput in) throws IOException,
            ClassNotFoundException {

        targetClass = (Class) in.readObject();
        setPath(in.readUTF());
        lastModified = in.readLong();
    }
}