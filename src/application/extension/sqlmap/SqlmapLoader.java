package application.extension.sqlmap;

import java.io.File;

public class SqlmapLoader {

	private static SqlmapController controller;

	public static void loadExecutor(String path) throws IllegalArgumentException {
		File file = new File(path);
		if (!file.isDirectory()) throw new IllegalArgumentException("Invalid path");
		String[] list = file.list((dir, name) -> name.equals("sqlmap.py"));
		if (list == null || list.length <= 0 ) throw new IllegalArgumentException("Execution not found");
		path = path.endsWith(File.separator) ? path : path + File.separator;
		controller = new SqlmapController(path);
	}

	public static SqlmapController getController() throws IllegalArgumentException {
		if (controller == null) {
			throw new IllegalArgumentException("Executor execution path is not defined");
		}
		return controller;
	}

	public static boolean isLoaded() {
		return controller != null;
	}
}