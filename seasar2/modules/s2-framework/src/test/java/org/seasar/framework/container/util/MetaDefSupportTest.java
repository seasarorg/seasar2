package org.seasar.framework.container.util;

import junit.framework.TestCase;

import org.seasar.framework.container.MetaDef;
import org.seasar.framework.container.impl.MetaDefImpl;
import org.seasar.framework.container.util.MetaDefSupport;

/**
 * @author higa
 *  
 */
public class MetaDefSupportTest extends TestCase {

	public void testGetMetaDefs() throws Exception {
		MetaDefSupport support = new MetaDefSupport();
		support.addMetaDef(new MetaDefImpl("aaa"));
		support.addMetaDef(new MetaDefImpl("bbb"));
		support.addMetaDef(new MetaDefImpl("aaa"));
		MetaDef[] metaDefs = support.getMetaDefs("aaa");
		assertEquals("1", 2, metaDefs.length);
	}
}