package me.magnum.il2mapintegrator.career;

import java.util.HashMap;

public final class MapDataMapper {
	private static final HashMap<String, String> dataMapper = new HashMap<>();
	static {
		dataMapper.put("BOM", "moscow");
		dataMapper.put("BOS", "stalingrad");
		dataMapper.put("BOK", "kuban");
		dataMapper.put("BOBP", "rheinland");
	}

	private MapDataMapper() {
	}

	public static String getMapName(String mapInfo) {
		if (!dataMapper.containsKey(mapInfo))
			return null;

		return dataMapper.get(mapInfo);
	}
}
