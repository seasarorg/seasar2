package org.seasar.framework.container.factory.property;

import javax.persistence.PersistenceContext;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.jpa.TxScopedEntityManagerProxy;
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
            final PropertyDef emfPropertyDef = createPropertyDef(
                    "entityManagerFactory", AccessTypeDefFactory.PROPERTY, name);
            final ComponentDef emComponentDef = new ComponentDefImpl(
                    TxScopedEntityManagerProxy.class);
            emComponentDef.addPropertyDef(emfPropertyDef);
            return createPropertyDef(propertyName, accessTypeDef,
                    emComponentDef);
        }

        final String unitName = persistenceContext.unitName();
        if (StringUtil.isEmpty(unitName)) {
            // not specified element (binding by type)
            return createPropertyDef(propertyName, accessTypeDef);
        }

        // specified 'unitName' element
        final ComponentDef emfComponentDef = PersistenceUnitPropertyDefBuilder
                .createPersistenceUnitCompoentDef(unitName);
        final PropertyDef emfPropertyDef = createPropertyDef(
                "entityManagerFactory", AccessTypeDefFactory.PROPERTY,
                emfComponentDef);
        final ComponentDef emComponentDef = new ComponentDefImpl(
                TxScopedEntityManagerProxy.class);
        emComponentDef.addPropertyDef(emfPropertyDef);
        return createPropertyDef(propertyName, accessTypeDef, emComponentDef);
    }
}
