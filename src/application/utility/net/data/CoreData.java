package application.utility.net.data;

import application.utility.net.Exceptions.IllegalHeaderDataException;
import application.utility.table.TableModelGenerator;

import javax.swing.table.TableModel;
import java.net.URL;

public class CoreData {

	private EditableContext request;
	private EditableContext response;

	public CoreData() {
		request = new ContextImpl(HeaderMap.REQUEST);
		response = new ContextImpl(HeaderMap.RESPONSE);
	}

	public void setURL(URL url) {
		request.setUrl(url);
	}

	public void setRequest(String requestStr) throws IllegalHeaderDataException {
		request.clear();
		request.setForm(requestStr);
	}

	public String getRequest() throws IllegalHeaderDataException {
		return request.toFormString();
	}

	public String getResponse() throws IllegalHeaderDataException {
		return response.toFormString();
	}

	public TableModel getRequestModel() {
		return TableModelGenerator.getInstance().generateHeaderModel(request.getHeader());
	}

	public TableModel getResponseModel() {
		return TableModelGenerator.getInstance().generateHeaderModel(response.getHeader());
	}

	public String getType() {
		Header contentTypeHeader = response.getHeader().get("Content-Type");
		if (contentTypeHeader != null) {
			return response.getHeader().get("Content-Type").toString();
		} else {
			return "";
		}
	}

	public String getResponseData() {
		return response.getData();
	}

	public EditableContext getRequestContext() {
		return request;
	}

	public EditableContext getResponseContext() {
		return response;
	}
}