package application.utility.net;

import application.utility.net.Exceptions.IllegalHeaderDataException;

public class DataHandler {

	private ContextImpl request;
	private ContextImpl response;
	private HTTPDataTableModel requestModel;
	private HTTPDataTableModel responseModel;

	DataHandler() {
		request = new ContextImpl(HTTPHeaderMap.REQUEST);
		requestModel = new HTTPDataTableModel(request.getHeader());
		response = new ContextImpl(HTTPHeaderMap.RESPONSE);
		responseModel = new HTTPDataTableModel(response.getHeader());
	}

	public void setRequest(String requestStr)
			throws IndexOutOfBoundsException, IllegalHeaderDataException {

		request.clear();

		String[] requestDataSet = requestStr.split("\n\n");
		String[] requestHeader = requestDataSet[0].split("\n");

		for (String line : requestHeader) {
			HTTPHeaderParser.parseHeader(request.getHeader(), line);
		}

		if (request.getHeader().getMethod().equals("POST")) {
			try {
				String requestData = requestDataSet[1];
				if (!requestData.equals("")) {
					int contentLength = requestData.getBytes().length;
					String line = "Content-Length: " + contentLength;
					HTTPHeaderParser.parseHeader(request.getHeader(), line);
				}
				request.setData(requestData.getBytes());
			} catch (IndexOutOfBoundsException ignored) {
			}
		}
	}

	public String getRequest() throws IllegalHeaderDataException {
		return request.toContextString();
	}

	public String getResponse() throws IllegalHeaderDataException {
		return response.toContextString();
	}

	public HTTPDataTableModel getRequestModel() {
		return requestModel;
	}

	public HTTPDataTableModel getResponseModel() {
		return responseModel;
	}

	public String getType() throws IllegalHeaderDataException {
		return response.getHeader().get("Content", "Content-Type");
	}

	public String getResponseData() {
		return response.getData();
	}

	ContextImpl getRequestContext() {
		return request;
	}

	public ContextImpl getResponseContext() {
		return response;
	}
}

