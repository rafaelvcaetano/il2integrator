package me.magnum.il2mapintegrator.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Service registry that stores instances of service implementations.
 */
public class ServiceRegistry {
	private List<IService> services;
	private List<IService> publicServices;

	protected ServiceRegistry() {
		this.services = new ArrayList<>();
		this.publicServices = new ArrayList<>();
	}

	/**
	 * Initializes and registers a new service in the registry. If initialization fails, the service
	 * is not added.
	 * @param service The service implementation
	 * @param isPublic If the service is considered public or not
	 */
	public void registerService(IService service, boolean isPublic) {
		for (IService registeredService : this.services) {
			if (registeredService.getCode().equals(service.getCode()))
				throw new IllegalArgumentException("Service " + service.getCode() + " is already registered");
		}

		if (!service.initialize())
			return;

		this.services.add(service);
		if (isPublic)
			this.publicServices.add(service);
	}

	/**
	 * Returns all service instances in the registry.
	 */
	public List<IService> getServices() {
		return this.services;
	}

	/**
	 * Returns all public services in the registry.
	 */
	public List<IService> getPublicServices() {
		return this.publicServices;
	}

	/**
	 * Stops all services.
	 */
	public void stopServices() {
		for (IService service : this.services) {
			service.stop();
		}
	}
}
