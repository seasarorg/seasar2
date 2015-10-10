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

/**
 * シーケンス記述です。
 * 
 * @author taedium
 */
public class SequenceDesc {

    /** キー */
    protected final Key key = new Key();

    /** シーケンス名 */
    protected String sequenceName;

    /** 初期値 */
    protected long initialValue;

    /** 割り当てサイズ */
    protected int allocationSize;

    /** データタイプ */
    protected String dataType;

    /**
     * シーケンス名を返します。
     * 
     * @return シーケンス名
     */
    public String getSequenceName() {
        return sequenceName;
    }

    /**
     * シーケンス記述を設定します。
     * 
     * @param sequenceName
     *            シーケンス名
     */
    public void setSequenceName(String sequenceName) {
        this.sequenceName = sequenceName;
        key.setSequenceName(sequenceName);
    }

    /**
     * 初期値を返します。
     * 
     * @return 初期値を設定します。
     */
    public long getInitialValue() {
        return initialValue;
    }

    /**
     * 初期値を設定します。
     * 
     * @param initialValue
     *            初期値
     */
    public void setInitialValue(long initialValue) {
        this.initialValue = initialValue;
    }

    /**
     * 割り当てサイズを返します。
     * 
     * @return 割り当てサイズ
     */
    public int getAllocationSize() {
        return allocationSize;
    }

    /**
     * 割り当てサイズを設定します。
     * 
     * @param allocationSize
     *            割り当てサイズ
     */
    public void setAllocationSize(int allocationSize) {
        this.allocationSize = allocationSize;
    }

    /**
     * データタイプを返します。
     * 
     * @return データタイプ
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * データタイプを設定します。
     * 
     * @param dataType
     *            データタイプ
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SequenceDesc other = (SequenceDesc) obj;
        return key.equals(other.key);
    }

    /**
     * キーです。
     * 
     * @author taedium
     */
    protected static class Key {

        /** シーケンス名 */
        protected String sequenceName;

        /**
         * シーケンス名を設定します。
         * 
         * @param sequenceName
         *            シーケンス名
         */
        public void setSequenceName(String sequenceName) {
            if (sequenceName != null) {
                this.sequenceName = sequenceName.toLowerCase();
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result
                    + ((sequenceName == null) ? 0 : sequenceName.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Key other = (Key) obj;
            if (sequenceName == null) {
                if (other.sequenceName != null) {
                    return false;
                }
            } else if (!sequenceName.equals(other.sequenceName)) {
                return false;
            }
            return true;
        }
    }

}
