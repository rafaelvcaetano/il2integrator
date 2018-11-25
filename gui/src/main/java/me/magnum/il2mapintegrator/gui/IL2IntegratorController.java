package me.magnum.il2mapintegrator.gui;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.magnum.il2mapintegrator.core.Logger;

public class IL2IntegratorController implements IL2IntegratorWindow.LaunchActionListener {
	private CommandInterface commandInterface;
	private IL2IntegratorWindow window;

	public IL2IntegratorController(CommandInterface commandInterface) {
		this.commandInterface = commandInterface;
		this.window = new IL2IntegratorWindow(this);
		this.startService();
		this.updateIp();
		this.checkInstalledServices();
	}

	private void startService() {
		if (!this.commandInterface.startService()) {
			this.window.showErrorMessage("Failed to initialize");
			System.exit(1);
		}
	}

	private void updateIp() {
		try {
			String ip = Inet4Address.getLocalHost().getHostAddress();
			this.window.setIp(ip);
		} catch (UnknownHostException e) {
			Logger.e(e);
			this.window.setIp("Error finding IP");
		}
	}

	private void checkInstalledServices() {
		List<String> services = this.commandInterface.getActiveServices();
		this.window.setLaunchIL2Enabled(services.contains("career"));
		this.window.setLaunchPWCGEnabled(services.contains("pwcg"));
	}

	@Override
	public void onLaunchIL2() {
		Map<String, String> args = new HashMap<>();
		args.put("source", this.window.launchFromSteam() ? "steam" : "standalone");

		if (!this.commandInterface.startApplication("career", args))
			this.window.showErrorMessage("Could not launch IL-2");
	}

	@Override
	public void onLaunchPWCG() {
		if (!this.commandInterface.startApplication("pwcg"))
			this.window.showErrorMessage("Could not launch PWCG");
	}
}
