package application.scanner;

import application.alertHandler.AlertHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class IssuesLoader {

	private static IssuesLoader loader;
	private ArrayList<IssueInfoFile> issueInfoFiles;
	private IssuesSourceTableModel issuesSourceTableModel;

	public static IssuesLoader getInstance() {
		if (loader == null) loader = new IssuesLoader();
		return loader;
	}

	private IssuesLoader() {
		String directory = "/application/scanner/issues/";
		String listFile = "/application/scanner/issues/issuesFile.txt";
		this.issueInfoFiles = new ArrayList<>();

		InputStreamReader input = new InputStreamReader(this.getClass().getResourceAsStream(listFile));

		BufferedReader bufferedReader = new BufferedReader(input);
		String fileName;
		try {
			while ((fileName = bufferedReader.readLine()) != null) {
				try {
					this.issueInfoFiles.add(new IssueInfoFile(directory + fileName));
				} catch (Exception e) {
					AlertHandler.getInstance().addAlert("Scanner",
							"Issues infomation file is missing");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		issuesSourceTableModel = new IssuesSourceTableModel(issueInfoFiles);

		//for fresh list
//		File fileHandler = new File("src/application/scanner/issues");
//		File[] files = fileHandler.listFiles();
//		File file1 = new File("src/application/scanner/issues/issuesFile.txt");
//		FileOutputStream out = null;
//		try {
//			file1.createNewFile();
//			out = new FileOutputStream(file1);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		if (files != null) {
//			for (File file : files) {
//				try {
//
//					//for refresh list
//					if (out != null) {
//						out.write((file.getName() + "\n").getBytes());
//						out.flush();
//					}
//				} catch (Exception ignored) {
//				}
//			}
//
//			try {
//				if (out != null) {
//					out.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public IssueInfoFile getAtIndex(int index) throws IndexOutOfBoundsException {
		return issueInfoFiles.get(index);
	}

	public IssuesSourceTableModel getModel() {
		return issuesSourceTableModel;
	}

}
