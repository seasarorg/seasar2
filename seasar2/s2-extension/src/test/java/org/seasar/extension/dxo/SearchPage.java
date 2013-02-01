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
 */
public class SearchPage {

    private String search_name_LIKE;

    private Integer search_age_GT;

    private String name;

    private Integer age;

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
    public Integer getSearch_age_GT() {
        return search_age_GT;
    }

    /**
     * @param search_age_GT
     */
    public void setSearch_age_GT(Integer search_age_GT) {
        this.search_age_GT = search_age_GT;
    }

    /**
     * @return
     */
    public String getSearch_name_LIKE() {
        return search_name_LIKE;
    }

    /**
     * @param search_name_LIKE
     */
    public void setSearch_name_LIKE(String search_name_LIKE) {
        this.search_name_LIKE = search_name_LIKE;
    }

}
