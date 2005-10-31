package org.seasar.framework.container.factory;

import junit.framework.TestCase;

import org.seasar.framework.container.factory.AnnotationHandler;
import org.seasar.framework.container.factory.AnnotationHandlerFactory;
import org.seasar.framework.container.factory.TigerAnnotationHandler;

/**
 * @author higa
 * 
 */
public class AnnotationHandlerFactoryTest extends TestCase {

    public void testGetAnnotationHandler() throws Exception {
        AnnotationHandler handler = AnnotationHandlerFactory
                .getAnnotationHandler();
        assertEquals("1", TigerAnnotationHandler.class, handler.getClass());
    }
}