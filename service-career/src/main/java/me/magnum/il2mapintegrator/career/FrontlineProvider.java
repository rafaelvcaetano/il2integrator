package me.magnum.il2mapintegrator.career;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.entities.Point;

public class FrontlineProvider {
	private static final Pattern frontlinePattern = Pattern.compile("\\[frontline\\](.*?)\\[end\\]");
	private static final Pattern periodPattern = Pattern.compile("period=\\\"(.*?)\\\",\\\"(.*?)\\\"");
	private static final Pattern pointPattern = Pattern.compile("p=(\\d+),(\\d+)");

	public static List<Point> getFrontline(String map, String date) {
		InputStream fileStream = ClassLoader.getSystemResourceAsStream("frontlines/" + map + ".cfg");
		if (fileStream == null)
			return Collections.EMPTY_LIST;

		BufferedInputStream inputStream = new BufferedInputStream(fileStream);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		StringBuffer sb = new StringBuffer();
		String line;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			Logger.e(e);
		}

		GregorianCalendar flightDate = parseDate(date);

		List<Point> frontlinePoints = new ArrayList<>();

		String data = sb.toString();
		Matcher frontlineMatcher = frontlinePattern.matcher(data);
		if (frontlineMatcher.matches()) {
			frontlineMatcher.reset();
			while (frontlineMatcher.find()) {
				String frontlineGroup = frontlineMatcher.group();
				Matcher dateMatcher = periodPattern.matcher(frontlineGroup);
				if (!dateMatcher.find())
					continue;

				String startDate = dateMatcher.group(1);
				String endDate = dateMatcher.group(2);
				GregorianCalendar start = parseDate(startDate);
				GregorianCalendar end = parseDate(endDate);

				if (start.compareTo(flightDate) > 0 || end.compareTo(flightDate) < 0)
					continue;

				Matcher pointMatcher = pointPattern.matcher(frontlineGroup);
				while (pointMatcher.find()) {
					Point point = new Point();
					point.x = Integer.parseInt(pointMatcher.group(2));
					point.y = Integer.parseInt(pointMatcher.group(1));

					frontlinePoints.add(point);
				}
				break;
			}
		}

		return frontlinePoints;
	}

	private static GregorianCalendar parseDate(String date) {
		String[] parts = date.split("\\.");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		int day = Integer.parseInt(parts[2]);

		return new GregorianCalendar(year, month - 1, day);
	}
}
