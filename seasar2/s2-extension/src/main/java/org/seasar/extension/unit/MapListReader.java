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
package org.seasar.extension.unit;

import java.util.List;
import java.util.Map;

/**
 * リスト用の {@link MapReader}です。
 * 
 * @author higa
 * 
 */
public class MapListReader extends MapReader {

    /**
     * {@link MapListReader}を作成します。
     * 
     * @param list
     *            リスト
     */
    public MapListReader(List list) {
        setupColumns((Map) list.get(0));
        for (int i = 0; i < list.size(); ++i) {
            Map map = (Map) list.get(i);
            setupRow(map);
        }
    }

}
