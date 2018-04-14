package me.magnum.il2mapintegrator.core;

import java.io.PrintStream;
import java.util.ArrayList;

public class IL2IntegratorBuilder {
	private PrintStream loggerOutput;
	private ArrayList<IService> services;
	private ArrayList<IServiceInterface> interfaces;

	public IL2IntegratorBuilder() {
		this.services = new ArrayList<>();
		this.interfaces = new ArrayList<>();
	}

	public IL2IntegratorBuilder addService(IService service) {
		if (service == null)
			throw new IllegalArgumentException("service cannot be null");

		this.services.add(service);
		return this;
	}

	public IL2IntegratorBuilder addInterface(IServiceInterface serviceInterface) {
		if (serviceInterface == null)
			throw new IllegalArgumentException("serviceInterface cannot be null");

		this.interfaces.add(serviceInterface);
		return this;
	}

	public IL2IntegratorBuilder setLoggerOutput(PrintStream loggerOutput) {
		this.loggerOutput = loggerOutput;
		return this;
	}

	public IL2Integrator build() {
		ServiceRegistry serviceRegistry = new ServiceRegistry();
		serviceRegistry.registerService(new InternalService(serviceRegistry), false);
		for (IService iService : this.services) {
			serviceRegistry.registerService(iService, true);
		}

		if (this.loggerOutput != null)
			Logger.setOutput(this.loggerOutput);

		return new IL2Integrator(this.interfaces, serviceRegistry);
	}
}