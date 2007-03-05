package example.smartdeploy.service.impl;

import java.io.Serializable;

import example.smartdeploy.service.MessageService;

public class MessageServiceImpl implements MessageService, Serializable {

	public String getMessage() {
		return ("Hello HOT deploy");
	}
}