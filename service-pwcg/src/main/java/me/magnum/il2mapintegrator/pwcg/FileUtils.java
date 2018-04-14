package me.magnum.il2mapintegrator.pwcg;

public final class FileUtils {
	private FileUtils() {
	}

	public static String getFileBaseName(String filename) {
		int slashIndex = filename.lastIndexOf('/');
		int reverseSlashIndex = filename.lastIndexOf('\\');
		if (slashIndex >= 0 || reverseSlashIndex >= 0) {
			int greater = Math.max(slashIndex, reverseSlashIndex);
			filename = filename.substring(greater + 1);
		}

		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex >= 0)
			filename = filename.substring(0, dotIndex);

		return filename;
	}
}
