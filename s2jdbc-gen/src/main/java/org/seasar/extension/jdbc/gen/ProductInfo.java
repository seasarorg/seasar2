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
package org.seasar.extension.jdbc.gen;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * プロダクト情報です。
 * 
 * @author taedium
 */
public class ProductInfo {

    /** プロダクト名 */
    protected final String PRODUCT_NAME = "S2JDBC-Gen";

    /** 不明な値 */
    protected final String UNKNOWN = "unknown";

    /** groupId */
    protected final String groupId;

    /** artifactId */
    protected final String artifactId;

    /** バージョン */
    protected final String version;

    /**
     * インスタンスを構築します。
     * 
     * @param version
     * @param groupId
     * @param artifactId
     */
    protected ProductInfo(String version, String groupId, String artifactId) {
        this.version = version != null ? version : UNKNOWN;
        this.groupId = groupId != null ? groupId : UNKNOWN;
        this.artifactId = artifactId != null ? artifactId : UNKNOWN;
    }

    /**
     * 名前を返します。
     * 
     * @return
     */
    public String getName() {
        return PRODUCT_NAME;
    }

    /**
     * バージョンを返します。
     * 
     * @return
     */
    public String getVersion() {
        return version;
    }

    /**
     * groupIdを返します。
     * 
     * @return
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * artifactIdを返します。
     * 
     * @return
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * シングルトンの{@link ProductInfo}を返します。
     * 
     * @return シングルトンの{@link ProductInfo}
     */
    public static ProductInfo getInstance() {
        return ProductInfoHolder.productInfo;
    }

    /**
     * プロダクト情報を標準出力へ出力します。
     * 
     * @param args
     */
    public static void main(String[] args) {
        ProductInfo info = ProductInfoHolder.productInfo;
        System.out.printf("s2jdbc-gen name : %s\n", info.getName());
        System.out.printf("s2jdbc-gen version : %s\n", info.getVersion());
        System.out.printf("s2jdbc-gen groupId : %s\n", info.getGroupId());
        System.out.printf("s2jdbc-gen artifactId : %s\n", info.getArtifactId());
    }

    /**
     * プロダクト情報のホルダです。
     * 
     * @author taedium
     */
    protected static class ProductInfoHolder {

        /** POMのプロパティファイルのパス */
        protected static final String POM_PROPERTIES_PATH = "META-INF/maven/org.seasar.container/s2jdbc-gen/pom.properties";

        /** プロダクト情報 */
        protected static final ProductInfo productInfo = createProductInfo();

        /**
         * プロダクト情報を返します。
         * 
         * @return プロダクト情報
         */
        protected static ProductInfo createProductInfo() {
            Properties props = loadProperties();
            return new ProductInfo(props.getProperty("version"), props
                    .getProperty("groupId"), props.getProperty("artifactId"));
        }

        /**
         * プロパティをロードし返します。
         * 
         * @return プロパティ
         */
        protected static Properties loadProperties() {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(POM_PROPERTIES_PATH);
            Properties props = new Properties();
            if (is != null) {
                try {
                    props.load(is);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return props;
        }
    }

}
