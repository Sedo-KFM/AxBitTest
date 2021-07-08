package com.sedo.AxBitTest.helpers;

import org.springframework.ui.Model;

public class MessageToModelTransfer {

	public static void transferMessage(StringBuilder message, Model model) {
		if (message.length() > 0) {
			model.addAttribute("message", message.toString());
			message.delete(0, message.length());
		}
	}
}
