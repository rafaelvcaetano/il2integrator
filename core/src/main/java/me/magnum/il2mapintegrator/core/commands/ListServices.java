package me.magnum.il2mapintegrator.core.commands;

import java.util.ArrayList;
import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IService;
import me.magnum.il2mapintegrator.core.ServiceRegistry;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.entities.Service;
import me.magnum.il2mapintegrator.core.entities.ServiceList;

public class ListServices extends Command {
	private ServiceRegistry serviceRegistry;

	public ListServices(ServiceRegistry serviceRegistry) {
		super("services");
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public Response execute(Map<String, String> args) {
		ArrayList<Service> services = new ArrayList<>();

		for (IService publicService : this.serviceRegistry.getPublicServices()) {
			Service service = new Service();
			service.key = publicService.getCode();
			service.name = publicService.getName();
			service.versionCode = publicService.versionCode();
			service.version = publicService.version();

			services.add(service);
		}

		ServiceList serviceList = new ServiceList();
		serviceList.result = ErrorCodes.SUCCESS;
		serviceList.message = "Success";
		serviceList.services = services;

		return serviceList;
	}
}
