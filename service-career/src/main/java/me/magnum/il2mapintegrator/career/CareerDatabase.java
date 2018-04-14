package me.magnum.il2mapintegrator.career;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.entities.Campaign;
import me.magnum.il2mapintegrator.core.entities.Point;
import me.magnum.il2mapintegrator.core.entities.Route;

public class CareerDatabase {
	private static final String DB_CONNECTION = "jdbc:sqlite:data/Career/cp.db";

	private Connection connection;
	private SQLiteConfig dbConfig;

	public CareerDatabase() {
		this.connection = null;

		this.dbConfig = new SQLiteConfig();
		this.dbConfig.setReadOnly(true);
		this.dbConfig.setOpenMode(SQLiteOpenMode.READONLY);
	}

	public void startConnection() throws DatabaseException {
		if (this.isDatabaseOpen())
			return;

		try {
			this.connection = DriverManager.getConnection(DB_CONNECTION, this.dbConfig.toProperties());
			Logger.d("Database connection created");
		} catch (SQLException e) {
			Logger.e(e);
			throw new DatabaseException(e);
		}
	}

	private boolean isDatabaseOpen() {
		try {
			return this.connection != null && !this.connection.isClosed();
		} catch (SQLException e) {
			Logger.w(e);
			return false;
		}
	}

	public List<Campaign> getCareers() {
		if (!this.isDatabaseOpen()) {
			Logger.e("No database connection open");
			return null;
		}

		ArrayList<Campaign> careers = new ArrayList<>();

		try {
			Statement statement = this.connection.createStatement();
			if (!statement.execute("SELECT id, playerId FROM career WHERE isDeleted = 0")) {
				Logger.e("Statement execution failed");
				return null;
			}

			ResultSet results = statement.getResultSet();
			while (results.next()) {
				int careerId = results.getInt("id");
				int playerId = results.getInt("playerId");

				Statement pilotStatement = this.connection.createStatement();
				ResultSet pilotResults = pilotStatement.executeQuery("SELECT name, lastName FROM pilot WHERE id = " + playerId);
				if (pilotResults.next()) {
					String firstName = pilotResults.getString("name");
					String lastName = pilotResults.getString("lastName");

					String careerName = firstName + " " + lastName;

					Campaign campaign = new Campaign();
					campaign.id = Integer.toString(careerId);
					campaign.name = careerName;
					careers.add(campaign);
				}

				pilotStatement.close();
			}

			statement.close();
		} catch (SQLException e) {
			Logger.e(e);
		}

		return careers;
	}

	public Route getMission(int careerId) {
		if (!this.isDatabaseOpen()) {
			Logger.e("No database connection open");
			return null;
		}

		try {
			String map = null;
			int playerId = -1;

			PreparedStatement careerQuery = this.connection.prepareStatement("SELECT infoId, playerId FROM career WHERE id = ?");
			careerQuery.setInt(1, careerId);

			ResultSet careerData = careerQuery.executeQuery();
			if (careerData.next()) {
				String mapInfo = careerData.getString("infoId");
				map = MapDataMapper.getMapName(mapInfo);
				playerId = careerData.getInt("playerId");
			}

			if (map == null || playerId == -1)
				return null;

			PreparedStatement routeQuery = this.connection.prepareStatement("SELECT route FROM mission WHERE careerId = ? AND (" +
					"pilot0 = ? OR " +
					"pilot1 = ? OR " +
					"pilot2 = ? OR " +
					"pilot3 = ? OR " +
					"pilot4 = ? OR " +
					"pilot5 = ? OR " +
					"pilot6 = ? OR " +
					"pilot7 = ? OR " +
					"pilot8 = ?) AND " +
					"state = 0 AND " +
					"isDeleted = 0");
			routeQuery.setInt(1, careerId);
			for (int i = 2; i <= 10; i++) {
				routeQuery.setInt(i, playerId);
			}

			ResultSet results = routeQuery.executeQuery();

			Route route = null;
			if (results.next()) {
				String routeData = results.getString("route");
				List<Point> waypoints = RouteParser.parseRoute(routeData);

				route = new Route();
				route.map = map;
				route.points = waypoints;
				route.destroy = new ArrayList<>();
				route.defend = new ArrayList<>();
			}
			return route;
		} catch (SQLException e) {
			Logger.e(e);
		}

		return null;
	}

	public void close() {
		if (!this.isDatabaseOpen())
			return;

		try {
			this.connection.close();
			Logger.d("Database closed");
		} catch (SQLException e) {
			Logger.e(e);
		}
	}
}
