package me.magnum.il2mapintegrator.core.tests.mock;

import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;

public class DummyService implements IService {
	private boolean isInitialized;

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
		this.isInitialized = true;
		return true;
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new DummyCommand());
		commandProxy.registerCommand(this, new BadDummyCommand());
		commandProxy.registerCommand(this, new DummyMultiArgCommand());
	}

	@Override
	public void stop() {
		this.isInitialized = false;
	}

	public boolean isInitialized() {
		return this.isInitialized;
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
