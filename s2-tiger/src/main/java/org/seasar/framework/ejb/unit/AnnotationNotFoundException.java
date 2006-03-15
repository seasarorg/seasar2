package org.seasar.framework.ejb.unit;

import java.lang.annotation.Annotation;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 * 
 */
public class AnnotationNotFoundException extends SRuntimeException {
    private static final long serialVersionUID = 1L;

    private Class<?> targetClass;

    private Class<? extends Annotation> annotationClass;

    public AnnotationNotFoundException(Class<?> targetClass,
            Class<? extends Annotation> annotationClass) {
        super("ESSR0500", new Object[] { targetClass.getName(),
                "@" + annotationClass.getName() });
        this.targetClass = targetClass;
        this.annotationClass = annotationClass;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }
}
