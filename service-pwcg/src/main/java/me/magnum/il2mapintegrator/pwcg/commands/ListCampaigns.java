package me.magnum.il2mapintegrator.pwcg.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Campaign;
import me.magnum.il2mapintegrator.core.entities.CampaignList;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.pwcg.FileUtils;
import me.magnum.il2mapintegrator.pwcg.PWCGPathProvider;

public class ListCampaigns extends Command {

	private final PWCGPathProvider pathProvider;

	public ListCampaigns(PWCGPathProvider pathProvider) {
		super("campaigns");
		this.pathProvider = pathProvider;
	}

	@Override
	public Response execute(Map<String, String> args) {
		File campaignsDir = new File(pathProvider.getCampaignsDirectoryPath());
		if (!campaignsDir.isDirectory())
			return null;

		ArrayList<Campaign> campaigns = new ArrayList<>();
		for (File file : campaignsDir.listFiles()) {
			if (!file.isDirectory() || file.getName().equals("config"))
				continue;

			Campaign campaign = new Campaign();
			campaign.name = FileUtils.getFileBaseName(file.getName());
			campaign.id = file.getName();

			campaigns.add(campaign);
		}

		CampaignList campaignList = new CampaignList();
		campaignList.result = ErrorCodes.SUCCESS;
		campaignList.message = "Success";
		campaignList.campaigns = campaigns;

		return campaignList;
	}
}
