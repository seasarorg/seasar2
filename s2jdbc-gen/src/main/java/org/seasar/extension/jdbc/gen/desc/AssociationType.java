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
package org.seasar.extension.jdbc.gen.desc;

import java.lang.annotation.Annotation;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * 関連タイプです。
 * 
 * @author taedium
 */
public enum AssociationType {

    /**
     * 多対一です。
     */
    MANY_TO_ONE {

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return ManyToOne.class;
        }
    },
    /**
     * 一対多です。
     */
    ONE_TO_MANY {

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return OneToMany.class;
        }
    },
    /**
     * 一対一です。
     */
    ONE_TO_ONE {

        @Override
        public Class<? extends Annotation> getAnnotation() {
            return OneToOne.class;
        }
    };

    /**
     * アノテーションを返します。
     * 
     * @return アノテーション
     */
    public abstract Class<? extends Annotation> getAnnotation();
}
