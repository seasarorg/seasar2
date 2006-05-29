package org.seasar.framework.container.deployer;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.ComponentDeployer;

public class HttpServletComponentDeployerProvider extends
        ComponentDeployerFactory.DefaultProvider {

    public ComponentDeployer createRequestComponentDeployer(
            final ComponentDef cd) {
        return new RequestComponentDeployer(cd);
    }

    public ComponentDeployer createSessionComponentDeployer(
            final ComponentDef cd) {
        return new SessionComponentDeployer(cd);
    }

    public ComponentDeployer createApplicationComponentDeployer(
            final ComponentDef cd) {
        return new ServletContextComponentDeployer(cd);
    }
}