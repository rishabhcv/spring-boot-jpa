package com.esp.jpaapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.esp.repository.PersonJpaRepository;

@SpringBootApplication
@EntityScan("com.esp.entity")
@ComponentScan(basePackages= {"com.esp.repository"})
public class SpringJpaApplication implements CommandLineRunner{
	
	Logger log = LoggerFactory.getLogger(SpringJpaApplication.class);
	
	@Autowired
	PersonJpaRepository personJpaRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
		log.info("{}", personJpaRepository.findById(10021));
	}
}
