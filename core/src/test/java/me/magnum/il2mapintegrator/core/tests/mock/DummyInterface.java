package me.magnum.il2mapintegrator.core.tests.mock;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IServiceInterface;

public class DummyInterface implements IServiceInterface {
	private boolean isStarted;

	@Override
	public boolean start(IL2Integrator il2Integrator) {
		this.isStarted = true;
		return true;
	}

	@Override
	public void stop() {
		this.isStarted = false;
	}

	public boolean isStarted() {
		return this.isStarted;
	}

	@Override
	public int versionCode() {
		return 0;
	}

	@Override
	public String version() {
		return "1.0";
	}
}
