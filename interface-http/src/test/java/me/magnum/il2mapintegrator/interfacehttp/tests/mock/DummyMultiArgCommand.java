package me.magnum.il2mapintegrator.interfacehttp.tests.mock;

import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class DummyMultiArgCommand extends Command {
	public DummyMultiArgCommand() {
		super("multi");
	}

	@Override
	public Response execute(Map<String, String> args) {
		String one = args.getOrDefault("one", null);
		String two = args.getOrDefault("two", null);
		String three = args.getOrDefault("three", null);

		if ("1".equals(one) && "2".equals(two) && "3".equals(three)) {
			Response response = new Response();
			response.result = ErrorCodes.SUCCESS;
			response.message = "Success";
			return response;
		}

		return null;
	}
}
