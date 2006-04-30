package org.seasar.framework.container.hotdeploy;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.S2Container;

public interface ComponentFilter {

    ComponentDef createComponentDef(S2Container container, Class clazz);
}
