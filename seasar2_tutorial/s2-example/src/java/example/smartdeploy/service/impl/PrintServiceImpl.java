package example.smartdeploy.service.impl;

import example.smartdeploy.service.MessageService;
import example.smartdeploy.service.PrintService;

public class PrintServiceImpl implements PrintService {
	private MessageService messageService;

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public void print() {
		System.out.println(messageService.getMessage());
	}
}