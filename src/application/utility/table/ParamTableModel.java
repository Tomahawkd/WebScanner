package application.utility.table;

import java.util.ArrayList;

public class ParamTableModel extends PairTableModel {

	private ArrayList<Param> paramArrayList;

	ParamTableModel(String params) {
		String[] paramList = params.split("&");
		this.paramArrayList = new ArrayList<>();
		if (paramList.length == 0) return;
		for (String param : paramList) {
			try {
				String[] paramEntry = param.split("=");
				paramArrayList.add(new Param(paramEntry[0], paramEntry[1]));
			} catch (IndexOutOfBoundsException ignored) {}
		}
	}

	@Override
	public int getRowCount() {
		return paramArrayList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Param param = paramArrayList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return param.name;
			case 1:
				return param.value;
			default:
				return null;
		}
	}

	private class Param {

		private String name;
		private String value;

		Param(String name, String value) {
			this.name = name;
			this.value = value;
		}
	}
}
