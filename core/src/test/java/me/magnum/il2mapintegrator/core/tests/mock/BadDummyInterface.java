package me.magnum.il2mapintegrator.core.tests.mock;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IServiceInterface;

public class BadDummyInterface implements IServiceInterface {
	@Override
	public boolean start(IL2Integrator il2Integrator) {
		return false;
	}

	@Override
	public void stop() {
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
