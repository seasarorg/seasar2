package org.seasar.framework.container.impl;

import org.seasar.framework.container.S2Container;

public class ContainerDepend {

	private S2Container container;
	
	public ContainerDepend() {
		super();
	}

	/**
	 * @return Returns the container.
	 */
	public S2Container getContainer() {
		return container;
	}
	/**
	 * @param container The container to set.
	 */
	public void setContainer(S2Container container) {
		this.container = container;
	}
}
