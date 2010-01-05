/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
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
package org.seasar.framework.unit.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

/**
 * EasyMockを利用してモックを作成することを示します。
 * <p>
 * EasyMockの詳細は<a href
 * ="http://www.easymock.org/index.html">http://www.easymock.org/index.html</a>を参照してください。
 * </p>
 * 
 * @author koichik
 * @author taedium
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface EasyMock {

    /**
     * モックの種類
     */
    EasyMockType value() default EasyMockType.DEFAULT;

    /**
     * S2コンテナに登録する場合<code>true</code>、登録しない場合<code>false</code>
     */
    boolean register() default false;

}
