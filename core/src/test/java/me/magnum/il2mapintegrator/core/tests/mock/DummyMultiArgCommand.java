package me.magnum.il2mapintegrator.core.tests.mock;

import java.util.Map;

import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class DummyMultiArgCommand extends Command {
	public DummyMultiArgCommand() {
		super("multi");
	}

	@Override
	public Response execute(Map<String, String> args) {
		return null;
	}
}
