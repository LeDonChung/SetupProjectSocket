package com.setup.server.utils;

public class ResponseDTO implements java.io.Serializable {
	private String response;
	private Object data;
	
	public ResponseDTO() {
	}
	public ResponseDTO(String response, Object data) {
		this.response = response;
		this.data = data;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseDTO [response=" + response + ", data=" + data + "]";
	}
	
	
	
	
}
