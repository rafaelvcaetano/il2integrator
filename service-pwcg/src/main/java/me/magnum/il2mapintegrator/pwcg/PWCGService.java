package me.magnum.il2mapintegrator.pwcg;

import java.io.File;

import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;
import me.magnum.il2mapintegrator.pwcg.commands.*;

public class PWCGService implements IService {
	@Override
	public String getCode() {
		return "pwcg";
	}

	@Override
	public String getName() {
		return "PWCG";
	}

	@Override
	public boolean initialize() {
		File pwcgDir = new File("PWCGCampaign");
		return pwcgDir.isDirectory();
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new ListCampaigns());
		commandProxy.registerCommand(this, new LoadMission(new MissionLister()));
		commandProxy.registerCommand(this, new StartPWCG());
	}

	@Override
	public void stop() {
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
