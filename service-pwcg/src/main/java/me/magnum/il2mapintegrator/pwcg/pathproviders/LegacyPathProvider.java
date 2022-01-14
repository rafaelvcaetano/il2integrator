package me.magnum.il2mapintegrator.pwcg.pathproviders;

import me.magnum.il2mapintegrator.pwcg.PWCGPathProvider;

public class LegacyPathProvider implements PWCGPathProvider {

	@Override
	public String getRootPath() {
		return "PWCGCampaign";
	}

	@Override
	public String getExecutablePath() {
		return "PWCGCampaign/PWCGBos.exe";
	}

	@Override
	public String getCampaignsDirectoryPath() {
		return "PWCGCampaign/Campaigns";
	}

	@Override
	public String getMissionsDirectoryPath() {
		return "data/Missions";
	}
}
