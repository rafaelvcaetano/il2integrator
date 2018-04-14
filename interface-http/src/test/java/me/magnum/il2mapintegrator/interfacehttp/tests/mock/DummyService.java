package me.magnum.il2mapintegrator.interfacehttp.tests.mock;

import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;

public class DummyService implements IService {
	@Override
	public String getCode() {
		return "dummy";
	}

	@Override
	public String getName() {
		return "Dummy";
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new DummyMultiArgCommand());
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
