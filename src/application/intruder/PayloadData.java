package application.intruder;

import java.io.*;
import java.util.ArrayList;

public class PayloadData {

	private static PayloadData data;

	private ArrayList<String> dictName;

	public static PayloadData getInstance() {
		if (data == null) data = new PayloadData();
		return data;
	}

	private PayloadData() {
		File fileLoader = new File("src/application.scanner/issues");
		dictName = new ArrayList<>();
		File[] dictionaries = fileLoader.listFiles();
		if (dictionaries != null) {
			for (File dictionary : dictionaries) {
				String fileName = dictionary.getName();
				dictName.add(fileName.substring(0, fileName.indexOf(".pay")));
			}
		}
	}

	public ArrayList<String> getDictModel() {
		return dictName;
	}

	public ArrayList<String> getDict(String name) throws FileNotFoundException {
		ArrayList<String> dict = new ArrayList<>();
		File fileLoader = new File("src/application.scanner/issues/" + name + ".pay");
		InputStreamReader input = new InputStreamReader(new FileInputStream(fileLoader));

		BufferedReader bufferedReader = new BufferedReader(input);
		String lineTxt;
		try {
			while ((lineTxt = bufferedReader.readLine()) != null) {
				dict.add(lineTxt);
			}
		} catch (IOException ignored) {
		}

		return dict;
	}
}
