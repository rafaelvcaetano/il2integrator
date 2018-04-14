package me.magnum.il2mapintegrator.core;

import java.util.HashMap;
import java.util.Map;

import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Response;

public class CommandProxy {
	private HashMap<String, HashMap<String, Command>> commandList;

	protected CommandProxy() {
		this.commandList = new HashMap<>();
	}

	/**
	 * Registers the given command associated to the given service in the internal registry making
	 * it accessible for interfaces to use.
	 * @param service the service to which the command is associated.
	 * @param command the command to be registered.
	 * @throws IllegalStateException when the given command and service pair is already registered.
	 */
	public void registerCommand(IService service, Command command) {
		String serviceName = service.getCode();

		HashMap<String, Command> serviceCommands;
		if (this.commandList.containsKey(serviceName)) {
			serviceCommands = this.commandList.get(serviceName);
		} else {
			serviceCommands = new HashMap<>();
			this.commandList.put(serviceName, serviceCommands);
		}

		if (serviceCommands.containsKey(command.getCommand())) {
			String error = "Command " + command.getCommand() + " is already registered for service " + serviceName;
			Logger.e(error);
			throw new IllegalStateException(error);
		}

		serviceCommands.put(command.getCommand(), command);
	}

	/**
	 * Checks if the given service code has the given command registered.
	 * @param service the service to check that specified the command.
	 * @param command the command to check.
	 * @return whether the given command is associated to the given service and registered or not.
	 */
	protected boolean isCommandRegistered(String service, String command) {
		return service != null &&
			   command != null &&
			   this.commandList.containsKey(service) &&
			   this.commandList.get(service).containsKey(command);

	}

	/**
	 * Runs the given command associated with the given service supplied by the given set of
	 * arguments.
	 * @param service The key of the service that owns the command.
	 * @param command The command to be executed.
	 * @param args The set of arguments that should be supplied to the command.
	 * @return The response returned by the command.
	 */
	protected Response runCommand(String service, String command, Map<String, String> args) {
		return this.commandList.get(service).get(command).execute(args);
	}
}
