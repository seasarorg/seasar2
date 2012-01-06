/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * 指定された論理パスを指定された物理パスに置換する{@link PathResolver}の実装クラスです。
 * <p>
 * 指定されていない場合、論理パスはそのまま物理パスとなります。
 * </p>
 * 
 * @author koichik
 * @author jundu
 */
public class SimplePathResolver implements PathResolver {

    private Map realPaths = new HashMap();

    /**
     * 置換対象の論理パスと置換後の物理パスを関連付けます。
     * 
     * @param targetPath
     *            置換対象の論理パス
     * @param realPath
     *            置換後の物理パス
     */
    public void addRealPath(String targetPath, String realPath) {
        realPaths.put(targetPath, realPath);
    }

    public String resolvePath(String context, String path) {
        if (realPaths.containsKey(path)) {
            return (String) realPaths.get(path);
        }
        return path;
    }
}
