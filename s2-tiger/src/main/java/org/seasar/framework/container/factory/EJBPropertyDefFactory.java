package org.seasar.framework.container.factory;

import javax.ejb.EJB;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.util.StringUtil;

public class EJBPropertyDefFactory extends AbstractPropertyDefFactory<EJB> {

    public EJBPropertyDefFactory() {
    }

    @Override
    protected Class<EJB> getAnnotationType() {
        return EJB.class;
    }

    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final EJB ejb) {
        return createPropertyDef(propertyName, accessTypeDef,
                getExpression(ejb));
    }

    protected String getExpression(final EJB ejb) {
        String name = ejb.beanName();
        if (StringUtil.isEmpty(name)) {
            name = ejb.name();
        }
        return name.replace('/', '.');
    }
}
