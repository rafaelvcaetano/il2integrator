package me.magnum.il2mapintegrator.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;

public class IL2Integrator {
	private List<IServiceInterface> interfaces;
	private ServiceRegistry serviceRegistry;
	private CommandProxy commandProxy;
	private boolean started;

	protected IL2Integrator(List<IServiceInterface> interfaces, ServiceRegistry serviceRegistry) {
		this.interfaces = interfaces;
		this.serviceRegistry = serviceRegistry;
		this.commandProxy = new CommandProxy();
	}

	/**
	 * Starts the integrator. This initializes all interfaces and asks every registered service to
	 * perform command registration.
	 * If an interface fails to start, initialization will still proceed.
	 * @return If initialization was successful or not.
	 */
	public boolean start() {
		for (IServiceInterface serviceInterface : this.interfaces) {
			if (serviceInterface == null) {
				Logger.w("Null interface specified");
				continue;
			}

			try {
				if (!serviceInterface.start(this)) {
					Logger.w("Interface " + serviceInterface.getClass().getSimpleName() + " failed to start");
					continue;
				}
				Logger.d("Started interface " + serviceInterface.getClass().getSimpleName() + " successfully");
			} catch (Exception e) {
				Logger.e(e);
			}
		}

		for (IService service : this.serviceRegistry.getServices()) {
			try {
				service.registerCommands(commandProxy);
			} catch (Exception e) {
				Logger.e(e);
			}
		}

		this.started = true;
		return true;
	}

	/**
	 * Processes the given command request. The command is searched in the registered services and
	 * then executed. If the command is not found among the registered services, an error response
	 * will be returned.
	 * @param request The request with the target command to be executed.
	 * @return The response returned by the command or an error message if an error occurs.
	 * @throws IllegalStateException If the integrator was not started.
	 */
	public Response processCommand(CommandRequest request) {
		if (!this.started) {
			Logger.e("Processing a command when the service was not started");
			throw new IllegalStateException("Service was not started properly");
		}

		if (request == null) {
			Logger.w("Null request");

			Response response = new Response();
			response.result = ErrorCodes.ERR_INVALID_MESSAGE;
			response.message = "Request is null";
			return response;
		}

		if (request.service == null || request.command == null) {
			Logger.w("Invalid request parameters");

			Response response = new Response();
			response.result = ErrorCodes.ERR_INVALID_MESSAGE;
			response.message = "Invalid request parameters";
			return response;
		}

		Map<String, String> args = request.args;
		if (request.args == null)
			args = new HashMap<>();

		Logger.d("Processing request:");
		Logger.d("\tService: " + request.service);
		Logger.d("\tCommand: " + request.command);
		Logger.d("\tArgs:");
		for (String arg : args.keySet()) {
			Logger.d("\t\t" + arg + " = " + args.get(arg));
		}

		if (!this.commandProxy.isCommandRegistered(request.service, request.command)) {
			Logger.w("Command " + request.command + " is not registered for service " + request.service);

			Response response = new Response();
			response.result = ErrorCodes.ERR_INVALID_COMMAND;
			response.message = "Invalid command";
			return response;
		}

		Response response;
		try {
			response = this.commandProxy.runCommand(
					request.service,
					request.command,
					args);
		} catch (Exception e) {
			Logger.e(e);

			response = new Response();
			response.result = ErrorCodes.ERR_COMMAND_ERROR;
			response.message = "Command exception: " + e.getMessage();
		}

		if (response == null) {
			Logger.w("Command error");

			response = new Response();
			response.result = ErrorCodes.ERR_COMMAND_ERROR;
			response.message = "Command error";
		}
		return response;
	}

	/**
	 * Stops the integrator. This stops all interfaces and registered services.
	 */
	public void stop() {
		if (!this.started)
			return;

		this.started = false;
		for (IServiceInterface serviceInterface : this.interfaces) {
			serviceInterface.stop();
		}
		this.serviceRegistry.stopServices();
		Logger.d("Service stopped");
	}
}
