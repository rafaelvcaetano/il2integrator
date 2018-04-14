package me.magnum.il2mapintegrator.interfacehttp;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;
import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;

public class HttpApp extends NanoHTTPD {
	private static final HashMap<Integer, Response.IStatus> statusMapper;
	static {
		statusMapper = new HashMap<>();
		statusMapper.put(ErrorCodes.SUCCESS, Response.Status.OK);
		statusMapper.put(ErrorCodes.ERR_INVALID_MESSAGE, Response.Status.BAD_REQUEST);
		statusMapper.put(ErrorCodes.ERR_INVALID_COMMAND, Response.Status.NOT_FOUND);
		statusMapper.put(ErrorCodes.ERR_COMMAND_ERROR, Response.Status.INTERNAL_ERROR);
	}

	private IL2Integrator il2Integrator;
	private Gson gson;

	public HttpApp(IL2Integrator il2Integrator, int port) {
		super(port);
		this.il2Integrator = il2Integrator;
		this.gson = new Gson();
	}

	@Override
	public Response serve(IHTTPSession session) {
		String[] uriParts = session.getUri().substring(1).split("/");
		CommandRequest commandRequest = new CommandRequest();
		if (uriParts.length > 0)
			commandRequest.service = uriParts[0];

		if (uriParts.length > 1)
			commandRequest.command = uriParts[1];

		commandRequest.args = session.getParms();

		me.magnum.il2mapintegrator.core.entities.Response response = this.il2Integrator.processCommand(commandRequest);
		String data = this.gson.toJson(response);

		Response.IStatus status = statusMapper.getOrDefault(response.result, Response.Status.OK);
		return newFixedLengthResponse(status, "application/json", data);
	}
}
