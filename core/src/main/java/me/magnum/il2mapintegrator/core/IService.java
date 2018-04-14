package me.magnum.il2mapintegrator.core;

/**
 * Defines a service that can be provided by the application. A service is identified internally by
 * a code returned by {@link #getCode()} and also has a name returned by {@link #getName()} that can
 * be exposed to end users in a more human-readable way. A service is initialized through
 * {@link #initialize()}when the integrator starting. If initialization fails, the service is not
 * registered. If registration is successful, the service will then be asked to register its exposed
 * commands in the given command proxy in {@link #registerCommands(CommandProxy)}. When the
 * integrator service is stopping, the service will be notified through {@link #stop()} so that
 * cleanup and resource disposal can be performed.
 */
public interface IService extends IVersionable {
	/**
	 * @return the code that identifies the service.
	 */
	String getCode();

	/**
	 * @return the human-readable name of the service.
	 */
	String getName();

	/**
	 * Called when the service should perform initialization. If false is returned, the service
	 * won't be registered and, as such, its functionality won't be available.
	 * @return if the service could be properly initialized or not.
	 */
	boolean initialize();

	/**
	 * Called when the service should perform command registration. The service should register all
	 * the commands that are to be exposed to the outside.
	 * @param commandProxy the proxy in which commands should be registered.
	 */
	void registerCommands(CommandProxy commandProxy);

	/**
	 * Called when the main service is stopping indicating that the service implementation should
	 * perform cleanup and resource disposal.
	 */
	void stop();
}
