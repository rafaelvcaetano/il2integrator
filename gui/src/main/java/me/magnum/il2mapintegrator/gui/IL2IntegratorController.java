package me.magnum.il2mapintegrator.gui;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.magnum.il2mapintegrator.core.Logger;

public class IL2IntegratorController implements IL2IntegratorWindow.LaunchActionListener {
	private final CommandInterface commandInterface;
	private final IL2IntegratorWindow window;

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
			InetAddress localAddress = getLocalHostLanAddress();
			String ip = localAddress.getHostAddress();
			this.window.setIp(ip);
		} catch (Exception e) {
			Logger.e(e);
			this.window.setIp("Error finding IP");
		}
	}

	private void checkInstalledServices() {
		List<String> services = this.commandInterface.getActiveServices();
		this.window.setLaunchIL2Enabled(services.contains("career"));
		this.window.setLaunchPWCGEnabled(services.contains("pwcg"));
	}

	private InetAddress getLocalHostLanAddress() throws UnknownHostException, SocketException {
		try (final DatagramSocket socket = new DatagramSocket()) {
			// Create a random socket to see which network interface would actually be used on a network request. The IP doesn't need to be reachable and no connection is made
			socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
			return socket.getLocalAddress();
		}
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
