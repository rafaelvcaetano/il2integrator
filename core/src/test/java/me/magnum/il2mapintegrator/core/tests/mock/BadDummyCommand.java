package me.magnum.il2mapintegrator.core.tests.mock;

import java.util.Map;

import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class BadDummyCommand extends Command {
	public BadDummyCommand() {
		super("bad");
	}

	@Override
	public Response execute(Map<String, String> args) {
		return null;
	}
}
