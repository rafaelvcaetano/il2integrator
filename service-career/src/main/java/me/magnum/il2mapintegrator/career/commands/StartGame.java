package me.magnum.il2mapintegrator.career.commands;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.utils.StreamConsumer;

public class StartGame extends Command {
	private static final String ARG_GAME_SOURCE = "source";

	private enum GameSource {
		STANDALONE,
		STEAM
	}

	public StartGame() {
		super("start");
	}

	@Override
	public Response execute(Map<String, String> args) {
		Response response = new Response();
		try {
			String sourceString = args.getOrDefault(ARG_GAME_SOURCE, GameSource.STANDALONE.name());
			GameSource source = GameSource.valueOf(sourceString.toUpperCase());

			switch (source) {
				case STANDALONE:
					File gameDir = new File("bin/game");
					if (!gameDir.isDirectory()) {
						response.result = ErrorCodes.ERR_COMMAND_ERROR;
						response.message = "Could not find the game directory";
						return response;
					}

					this.launchStandaloneGame(gameDir);
					break;
				case STEAM:
					this.launchSteamGame();
					break;
				default:
					throw new IllegalArgumentException(sourceString + " is not a valid source");
			}

			response.result = ErrorCodes.SUCCESS;
			response.message = "Success";
		} catch (URISyntaxException | IOException e) {
			Logger.e(e);
			response.result = ErrorCodes.ERR_COMMAND_ERROR;
			response.message = "Failed to launch game: " + e.getMessage();
		} catch (IllegalArgumentException e) {
			Logger.e(e);
			response.result = ErrorCodes.ERR_INVALID_MESSAGE;
			response.message = "Specified source is invalid";
		}

		return response;
	}

	private void launchStandaloneGame(File runDir) throws IOException {
		Process process = Runtime.getRuntime().exec("bin/game/Il-2.exe", null, runDir);
		// Input and error streams must be consumed. Otherwise the application will hang
		new StreamConsumer(process.getInputStream()).start();
		new StreamConsumer(process.getErrorStream()).start();
	}

	private void launchSteamGame() throws URISyntaxException, IOException {
		URI gameURI = new URI("steam://run/307960");
		Desktop.getDesktop().browse(gameURI);
	}
}
