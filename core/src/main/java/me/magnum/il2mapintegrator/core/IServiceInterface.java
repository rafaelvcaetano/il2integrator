package me.magnum.il2mapintegrator.core;

/**
 * Defined an application for the service. An interface implementation can be exposed to the outside
 * of the application to receive command requests which are then converted and sent to the service
 * instance to be processed. The response can then be sent back to the client that made the original
 * request.
 */
public interface IServiceInterface extends IVersionable {
	/**
	 * Called on interface initialization. This is called when the service is starting.
	 * @param il2Integrator the service that manages this interface
	 * @return whether the interface could start successfully or not
	 */
	boolean start(IL2Integrator il2Integrator);

	/**
	 * Called when the interface should be stopped and perform resource cleanup. This is called when
	 * the service is stopping.
	 */
	void stop();
}
