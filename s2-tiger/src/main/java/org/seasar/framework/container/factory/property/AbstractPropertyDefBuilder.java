package org.seasar.framework.container.factory.property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.seasar.framework.beans.BeanDesc;
import org.seasar.framework.beans.PropertyDesc;
import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.PropertyDef;
import org.seasar.framework.container.assembler.AccessTypeDefFactory;
import org.seasar.framework.container.assembler.BindingTypeDefFactory;
import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.PropertyDefBuilder;
import org.seasar.framework.container.impl.PropertyDefImpl;
import org.seasar.framework.container.ognl.OgnlExpression;
import org.seasar.framework.util.StringUtil;

public abstract class AbstractPropertyDefBuilder<T extends Annotation>
        implements PropertyDefBuilder {

    public AbstractPropertyDefBuilder() {
    }

    public PropertyDef createPropertyDef(
            final AnnotationHandler annotationHandler, BeanDesc beanDesc,
            PropertyDesc propertyDesc) {
        if (!propertyDesc.hasWriteMethod()) {
            return null;
        }
        final Method method = propertyDesc.getWriteMethod();
        final T annotation = method.getAnnotation(getAnnotationType());
        if (annotation == null) {
            return null;
        }
        return createPropertyDef(propertyDesc.getPropertyName(),
                AccessTypeDefFactory.PROPERTY, annotation);
    }

    public PropertyDef createPropertyDef(
            final AnnotationHandler annotationHandler, BeanDesc beanDesc,
            Field field) {
        final T annotation = field.getAnnotation(getAnnotationType());
        if (annotation == null) {
            return null;
        }
        return createPropertyDef(field.getName(), AccessTypeDefFactory.FIELD,
                annotation);
    }

    protected abstract Class<T> getAnnotationType();

    protected abstract PropertyDef createPropertyDef(String name,
            AccessTypeDef accessTypeDef, T annotation);

    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef) {
        return createPropertyDef(propertyName, accessTypeDef, "");
    }

    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final String expression) {
        return createPropertyDef(propertyName, accessTypeDef, expression, null);
    }

    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final ComponentDef child) {
        return createPropertyDef(propertyName, accessTypeDef, "", child);
    }

    protected PropertyDef createPropertyDef(final String propertyName,
            final AccessTypeDef accessTypeDef, final String expression,
            final ComponentDef child) {
        final PropertyDef propertyDef = new PropertyDefImpl(propertyName);
        propertyDef.setAccessTypeDef(accessTypeDef);
        propertyDef.setBindingTypeDef(BindingTypeDefFactory.MUST);
        if (!StringUtil.isEmpty(expression)) {
            propertyDef.setExpression(new OgnlExpression(expression));
        }
        if (child != null) {
            propertyDef.setChildComponentDef(child);
        }
        return propertyDef;
    }
}
