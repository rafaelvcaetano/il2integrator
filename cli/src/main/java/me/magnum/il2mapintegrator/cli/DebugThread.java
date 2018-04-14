package me.magnum.il2mapintegrator.cli;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;

public class DebugThread extends Thread {
	private static final Pattern commandSplitPattern = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

	private Gson gson;
	private IL2Integrator il2Integrator;
	private AtomicBoolean running;

	public DebugThread(IL2Integrator il2Integrator) {
		this.il2Integrator = il2Integrator;
		this.gson = new Gson();
		this.running = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		this.running.set(true);

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
		while (this.running.get()) {
			String input;
			try {
				while (!inputReader.ready() && this.running.get()) {
					try {
						sleep(100);
					} catch (InterruptedException e) {
						Logger.e(e);
					}
				}
				if (!this.running.get())
					return;
				input = inputReader.readLine();
			} catch (IOException e) {
				Logger.e(e);
				continue;
			}

			if (input.equals("exit")) {
				this.running.set(false);
				break;
			}

			CommandRequest request = this.createRequest(input);
			if (request == null) {
				System.out.println("Invalid syntax");
				continue;
			}

			Response response = this.il2Integrator.processCommand(request);

			String responseString = this.gson.toJson(response);
			System.out.println(responseString);
		}
	}
	private CommandRequest createRequest(String data) {
		List<String> parts = new ArrayList<>();

		Matcher matcher = commandSplitPattern.matcher(data);
		while (matcher.find()) {
			String match = matcher.group(1);
			if (match.length() == 0)
				continue;

			if (match.charAt(0) == '\"')
				match = match.replace("\"", "");

			parts.add(match);
		}

		if (parts.size() < 2)
			return null;

		Map<String, String> args = new HashMap<>();
		for (int i = 2; i < parts.size(); i++) {
			String part = parts.get(i);
			String[] arg = part.split("=");
			if (arg.length != 2)
				return null;

			args.put(arg[0], arg[1]);
		}

		CommandRequest request = new CommandRequest();
		request.service = parts.get(0);
		request.command = parts.get(1);
		request.args = args;

		return request;
	}
}
