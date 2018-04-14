package me.magnum.il2mapintegrator.interfacehttp;

import java.io.IOException;

import me.magnum.il2mapintegrator.core.IServiceInterface;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.Logger;

public class HttpInterface implements IServiceInterface {
	private int port;
	private HttpApp app;

	public HttpInterface(int port) {
		this.port = port;
	}

	public boolean start(IL2Integrator il2Integrator) {
		try {
			this.app = new HttpApp(il2Integrator, port);
			this.app.start();
			return true;
		} catch (IOException e) {
			Logger.e(e);
			return false;
		}
	}

	@Override
	public void stop() {
		if (this.app != null)
			this.app.stop();
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
