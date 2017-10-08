package application.alertHandler;

import application.view.frame.alert.AlertPanelController;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlertHandler {

	private static AlertHandler handler;
	private AlertDataModel dataModel;

	public static AlertHandler getInstance() {
		if (handler == null) handler = new AlertHandler();
		return handler;
	}

	private AlertHandler() {
		dataModel = new AlertDataModel();
	}

	public AlertDataModel getDataModel() {
		return dataModel;
	}

	public void addAlert(String tool, String message) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd MMMM yyyy");
		dataModel.addAlert(dateFormat.format(calendar.getTime()), tool, message);
		AlertPanelController.updateTable();
	}
}
