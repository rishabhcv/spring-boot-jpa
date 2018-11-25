# JPA using Spring Boot
This project was created for re-learning Spring-Boot and JPA concepts. I wanted to document my learning, as well as provide a small 
tutorial with this repository.

### What is JPA?
1. [Java persistence API - Vogella](http://www.vogella.com/tutorials/JavaPersistenceAPI/article.html)
2. [Java persistence API - Oracle](https://docs.oracle.com/javaee/6/tutorial/doc/bnbpz.html)

#### How does JPA/Hibernate Work?
Databases are designed with Tables/Relations. Java objects are designed using OOPS. We would want to store the data from
objects into tables and vice-versa. Earlier approaches involved writing SQL Queries.       
JDBC, Spring JDBC and myBatis were 
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

### Getting Started with JPA/Hibernate : Defining Your Entities
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
2. *@GeneratedValue* : Together with an ID this annotation defines that this value is generated automatically. [Read More](https://www.thoughts-on-java.org/jpa-generate-primary-keys/)

```java
@Entity
public class Person {
	
	@Id
	@GeneratedValue
	private int id;
	
	@Column (name="name")
	//the above annotation is not required if the field's 
	//name matches the column name in the table
	private String name;
	private String location;
	private Date birthDate;
	
	Person(){
		
	}
	//More contructors, getters and setters, etc
}
```

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

### Defining your Repositories
A *Repository* is any class that fulfills the role or stereotype (also known as Data Access Object or DAO) of a repository. There are two ways in which we can define our repositories.        
**1. By using @*Repository* annotation :** It is a specialization of *@Component* annotation, so Spring Repository classes are autodetected by spring framework through classpath scanning. This annotation guarantees that all Spring DAO support, including the exception translation is provided in a consistent way. In addition to importing the DAOs into the DI container, it also makes the unchecked exceptions (thrown from DAO methods) eligible for translation into Spring DataAccessException. There are two advantages; first we don't have to catch any H2 or JDBC related exceptions in our DAO (even we don't have to have the knowledge of those exceptions), second if in future we want to migrate from JDBC to other technology e.g. JPA then we just need to change our DAO implementation without changing our service layer.             
```java
@Repository
@Transactional
public class PersonJpaRepository{

@PersistenceContext
EntityManager entityManager;

```
**a. @Transactional :**  A database transaction is a sequence of actions that are treated as a single unit of work. These actions should either complete entirely or take no effect at all. Transaction management is an important part of RDBMS-oriented enterprise application to ensure data integrity and consistency. By using @Transactional, many important aspects such as transaction propagation are handled automatically. [A Must Read!](https://dzone.com/articles/how-does-spring-transactional)           
**b. @PersistenceContext :** A persistence context is a set of entities such that for any persistent identity there is a unique entity instance. Within a persistence context, entities are managed. The EntityManager controls their lifecycle, and they can access datastore resources. When a persistence context ends, previously-managed entities become detached. A detached entity is no longer under the control of the EntityManager, and no longer has access to datastore resources. [Read More](https://openjpa.apache.org/builds/1.2.3/apache-openjpa/docs/jpa_overview_emfactory_perscontext.html)            
**c. EntityManager :** The EntityManager API creates and removes persistent entity instances, finds entities by the entity’s primary key, and allows queries to be run on entities. [Read More](https://www.journaldev.com/17379/jpa-entitymanager-hibernate)         



**2. By using Spring Data Reposistory interface :**
We can create an interface that extends the *JpaRepository* interface. The central interface in the Spring Data repository abstraction is *Repository*. It takes the domain class to manage as well as the ID type of the domain class as type arguments. This interface acts primarily as a marker interface to capture the types to work with and to help you to discover interfaces that extend this one. The *CrudRepository* provides sophisticated CRUD functionality for the entity class that is being managed. Spring also provides persistence technology-specific abstractions, such as *JpaRepository* or *MongoRepository*. Those interfaces extend *CrudRepository* and expose the capabilities of the underlying persistence technology in addition to the rather generic persistence technology-agnostic interfaces such as *CrudRepository*. To include this interface as a Repository, we need to add *@EnableJpaRepositories("com.esp.repository")* to our application. [Read More](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)    
```java
public interface PersonSpringDataRepository extends JpaRepository<Person, Integer> {

}
```

Also, we can mix the above two methods to create our own custom repository. [Read More](https://dzone.com/articles/add-custom-functionality-to-a-spring-data-reposito)

#### Basic Operations Inside the Repository
We can define our methods inside our Repository classes, utilising the EntityManager's methods like **find(), merge(), createNamedQuery(), etc**. [Read More](https://docs.oracle.com/javaee/7/api/javax/persistence/EntityManager.html)
```java
        //Running a named query, which resides inside the Entity class
	/*
	createNamedQuery(String name, Class<T> resultClass) 
        Parameters:
        name - the name of a query defined in metadata
        resultClass - the type of the query result
        Returns:
        the new query instance
	*/
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
	
	//To delete a row, you need to find it first
	public void deleteById(int id) {
		Person person = findById(id);
		entityManager.remove(person);
	}
```
```java
@Entity
@NamedQuery(name="find_all_persons", query="select p from Person p")
public class Person 
```


### The Application Class
Everything comes together in the Application class. The basic stuff is explained in the comments.        

**The CommandLineRunner Interface:** It is used to indicate that a bean should run when it is contained within a SpringApplication. Multiple CommandLineRunner beans can be defined within the same application context and can be ordered using the Ordered interface or @Order annotation. Command line runners are a useful functionality to execute the various types of code that only have to be run once, right after application startup. Spring Batch relies on these runners in order to trigger the execution of the jobs. We can use the dependency injection to our advantage in order to wire in whatever dependencies that we need and in whatever way we want – in run() method implementation. [Read More](https://howtodoinjava.com/spring-boot/command-line-runner-interface-example/)

```java
@SpringBootApplication //Enabling Auto Configuration, Component Scan
@EntityScan("com.esp.entity") //Entity Scan outside the package
@ComponentScan(basePackages= {"com.esp.repository"}) //Component Scan outside the package
@EnableJpaRepositories("com.esp.repository") // Will scan the package of the annotated configuration class for Spring Data repositories by default.
public class SpringJpaApplication implements CommandLineRunner{
	
	Logger log = LoggerFactory.getLogger(SpringJpaApplication.class);
	
	@Autowired
	//Injecting PersonJpaRepository which uses the EntityManager's methods.
	PersonJpaRepository personJpaRepository;   
	
	@Autowired
	//Injecting PersonSpringDataRepository which uses Spring Data's JpaRepository
	PersonSpringDataRepository personSpringDataRepository; 

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception{
	
	        // Executing PersonJpaRepository's methods
		log.info("Found User 10021 -> {}", personJpaRepository.findById(10021));
		log.info("Inserting new user -> {}", personJpaRepository.insertPerson(new Person( "Rahul", "Ghaziabad", new Date())));
		log.info("Updating 10001 user -> {}", personJpaRepository.updatePerson(new Person(10001, "Raj", "Nagpur", new Date())));
		log.info("Fetching all users -> {}", personJpaRepository.findAll());
		personJpaRepository.deleteById(10001);
		
		//Executing Spring Data Queries
                log.info("Found User 10021 -> {}", personSpringDataRepository.findById(10021));
		log.info("Inserting new user -> {}", personSpringDataRepository.save(new Person( "Rohit", "Bokaro", new Date())));
		log.info("Updating 10001 user -> {}", personSpringDataRepository.save(new Person(10001, "Sajid", "Patna", new Date())));
		log.info("Fetching all users -> {}", personSpringDataRepository.findAll());
		personSpringDataRepository.deleteById(1);
	}
}
```


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


3. **Field personSpringDataRepository in com.esp.jpaapp.SpringJpaApplication required a bean of type 'com.esp.repository.PersonSpringDataRepository' that could not be found.** The repository file we are trying to use is not annotated with *@Repository* and implements the *JpaRepository*. To include this interface as a Repository, we need to add *@EnableJpaRepositories("com.esp.repository")* to our application. This will scan Spring Data repositories in the packages specified.

### Useful Links       
1. [CrudRepository, JpaRepository and PagingAndSortingRepository](https://www.baeldung.com/spring-data-repositories)
2. [createQuery() vs createNamedQuery() vs createNativeQuery()](https://stackoverflow.com/questions/33798493/jpas-entitymanager-createquery-vs-createnamedquery-vs-createnativequery)
3. [Spring Transaction Management](https://docs.spring.io/spring/docs/4.2.x/spring-framework-reference/html/transaction.html)
<!-- 4. []()
5. []()
6. []()
7. []()
8. []()
9. []()
10. []() -->
