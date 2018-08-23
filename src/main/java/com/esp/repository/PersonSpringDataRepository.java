package com.esp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esp.entity.Person;

public interface PersonSpringDataRepository extends JpaRepository<Person, Integer> {

}
