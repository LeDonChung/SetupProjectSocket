package com.setup.server.utils;

public class RequestDTO implements java.io.Serializable {
	private String request;
	private Object data;
	
	public RequestDTO() {
	}
	public RequestDTO(String request, Object data) {
		this.request = request;
		this.data = data;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResquestDTO [request=" + request + ", data=" + data + "]";
	}
	
	
	
}
