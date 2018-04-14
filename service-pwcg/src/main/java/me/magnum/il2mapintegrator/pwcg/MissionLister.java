package me.magnum.il2mapintegrator.pwcg;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MissionLister {
	private static final String MISSION_EXTENSION = ".mission";

	public String getNextMissionForCampaign(final String campaign) {
		File missionDir = new File("data/Missions");
		if (!missionDir.isDirectory())
			return null;

		File[] missions = missionDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (!pathname.isFile())
					return false;

				String name = pathname.getName();
				String regex = "^" + campaign + " \\d{4}-\\d{1,2}-\\d{1,2}\\" + MISSION_EXTENSION;
				return name.matches(regex);
			}
		});

		ArrayList<String> missionList = new ArrayList<>();
		for (File mission : missions) {
			missionList.add(mission.getName());
		}

		Collections.sort(missionList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				String[] parts1 = o1.split(" ");
				String date1 = parts1[parts1.length - 1].split("\\.")[0];

				String[] parts2 = o2.split(" ");
				String date2 = parts2[parts2.length - 1].split("\\.")[0];

				String[] dateParts1 = date1.split("-");
				String[] dateParts2 = date2.split("-");

				int yearDiff = Integer.parseInt(dateParts2[0]) - Integer.parseInt(dateParts1[0]);
				if (yearDiff != 0) {
					return yearDiff;
				}

				int monthDiff = Integer.parseInt(dateParts2[1]) - Integer.parseInt(dateParts1[1]);
				if (monthDiff != 0) {
					return monthDiff;
				}

				int dayDiff = Integer.parseInt(dateParts2[2]) - Integer.parseInt(dateParts1[2]);
				return dayDiff;
			}
		});

		if (missionList.size() > 0)
			return missionList.get(0);
		else
			return null;
	}
}
