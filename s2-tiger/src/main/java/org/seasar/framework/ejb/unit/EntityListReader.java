package org.seasar.framework.ejb.unit;

import java.util.List;

import org.seasar.framework.ejb.unit.impl.EntityClassDesc;

/**
 * @author taedium
 *
 */
public class EntityListReader extends EntityReader {

    public EntityListReader(List list) {
        PersistentClassDesc psh = new EntityClassDesc(list.get(0).getClass());
        setupColumns(psh);
        for (int i = 0; i < list.size(); ++i) {
            setupRow(psh, list.get(i));
            release(list.get(i));
        }
    }
}
