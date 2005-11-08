package org.seasar.framework.container.factory;

/**
 * @author higa
 */
public class Foo {

    public static final String COMPONENT = "name = foo";
    
    public static final String aaa_INJECT = "aaa";
        
    private String aaa;
    
    /**
     * @return Returns the aaa.
     */
    public String getAaa() {
        return aaa;
    }
    
    /**
     * @param aaa The aaa to set.
     */
    public void setAaa(String aaa) {
        this.aaa = aaa;
    }
}