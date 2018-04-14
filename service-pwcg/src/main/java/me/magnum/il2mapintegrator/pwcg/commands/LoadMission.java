package me.magnum.il2mapintegrator.pwcg.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.commands.Command;
import me.magnum.il2mapintegrator.core.entities.Mission;
import me.magnum.il2mapintegrator.core.entities.Point;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.entities.Route;
import me.magnum.il2mapintegrator.pwcg.MissionLister;

public class LoadMission extends Command {
	private static final HashMap<String, String> translator = new HashMap<>();
	static {
		translator.put("vluki", "luki");
	}

	private static final String ARG_CAMPAIGN = "campaign";

	private MissionLister missionLister;
	private Pattern mapPattern;
	private Pattern leaderPattern;

	public LoadMission(MissionLister missionLister) {
		super("mission");
		this.missionLister = missionLister;
		this.mapPattern = Pattern.compile("GuiMap = \"(.+?)(-.*|\");");
		this.leaderPattern = Pattern.compile("Plane\\r\\n\\{.*?LinkTrId = (\\d*);", Pattern.DOTALL);
	}

	@Override
	public Response execute(Map<String, String> args) {
		String campaign = args.getOrDefault(ARG_CAMPAIGN, null);
		if (campaign == null)
			return null;

		String mission = this.missionLister.getNextMissionForCampaign(campaign);
		File missionFile = new File("data/Missions/" + mission);

		// If the mission file was not found, there probably isn't a mission for the target campaign.
		// Return a mission response without a route.
		if (!missionFile.isFile()) {
			Mission noMission = new Mission();
			noMission.result = ErrorCodes.SUCCESS;
			noMission.message = "Success";
			noMission.route = null;
			return noMission;
		}

		StringBuilder dataSB = new StringBuilder();
		char[] buffer = new char[4096];
		try {
			InputStreamReader inputStream = new InputStreamReader(new FileInputStream(missionFile));
			int readBytes;
			while ((readBytes = inputStream.read(buffer)) > 0) {
				dataSB.append(buffer, 0, readBytes);
			}

			String data = dataSB.toString();
			String map = this.getMap(data);
			if (map == null) {
				Logger.e("Could not find mission's map for mission: " + mission);
				return null;
			}
			String leaderID = this.getLeaderID(data);
			if (leaderID == null) {
				Logger.e("Could not find mission's leader for mission: " + mission);
				return null;
			}

			List<Point> waypoints = this.getPoints(data, leaderID);
			if (waypoints.isEmpty()) {
				Logger.e("No waypoints for mission: " + mission);
				return null;
			}

			Route route = new Route();
			route.map = map;
			route.points = waypoints;
			route.defend = new ArrayList<>();
			route.destroy = new ArrayList<>();

			Mission loadedMission = new Mission();
			loadedMission.result = ErrorCodes.SUCCESS;
			loadedMission.message = "Success";
			loadedMission.route = route;

			return loadedMission;
		} catch (IOException e) {
			Logger.e(e);
			return null;
		}
	}

	private String getMap(String data) {
		Matcher mapMatcher = this.mapPattern.matcher(data);
		if (!mapMatcher.find()) {
			return null;
		}

		if (mapMatcher.groupCount() < 1) {
			return null;
		}
		String map = mapMatcher.group(1);
		if (translator.containsKey(map)) {
			map = translator.get(map);
		}
		return map;
	}

	private String getLeaderID(String data) {
		Matcher leaderMatcher = this.leaderPattern.matcher(data);
		if (!leaderMatcher.find()) {
			return null;
		}

		if (leaderMatcher.groupCount() < 1) {
			return null;
		}
		return leaderMatcher.group(1);
	}

	private List<Point> getPoints(String data, String leaderID) {
		Pattern waypointPattern = Pattern.compile("MCU_Waypoint\\r\\n\\{[^}]*?Objects = \\[" + leaderID + "].*?XPos = ([0-9.]+).*?ZPos = ([0-9.]+);.*?}", Pattern.DOTALL);
		Matcher waypointMatcher = waypointPattern.matcher(data);

		ArrayList<Point> waypoints = new ArrayList<>();
		while (waypointMatcher.find()) {
			if (waypointMatcher.groupCount() < 2) {
				continue;
			}

			String xPos = waypointMatcher.group(1);
			String yPos = waypointMatcher.group(2);

			float x = Float.parseFloat(xPos);
			float y = Float.parseFloat(yPos);

			waypoints.add(this.locationToPoint(x, y));
		}

		if (waypoints.size() > 1) {
			waypoints.add(0, waypoints.get(waypoints.size() - 1));
		}

		return waypoints;
	}

	@SuppressWarnings("SuspiciousNameCombination")
	private Point locationToPoint(float x, float y) {
		Point point = new Point();
		// This is intentional. Values are represented differently in the mission editor
		point.x = (int) y;
		point.y = (int) x;
		return point;
	}
}
