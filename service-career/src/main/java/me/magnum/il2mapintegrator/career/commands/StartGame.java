package me.magnum.il2mapintegrator.career.commands;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.utils.StreamConsumer;

public class StartGame extends Command {
	public StartGame() {
		super("start");
	}

	@Override
	public Response execute(Map<String, String> args) {
		File gameDir = new File("bin/game");
		if (!gameDir.isDirectory()) {
			Response response = new Response();
			response.result = ErrorCodes.ERR_COMMAND_ERROR;
			response.message = "Could not find the game directory";
			return response;
		}

		Response response = new Response();
		try {
			this.launchGame(gameDir);

			response.result = ErrorCodes.SUCCESS;
			response.message = "Success";
		} catch (IOException e) {
			Logger.e(e);
			response.result = ErrorCodes.ERR_COMMAND_ERROR;
			response.message = "Failed to launch game: " + e.getMessage();
		}

		return response;
	}

	private void launchGame(File runDir) throws IOException {
		Process process = Runtime.getRuntime().exec("bin/game/Il-2.exe", null, runDir);
		// Input and error streams must be consumed. Otherwise the application will hang
		new StreamConsumer(process.getInputStream()).start();
		new StreamConsumer(process.getErrorStream()).start();
	}
}
