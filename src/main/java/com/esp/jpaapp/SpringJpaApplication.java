package com.esp.jpaapp;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.esp.entity.Person;
import com.esp.repository.PersonJpaRepository;
import com.esp.repository.PersonSpringDataRepository;

@SpringBootApplication
@EntityScan("com.esp.entity")
@ComponentScan(basePackages= {"com.esp.repository"})
@EnableJpaRepositories("com.esp.repository")
public class SpringJpaApplication implements CommandLineRunner{
	
	Logger log = LoggerFactory.getLogger(SpringJpaApplication.class);
	
	@Autowired
	PersonJpaRepository personJpaRepository;
	
	@Autowired
	PersonSpringDataRepository personSpringDataRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		log.info("Found User 10021 -> {}", personJpaRepository.findById(10021));
		
		log.info("Inserting new user -> {}", personJpaRepository.insertPerson(new Person( "Rahul", "Ghaziabad", new Date())));
		
		log.info("Updating 10001 user -> {}", personJpaRepository.updatePerson(new Person(10001, "Rajeev", "Faridabad", new Date())));
		
		log.info("Fetching all users -> {}", personJpaRepository.findAll());
		
		personJpaRepository.deleteById(10001);
		
		
		//Executing Spring Data Queries
		
        log.info("Found User 10021 -> {}", personSpringDataRepository.findById(10021));
		
		log.info("Inserting new user -> {}", personSpringDataRepository.save(new Person( "Rohit", "Bokaro", new Date())));
		
		log.info("Updating 10001 user -> {}", personSpringDataRepository.save(new Person(10001, "Rakesh", "Sahibabad", new Date())));
		
		log.info("Fetching all users -> {}", personSpringDataRepository.findAll());
		
		personSpringDataRepository.deleteById(1);
	}
}
