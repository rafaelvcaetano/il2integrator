package me.magnum.il2mapintegrator.core.tests.utils;

import java.util.HashMap;
import java.util.Map;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;

public final class CommandSender {
	private CommandSender() {
	}

	public static Response sendCommand(IL2Integrator il2Integrator, String service, String command) {
		return sendCommand(il2Integrator, service, command, new HashMap<>());
	}

	public static Response sendCommand(IL2Integrator il2Integrator, String service, String command, Map<String, String> args) {
		CommandRequest request = new CommandRequest();
		request.service = service;
		request.command = command;
		request.args = args;

		return il2Integrator.processCommand(request);
	}
}
