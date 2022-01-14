package me.magnum.il2mapintegrator.pwcg.pathproviders;

import me.magnum.il2mapintegrator.pwcg.PWCGPathProvider;

public class NewPathProvider implements PWCGPathProvider {

	@Override
	public String getRootPath() {
		return "PWCGBoS";
	}

	@Override
	public String getExecutablePath() {
		return "PWCGBoS/PWCG.exe";
	}

	@Override
	public String getCampaignsDirectoryPath() {
		return "PWCGBoS/User/Campaigns";
	}

	@Override
	public String getMissionsDirectoryPath() {
		return "data/Missions/PWCG";
	}
}
