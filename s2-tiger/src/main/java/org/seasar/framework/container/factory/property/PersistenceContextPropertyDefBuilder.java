package org.seasar.framework.container.factory.property;

import javax.persistence.PersistenceContext;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.jpa.impl.TxScopedEntityManagerProxy;
import org.seasar.framework.util.StringUtil;

public class PersistenceContextPropertyDefBuilder extends
        AbstractPropertyDefBuilder<PersistenceContext> {

    public PersistenceContextPropertyDefBuilder() {
    }

    @Override
    protected Class<PersistenceContext> getAnnotationType() {
        return PersistenceContext.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef,
            final PersistenceContext persistenceContext) {
        final String name = persistenceContext.name();
        if (!StringUtil.isEmpty(name)) {
            // specified 'name' element (binding by name)
            final ComponentDef emComponentDef = createComponentDef(TxScopedEntityManagerProxy.class);
            final PropertyDef emfPropertyDef = createPropertyDef("emf",
                    AccessTypeDefFactory.FIELD, name);
            emComponentDef.addPropertyDef(emfPropertyDef);
            return createPropertyDef(propertyName, accessTypeDef,
                    emComponentDef);
        }

        final String unitName = persistenceContext.unitName();
        if (!StringUtil.isEmpty(unitName)) {
            // specified 'unitName' element
            final ComponentDef emfComponentDef = PersistenceUnitPropertyDefBuilder
                    .createPersistenceUnitCompoentDef(unitName);
            final ComponentDef emComponentDef = createComponentDef(TxScopedEntityManagerProxy.class);
            final PropertyDef emfPropertyDef = createPropertyDef("emf",
                    AccessTypeDefFactory.FIELD, emfComponentDef);
            emComponentDef.addPropertyDef(emfPropertyDef);
            return createPropertyDef(propertyName, accessTypeDef,
                    emComponentDef);
        }

        // not specified element (binding by type)
        return createPropertyDef(propertyName, accessTypeDef);
    }
}
