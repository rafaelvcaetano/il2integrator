package me.magnum.il2mapintegrator.core;

import me.magnum.il2mapintegrator.core.commands.ListServices;

/**
 * Internal service implementation that provides access to the list of active public services.
 */
class InternalService implements IService {
	/**
	 * The service registry from which the service listing is obtained.
	 */
	private ServiceRegistry serviceRegistry;

	/**
	 * Creates a new instance of the internal service that uses the given service registry when
	 * fetching the list of active public services.
	 * @param serviceRegistry The registry to use for service lookup
	 */
	public InternalService(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public String getCode() {
		return "internal";
	}

	@Override
	public String getName() {
		return "internal";
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new ListServices(this.serviceRegistry));
	}

	@Override
	public void stop() {
	}

	@Override
	public int versionCode() {
		return 1;
	}

	@Override
	public String version() {
		return "1.0";
	}
}
