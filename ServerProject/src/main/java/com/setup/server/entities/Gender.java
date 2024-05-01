package com.setup.server.entities;

public enum Gender implements java.io.Serializable{
	MALE("Male"), FEMALE("Female"), ORTHER("Other");
	private String name;

	private Gender(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	

}
