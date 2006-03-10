package org.seasar.framework.ejb;

import java.util.ArrayList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.seasar.framework.aop.intertype.AbstractInterType;

public class EJB3InterceptorSupportInterType extends AbstractInterType {
    protected List<Class<?>> interceptorClasses = new ArrayList<Class<?>>();

    public EJB3InterceptorSupportInterType() {
    }

    public void addInterceptor(final Class<?> interceptorClass) {
        interceptorClasses.add(interceptorClass);
    }

    public boolean hasInterceptor() {
        return !interceptorClasses.isEmpty();
    }

    @Override
    protected void introduce() throws CannotCompileException, NotFoundException {
        for (final Class<?> clazz : interceptorClasses) {
            final String name = getFieldName(clazz);
            addField(clazz, name);
            addMethod("set" + name, new Class[] { clazz }, "this." + name
                    + "=$1;");
        }
    }

    public static String getFieldName(final Class<?> clazz) {
        return "$$S2EJB3$$" + clazz.getName().replace('.', '$') + "$$";
    }
}
