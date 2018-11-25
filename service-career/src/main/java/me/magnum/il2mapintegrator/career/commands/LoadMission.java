package me.magnum.il2mapintegrator.career.commands;

import java.util.Map;

import me.magnum.il2mapintegrator.career.CareerDatabase;
import me.magnum.il2mapintegrator.career.DatabaseException;
import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Mission;
import me.magnum.il2mapintegrator.core.entities.Response;

public class LoadMission extends Command {
	private static final String ARG_CAMPAIGN = "campaign";

	private CareerDatabase database;

	public LoadMission(CareerDatabase database) {
		super("mission");
		this.database = database;
	}

	@Override
	public Response execute(Map<String, String> args) {
		String campaignId = args.getOrDefault(ARG_CAMPAIGN, null);
		if (campaignId == null) {
			Logger.e("Parameter " + ARG_CAMPAIGN + " is missing for LoadMission command");
			return null;
		}

		int career;
		try {
			career = Integer.valueOf(campaignId);
		} catch (NumberFormatException e) {
			Logger.e(e);
			return null;
		}

		try {
			this.database.startConnection();
		} catch (DatabaseException e) {
			Logger.e(e);
			return null;
		}
		Mission loadedMission = this.database.getMission(career);
		this.database.close();

		loadedMission.result = ErrorCodes.SUCCESS;
		loadedMission.message = "Success";

		return loadedMission;
	}
}
