package me.magnum.il2mapintegrator.core.tests.mock;

import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;

public class BadDummyService implements IService {
	@Override
	public String getCode() {
		return "badDummy";
	}

	@Override
	public String getName() {
		return "Bad Dummy";
	}

	@Override
	public boolean initialize() {
		return false;
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
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
