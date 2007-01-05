/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
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
package examples.jdbc;

import java.util.List;

import org.seasar.extension.jdbc.SelectHandler;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class SelectBeanListClient {

    private static final String PATH = "examples/jdbc/SelectBeanList.dicon";

    public static void main(String[] args) {
        S2Container container = S2ContainerFactory.create(PATH);
        container.init();
        try {
            SelectHandler handler = (SelectHandler) container
                    .getComponent("selectBeanListHandler");
            List result = (List) handler.execute(null);
            for (int i = 0; i < result.size(); ++i) {
                System.out.println(result.get(i));
            }
        } finally {
            container.destroy();
        }

    }
}
