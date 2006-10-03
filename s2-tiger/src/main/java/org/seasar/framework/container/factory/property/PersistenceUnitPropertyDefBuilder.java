package org.seasar.framework.container.factory.property;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.Expression;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.jpa.PersistenceUnitManager;
import org.seasar.framework.util.StringUtil;

public class PersistenceUnitPropertyDefBuilder extends
        AbstractPropertyDefBuilder<PersistenceUnit> {
    public PersistenceUnitPropertyDefBuilder() {
    }

    @Override
    protected Class<PersistenceUnit> getAnnotationType() {
        return PersistenceUnit.class;
    }

    @Override
    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef,
            final PersistenceUnit persistenceUnit) {
        final String name = persistenceUnit.name();
        if (!StringUtil.isEmpty(name)) {
            return createPropertyDef(propertyName, accessTypeDef, name);
        }

        final String unitName = persistenceUnit.unitName();
        if (StringUtil.isEmpty(unitName)) {
            return createPropertyDef(propertyName, accessTypeDef);
        }

        return createPropertyDef(propertyName, accessTypeDef,
                createPersistenceUnitCompoentDef(unitName));
    }

    protected static ComponentDef createPersistenceUnitCompoentDef(
            final String unitName) {
        final ComponentDef componentDef = new ComponentDefImpl(
                EntityManagerFactory.class);
        componentDef.setExpression(getExpression(unitName));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("close"));
        return componentDef;
    }

    protected static Expression getExpression(final String unitName) {
        return new PersistenceUnitExpression(unitName);
    }

    public static class PersistenceUnitExpression implements Expression {
        String unitName;

        public PersistenceUnitExpression(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitName() {
            return unitName;
        }

        @SuppressWarnings("unchecked")
        public Object evaluate(final S2Container container, final Map context) {
            final PersistenceUnitManager pum = (PersistenceUnitManager) container
                    .getComponent("jpa.persistenceUnitManager");
            return pum.getEntityManagerFactory(unitName);
        }
    }
}
