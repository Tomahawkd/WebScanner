package application.scanner;

import java.io.*;

public class IssueInfoFile {

	private String issueName;
	private String severity;
	private String filePath;

	IssueInfoFile(String filePath) throws IOException, IndexOutOfBoundsException {

		this.filePath = filePath;

		String content = getContent();

		issueName = getLabel(content, "<h1>Issue name</h1>");
		severity = getLabel(content, "<h1>Typical severity</h1>");
	}

	String getIssueName() {
		return issueName;
	}

	String getSeverity() {
		return severity;
	}

	public String getContent() throws IOException {

		InputStreamReader input = new InputStreamReader(new FileInputStream(new File(filePath)));
		StringBuilder builder = new StringBuilder();

		BufferedReader bufferedReader = new BufferedReader(input);
		String lineTxt;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			builder.append(lineTxt);
		}

		return builder.toString();
	}

	private String getLabel(String content, String htmlLabel) throws IndexOutOfBoundsException {
		int s = content.indexOf(htmlLabel) + htmlLabel.length();
		content = content.substring(s);
		int e = content.indexOf("<h1>");
		return content.substring(0, e);
	}
}
