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
package examples.aop.syncinterceptor;

import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;

public class AopSyncClient {
    private String PATH = "examples/aop/syncinterceptor/SyncCalc.dicon";

    private Count _count = null;

    public void init() {
        S2Container container = S2ContainerFactory.create(PATH);
        _count = (Count) container.getComponent(Count.class);
    }

    public void start() {
        System.out.println("count: " + _count.get());

        Runnable r = new Runnable() {
            public void run() {
                _count.add();
            }
        };
        Thread[] thres = new Thread[5];
        for (int i = 0; i < 5; i++) {
            thres[i] = new Thread(r);
            thres[i].start();
        }
        for (int i = 0; i < 5; i++) {
            try {
                thres[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("count: " + _count.get());
    }

    public static void main(String[] args) {
        AopSyncClient asc = new AopSyncClient();
        asc.init();
        asc.start();
    }
}
