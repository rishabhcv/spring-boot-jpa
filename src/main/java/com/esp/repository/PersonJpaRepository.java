package com.esp.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.esp.entity.Person;

@Repository
@Transactional
public class PersonJpaRepository {
	
	
	@PersistenceContext
	EntityManager entityManager;
	
	//FindAll method is created using a named query
	public List<Person> findAll(){
		TypedQuery<Person> namedQuery = entityManager.createNamedQuery(
				"find_all_persons", Person.class);
	    return namedQuery.getResultList();
	}
	
	
	//Find an entry using the Primary Key, Id
	public Person findById(int id) {
		return entityManager.find(Person.class, id);
	}
	
	
	//Insert a new row into the DB
	public Person insertPerson(Person person) {
		return entityManager.merge(person);
	}
	
	//This method is the same as above. For the EntityManager
	//insert and update are essentially the same. We however pass an 
	//existing Id and the record would get updated
	public Person updatePerson(Person person) {
		return entityManager.merge(person);
	}
	
	
	//To delete a row, you need to find it first
	public void deleteById(int id) {
		Person person = findById(id);
		entityManager.remove(person);
	}

}
