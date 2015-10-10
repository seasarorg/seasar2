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
package org.seasar.extension.jdbc.it.entity;

/**
 * @author taedium
 * 
 */
public enum JobType {

    /**
     * クラークです。
     */
    CLERK {

        @Override
        public int calculateBonus(int salary) {
            return salary;
        }
    },
    /**
     * セールスマンです。
     */
    SALESMAN {

        @Override
        public int calculateBonus(int salary) {
            return salary * 2;
        }
    },
    /**
     * マネージャです。
     */
    MANAGER {

        @Override
        public int calculateBonus(int salary) {
            return salary * 3;
        }
    },
    /**
     * アナリストです。
     */
    ANALYST {

        @Override
        public int calculateBonus(int salary) {
            return salary * 4;
        }
    },
    /**
     * プレシデントです。
     */
    PRESIDENT {

        @Override
        public int calculateBonus(int salary) {
            return salary * 5;
        }
    };

    /**
     * ボーナスを計算します。
     * 
     * @param salary
     *            給与
     * @return ボーナス
     */
    public abstract int calculateBonus(int salary);

}
