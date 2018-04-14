package me.magnum.il2mapintegrator.career;

import java.util.ArrayList;
import java.util.List;

import me.magnum.il2mapintegrator.core.entities.Point;

public class RouteParser {
	private RouteParser() {
	}

	public static List<Point> parseRoute(String data) {
		ArrayList<Point> points = new ArrayList<>();

		String[] lines = data.split("\\|");
		for (int i = 1; i < lines.length; i++) {
			String line = lines[i];
			String[] fields = line.split(",");

			float x = Float.parseFloat(fields[7]);
			float z = Float.parseFloat(fields[9]);

			Point point = new Point();
			// This is intentional since for some reason mission coordinates are inverted
			point.x = (int) z;
			point.y = (int) x;
			points.add(point);
		}

		return points;
	}
}
