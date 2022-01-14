package me.magnum.il2mapintegrator.pwcg;

import java.io.File;

import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.pwcg.commands.*;
import me.magnum.il2mapintegrator.pwcg.pathproviders.LegacyPathProvider;
import me.magnum.il2mapintegrator.pwcg.pathproviders.NewPathProvider;

public class PWCGService implements IService {

	private PWCGPathProvider pathProvider;

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
		PWCGPathProvider bestPathProvider = findBestPathProvider();
		pathProvider = bestPathProvider;

		boolean foundPwcg = bestPathProvider != null;
		if (foundPwcg) {
			Logger.d("Found PWCG installation at " + pathProvider.getRootPath());
		} else {
			Logger.w("Did not find any PWCG installation");
		}

		return foundPwcg;
	}

	private PWCGPathProvider findBestPathProvider() {
		File pwcgDir = new File("PWCGBoS");
		if (pwcgDir.isDirectory()) {
			Logger.d("Found new PWCG");
			return new NewPathProvider();
		}

		File legacyPwcgDir = new File("PWCGCampaign");
		if (legacyPwcgDir.isDirectory()) {
			Logger.d("Found legacy PWCG");
			return new LegacyPathProvider();
		}

		return null;
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new ListCampaigns(this.pathProvider));
		commandProxy.registerCommand(this, new LoadMission(new MissionLister(this.pathProvider), this.pathProvider));
		commandProxy.registerCommand(this, new StartPWCG(this.pathProvider));
	}

	@Override
	public void stop() {
	}

	@Override
	public int versionCode() {
		return 2;
	}

	@Override
	public String version() {
		return "2.0";
	}
}
