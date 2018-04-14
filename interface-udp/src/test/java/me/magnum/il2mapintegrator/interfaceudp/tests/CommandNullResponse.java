package me.magnum.il2mapintegrator.interfaceudp.tests;

import java.util.Map;

import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class CommandNullResponse extends Command {
	public CommandNullResponse() {
		super("null");
	}

	@Override
	public Response execute(Map<String, String> args) {
		return null;
	}
}
