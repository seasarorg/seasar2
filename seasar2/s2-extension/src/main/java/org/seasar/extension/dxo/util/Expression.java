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
package org.seasar.extension.dxo.util;

import java.util.Map;

/**
 * 変換ルールで指定された式の評価可能な表現です。
 * 
 * @author koichik
 */
public interface Expression {

    /**
     * 式を評価した結果の{@link Map}を返します。
     * 
     * @param source
     *            変換元オブジェクト
     * @return 式を評価した結果の{@link Map}
     */
    Map evaluate(Object source);

}
