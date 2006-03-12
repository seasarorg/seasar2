package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 *
 */
public class AnnotationNotFoundException extends SRuntimeException {
	private static final long serialVersionUID = 1L;

	public AnnotationNotFoundException(String annotationName,
			String targetName) {
        // TODO : write message to SSRMessages.properties
		super("ESSR0501", new Object[] { annotationName, targetName });
	}
}
