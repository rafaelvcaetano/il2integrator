package me.magnum.il2mapintegrator.career;

import java.io.File;

import me.magnum.il2mapintegrator.career.commands.StartGame;
import me.magnum.il2mapintegrator.core.CommandProxy;
import me.magnum.il2mapintegrator.core.IService;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.career.commands.ListCampaigns;
import me.magnum.il2mapintegrator.career.commands.LoadMission;

public class CareerService implements IService {
	private CareerDatabase database;

	@Override
	public String getCode() {
		return "career";
	}

	@Override
	public String getName() {
		return "Career";
	}

	@Override
	public boolean initialize() {
		File il2BinFile = new File("bin/game/Il-2.exe");
		if (il2BinFile.isFile()) {
			this.database = new CareerDatabase();
			return true;
		} else {
			Logger.e("Could not initialize career service: game binary not found. Integration is " +
					"probably in the wrong directory.");
			return false;
		}
	}

	@Override
	public void registerCommands(CommandProxy commandProxy) {
		commandProxy.registerCommand(this, new ListCampaigns(this.database));
		commandProxy.registerCommand(this, new LoadMission(this.database));
		commandProxy.registerCommand(this, new StartGame());
	}

	@Override
	public void stop() {
		if (this.database == null)
			return;

		this.database.close();
		this.database = null;
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
