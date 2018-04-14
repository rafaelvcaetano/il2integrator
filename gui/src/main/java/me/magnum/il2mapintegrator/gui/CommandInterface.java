package me.magnum.il2mapintegrator.gui;

import java.util.ArrayList;
import java.util.List;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.entities.Service;
import me.magnum.il2mapintegrator.core.entities.ServiceList;

public class CommandInterface {
	private IL2Integrator il2Integrator;

	public CommandInterface(IL2Integrator il2Integrator) {
		this.il2Integrator = il2Integrator;
		if (this.il2Integrator == null)
			throw new IllegalArgumentException("il2service cannot be null");
	}

	public boolean startService() {
		return this.il2Integrator.start();
	}

	public List<String> getActiveServices() {
		CommandRequest request = new CommandRequest();
		request.service = "internal";
		request.command = "services";

		ServiceList response = (ServiceList) this.il2Integrator.processCommand(request);
		if (response == null)
			return null;

		List<String> serviceKeys = new ArrayList<>();
		for (Service service : response.services) {
			serviceKeys.add(service.key);
		}

		return serviceKeys;
	}

	public boolean startApplication(String serviceName) {
		CommandRequest request = new CommandRequest();
		request.service = serviceName;
		request.command = "start";

		Response response = this.il2Integrator.processCommand(request);
		return response.result == ErrorCodes.SUCCESS;
	}
}
