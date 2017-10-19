package application.scanner;

import application.alertHandler.AlertHandler;

import java.io.File;
import java.nio.file.Paths;
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
		File fileHandler = Paths.get("src","application", "scanner", "issues").toFile();
		System.out.println(fileHandler.getAbsolutePath());
		this.issueInfoFiles = new ArrayList<>();
		File[] files = fileHandler.listFiles();
		if (files != null) {
			for (File file : files) {
				try {
					this.issueInfoFiles.add(new IssueInfoFile(file.getPath()));
				} catch (Exception e) {
					AlertHandler.getInstance().addAlert("Scanner",
							"Issues infomation file is missing");
				}

			}

			issuesSourceTableModel = new IssuesSourceTableModel(issueInfoFiles);
		}
	}

	public IssueInfoFile getAtIndex(int index) throws IndexOutOfBoundsException {
		return issueInfoFiles.get(index);
	}

	public IssuesSourceTableModel getModel() {
		return issuesSourceTableModel;
	}

}
