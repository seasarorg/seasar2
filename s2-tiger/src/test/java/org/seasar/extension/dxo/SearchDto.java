/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
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
package org.seasar.extension.dxo;

/**
 * @author koichik
 * 
 */
public class SearchDto {

    private String name;

    private String name_LIKE;

    private Integer age;

    private Integer age_GT;

    private String hoge;

    /**
     * @return
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return
     */
    public Integer getAge_GT() {
        return age_GT;
    }

    /**
     * @param age_GT
     */
    public void setAge_GT(Integer age_GT) {
        this.age_GT = age_GT;
    }

    /**
     * @return
     */
    public String getHoge() {
        return hoge;
    }

    /**
     * @param hoge
     */
    public void setHoge(String hoge) {
        this.hoge = hoge;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getName_LIKE() {
        return name_LIKE;
    }

    /**
     * @param name_LIKE
     */
    public void setName_LIKE(String name_LIKE) {
        this.name_LIKE = name_LIKE;
    }

}
