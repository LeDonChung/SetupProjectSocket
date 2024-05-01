package com.setup.server.dao;

import java.util.List;

import com.setup.server.entities.Gender;
import com.setup.server.entities.Person;

public interface PersonDAO {
	List<Person> findAll();
	Person save(Person person);
	Person findById(int id);
	List<Person> findByGender(Gender gender);
	List<Person> findByGenderAndManager(Gender gender, int manager);	
}
