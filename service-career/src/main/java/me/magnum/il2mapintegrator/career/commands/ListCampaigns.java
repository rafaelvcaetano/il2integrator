package me.magnum.il2mapintegrator.career.commands;

import java.util.List;
import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.career.CareerDatabase;
import me.magnum.il2mapintegrator.career.DatabaseException;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Campaign;
import me.magnum.il2mapintegrator.core.entities.CampaignList;
import me.magnum.il2mapintegrator.core.entities.Response;

public class ListCampaigns extends Command {
	private CareerDatabase database;

	public ListCampaigns(CareerDatabase database) {
		super("campaigns");
		this.database = database;
	}

	@Override
	public Response execute(Map<String, String> args) {
		try {
			this.database.startConnection();
		} catch (DatabaseException e) {
			Logger.e(e);
			return null;
		}

		List<Campaign> careers = this.database.getCareers();
		this.database.close();

		CampaignList careerList = new CampaignList();
		careerList.result = ErrorCodes.SUCCESS;
		careerList.message = "Success";
		careerList.campaigns = careers;

		return careerList;
	}
}
