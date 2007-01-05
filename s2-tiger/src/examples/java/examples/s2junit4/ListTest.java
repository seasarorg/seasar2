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
package examples.s2junit4;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seasar.framework.unit.Seasar2;

@RunWith(Seasar2.class)
public class ListTest {
    protected List<Integer> fEmpty;

    protected List<Integer> fFull;

    protected static List<Integer> fgHeavy;

    public static void beforeClass() {
        fgHeavy = new ArrayList<Integer>();
        for (int i = 0; i < 1000; i++)
            fgHeavy.add(i);
    }

    public void before() {
        fEmpty = new ArrayList<Integer>();
        fFull = new ArrayList<Integer>();
        fFull.add(1);
        fFull.add(2);
        fFull.add(3);
    }

    @Ignore("not today")
    public void capacity() {
        int size = fFull.size();
        for (int i = 0; i < 100; i++)
            fFull.add(i);
        assertTrue(fFull.size() == 100 + size);
    }

    public void testCopy() {
        List<Integer> copy = new ArrayList<Integer>(fFull.size());
        copy.addAll(fFull);
        assertTrue(copy.size() == fFull.size());
        assertTrue(copy.contains(1));
    }

    public void contains() {
        assertTrue(fFull.contains(1));
        assertTrue(!fEmpty.contains(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void elementAt() {
        int i = fFull.get(0);
        assertTrue(i == 1);
        fFull.get(fFull.size());
    }

    public void removeAll() {
        fFull.removeAll(fFull);
        fEmpty.removeAll(fEmpty);
        assertTrue(fFull.isEmpty());
        assertTrue(fEmpty.isEmpty());
    }

    public void removeElement() {
        fFull.remove(new Integer(3));
        assertTrue(!fFull.contains(3));
    }
}