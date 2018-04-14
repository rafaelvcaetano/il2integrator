package me.magnum.il2mapintegrator.core.tests.mock;

import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class DummyCommand extends Command {
	public DummyCommand() {
		super("dummy");
	}

	@Override
	public Response execute(Map<String, String> args) {
		DummyResponse response = new DummyResponse();
		response.result = ErrorCodes.SUCCESS;
		response.message = "Success";
		response.dummyData = "dummy";
		return response;
	}
}
