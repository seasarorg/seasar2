package org.seasar.framework.container.factory;

import java.util.Formatter;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.impl.ComponentDefImpl;
import org.seasar.framework.container.impl.DestroyMethodDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.StringUtil;

public class PersistenceUnitPropertyDefFactory extends
        AbstractPropertyDefFactory<PersistenceUnit> {
    public static final String CREATE_EMF = "jpa.persistenceUnitManager.getEntityManagerFactory(\"%s\")";

    public PersistenceUnitPropertyDefFactory() {
    }

    @Override
    protected Class<PersistenceUnit> getAnnotationType() {
        return PersistenceUnit.class;
    }

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
        componentDef.setExpression(new OgnlExpression(getExpression(unitName)));
        componentDef.addDestroyMethodDef(new DestroyMethodDefImpl("close"));
        return componentDef;
    }

    protected static String getExpression(final String unitName) {
        final StringBuilder buf = new StringBuilder(100);
        final Formatter formatter = new Formatter(buf);
        formatter.format(CREATE_EMF, unitName);
        return new String(buf);
    }
}
