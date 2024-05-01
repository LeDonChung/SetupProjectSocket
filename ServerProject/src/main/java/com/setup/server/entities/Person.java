package com.setup.server.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "persons")
public class Person implements Serializable{
	@Id
	@Column(name = "person_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	// Nhiều người được quản lý bởi một người
	@ManyToOne
	@JoinColumn(name = "manager_id")
	private Person manager;
	
	// Thuộc tính enum
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	// Thuộc tính đa trị: chỉ có string, int, ...
	// nếu là đối tượng thì là OneToMany
	// C1: @ElementCollection
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "phones", joinColumns = @JoinColumn(name = "person_id"))
	@Column(name = "phone")
	private List<String> phones;

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", manager=" + manager + ", gender=" + gender + ", phones="
				+ phones + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	
	
}