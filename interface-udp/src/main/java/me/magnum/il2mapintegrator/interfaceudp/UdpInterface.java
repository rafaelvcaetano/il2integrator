package me.magnum.il2mapintegrator.interfaceudp;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IServiceInterface;
import me.magnum.il2mapintegrator.core.Logger;

public class UdpInterface implements IServiceInterface {
	private int port;
	private UdpServer server;

	public UdpInterface(int port) {
		this.port = port;
	}

	@Override
	public boolean start(IL2Integrator il2Integrator) {
		try {
			this.server = new UdpServer(il2Integrator, this.port);
			this.server.start();
			return true;
		} catch (IllegalThreadStateException e) {
			Logger.e(e);
			return false;
		}
	}

	@Override
	public void stop() {
		if (this.server != null)
			this.server.stopServer();
	}

	@Override
	public int versionCode() {
		return 1;
	}

	@Override
	public String version() {
		return "1.0";
	}
}
