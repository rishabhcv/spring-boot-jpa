# JPA using Spring Boot
This project was created for re-learning Spring-Boot and JPA concepts. I wanted to document my learning, as well as provide a small 
tutorial with this repository.

### What is JPA?
1. [Java persistence API - Vogella](http://www.vogella.com/tutorials/JavaPersistenceAPI/article.html)
2. [Java persistence API - Oracle](https://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html)

#### How does JPA/Hibernate Work?
Databases are designed with Tables/Relations. Java objects are designed using OOPS. We would want to store the data from
objects into tables and vice-versa. Earlier approaches involved writing SQL Queries. JDBC, Spring JDBC and myBatis were 
popular approaches. *How about mapping the objects directly to tables/relationships?*        
This Mapping is also called ORM - Object Relational Mapping. Before JPA, ORM was the term more commonly used to refer to these frameworks.
Thats one of the reasons, Hibernate is called a ORM framework.
JPA allows to map application classes to tables in database.     
1. *EntityManager* - Once the mappings are defined, entity manager can manage your entities. The EntityManager API creates and
removes persistent entity instances, finds entities by the entity’s primary key, and allows queries to be run on entities.
2. *JPQL (Java Persistence Query Language)* - Provides ways to write queries to execute searches against entities. 
Important thing to understand is the these are different from SQL queries. JPQL queries already understand the mappings
that are defined between entities. We can add additional conditions as needed.
3. *Criteria API* defines a Java based API to execute searches against databases.
4. *Persistence Context* : Within a persistence context, entities are managed. The EntityManager controls their lifecycle, and they can
access datastore resources.

#### JPA vs Hibernate
JPA defines the specification. It is an API. Hibernate is one of the popular implementations of JPA.     
1. Hibernate understands the mappings that we add between objects and tables. 
It ensures that data is stored/retrieved from the database based on the mappings.
2. Hibernate also provides additional features on top of JPA. But depending on them would mean a lock in to Hibernate. 
You cannot move to other JPA implementations like Toplink. [Read More](http://www.springboottutorial.com/hibernate-jpa-tutorial-with-spring-boot-starter-jpa)

### Getting Started with JPA/Hibernate
#### How to Define an Entity?

1. A class which should be persisted in a database, must be annotated with *@Entity* (javax.persistence.Entity)
2. By default, the table name corresponds to the class name. You can change this with the addition to the annotation 
*@Table(name="NEWTABLENAME")*.
3. All entity classes must define a primary key, must have a non-arg constructor and or not allowed to be final. Keys can be a single 
field or a combination of fields. JPA allows to auto-generate the primary key in the database via the *@GeneratedValue* annotation.

#### How to Define an Column?
1. By default each field is mapped to a column with the name of the field. You can change the default name via *@Column (name="newColumnName")*.
2. JPA persists by default all fields of an Entity, if fields should not be saved they must be marked with *@Transient*. 

#### How to Define Primary Key(s)?
1. Annotate with *@Id* : This identifies the unique ID of the database entry
2. *@GeneratedValue* : Together with an ID this annotation defines that this value is generated automatically.

### What is H2 Database?
H2 is an open-source lightweight Java database. It can be embedded in Java applications or run in the client-server mode. Mainly, 
H2 database can be configured to run as in-memory database, which means that data will not persist on the disk. Because of 
embedded database it is not used for production development, but mostly used for development and testing. [Read More](https://www.tutorialspoint.com/h2_database/h2_database_introduction.htm)

### How does Spring connect to the H2 Database?
Spring detects the dependency of H2 (an Embedded *DataSource*) in the pom.xml and auto configures the H2 database for the application. [Read More About Auto Configuration](
https://docs.spring.io/spring-boot/docs/current/reference/html/using-boot-auto-configuration.html)
```
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<scope>runtime</scope>
</dependency>
```
#### What is data.sql?
When H2 is configured as the database, Spring Auto Configuration also looks for a **data.sql** file in */src/main/resources* with your
SQL queries. When using JPA, we don't need to write a CREATE TABLE statement, as the table schema created automatically from the 
class annotated with *@Entity*. [Read More](http://www.springboottutorial.com/spring-boot-and-h2-in-memory-database)

### Troubleshooting

1. **Error creating bean with name 'springJpaApplication': Unsatisfied dependency expressed through field 'personJpaRepository'; 
Caused by: org.h2.jdbc.JdbcSQLException: Table "PERSON" not found; SQL statement:**                
The Componenet Scan runs only in the package where your application is located (annotated with *@SpringBootApplication*). For Spring to detect the 
Entity, you need to add *@EntityScan("com.esp.entity")* to your application, as classes annotated with *@Entity* are managed by the EntityManager
and they are not Spring Beans.


2. **Field personJpaRepository in com.esp.jpaapp.SpringJpaApplication required a bean of type 'com.esp.repository.PersonJpaRepository'
that could not be found.**      
This is similar to the above problem and the solution is trivial. Add *@ComponentScan(basePackages= {"com.esp.repository"})* to your application class.
[Read More](https://springbootdev.com/2017/11/13/what-are-the-uses-of-entityscan-and-enablejparepositories-annotations/)
