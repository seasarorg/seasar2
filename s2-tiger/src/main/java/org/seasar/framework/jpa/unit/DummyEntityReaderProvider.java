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
package org.seasar.framework.jpa.unit;

import java.util.Collection;


/**
 * @author koichik
 *
 */
public class DummyEntityReaderProvider implements EntityReaderProvider {

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.unit.EntityReaderProvider#createEntityReader(java.lang.Object)
     */
    public EntityReader createEntityReader(Object entity) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.seasar.framework.jpa.unit.EntityReaderProvider#createEntityReader(java.util.Collection)
     */
    public EntityReader createEntityReader(Collection<?> entities) {
        // TODO Auto-generated method stub
        return null;
    }

}
