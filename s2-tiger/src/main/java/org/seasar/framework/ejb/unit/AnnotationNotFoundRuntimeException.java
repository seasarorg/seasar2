package org.seasar.framework.ejb.unit;

import org.seasar.framework.exception.SRuntimeException;

/**
 * @author taedium
 *
 */
public class AnnotationNotFoundRuntimeException extends SRuntimeException {
	private static final long serialVersionUID = 1L;

	public AnnotationNotFoundRuntimeException(String annotationName,
			String targetName) {
        // TODO : write message to SSRMessages.properties
		super("ESSR0501", new Object[] { annotationName, targetName });
	}
}
