package org.seasar.framework.container.assembler;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.AccessTypeDef;
import org.seasar.framework.container.IllegalAccessTypeDefRuntimeException;

/**
 * アクセスタイプ定義のファクトリです。
 * 
 * @author higa
 *
 */
public class AccessTypeDefFactory {

    public static final AccessTypeDef PROPERTY = new AccessTypePropertyDef();

    public static final AccessTypeDef FIELD = new AccessTypeFieldDef();

    private AccessTypeDefFactory() {
    }

    private static Map accessTypeDefs = new HashMap();

    static {
        addAccessTypeDef(PROPERTY);
        addAccessTypeDef(FIELD);
    }

    public static void addAccessTypeDef(final AccessTypeDef accessTypeDef) {
        accessTypeDefs.put(accessTypeDef.getName(), accessTypeDef);
    }

    public static boolean existAccessTypeDef(String name) {
        return accessTypeDefs.containsKey(name);
    }

    public static AccessTypeDef getAccessTypeDef(String name) {
        if (!existAccessTypeDef(name)) {
            throw new IllegalAccessTypeDefRuntimeException(name);
        }
        return (AccessTypeDef) accessTypeDefs.get(name);
    }
}
