package org.seasar.framework.ejb.unit;

import java.util.List;

import org.seasar.framework.ejb.unit.impl.EntityClass;

/**
 * @author taedium
 *
 */
public class EntityListReader extends EntityReader {

    public EntityListReader(List list) {
        PersistentClass psh = new EntityClass(list.get(0).getClass());
        setupColumns(psh);
        for (int i = 0; i < list.size(); ++i) {
            setupRow(psh, list.get(i));
            release(list.get(i));
        }
    }
}
