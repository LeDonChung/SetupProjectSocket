package com.setup.server.dao.impl;

import java.util.List;

import com.setup.server.dao.PersonDAO;
import com.setup.server.entities.Gender;
import com.setup.server.entities.Person;
import com.setup.server.utils.AppUtils;

public class PersonDAOImpl implements PersonDAO {

	private static final long serialVersionUID = 1L;

	public PersonDAOImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Person> findAll() {
		try (var em = AppUtils.getEntityManager()) {
			return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
		}
	}

	@Override
	public Person save(Person person) {
		try (var em = AppUtils.getEntityManager()) {
			em.getTransaction().begin();
			em.persist(person);
			em.getTransaction().commit();
			return person;
		}
	}

	@Override
	public Person findById(int id) {
		try (var em = AppUtils.getEntityManager()) {
			return em.find(Person.class, id);
		}
	}

	@Override
	public List<Person> findByGender(Gender gender) {
		try (var em = AppUtils.getEntityManager()) {
			return em.createQuery("SELECT p FROM Person p WHERE p.gender = :gender", Person.class)
					.setParameter("gender", gender).getResultList();
		}
	}

	@Override
	public List<Person> findByGenderAndManager(Gender gender, int manager) {
		try (var em = AppUtils.getEntityManager()) {
			return em
					.createQuery("SELECT p FROM Person p WHERE p.gender = :gender AND p.manager.id = :id", Person.class)
					.setParameter("gender", gender).setParameter("id", manager).getResultList();
		}
	}
}
