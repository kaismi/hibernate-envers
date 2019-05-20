package at.kaismi.hibernateenvers.repository;

import at.kaismi.hibernateenvers.domain.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {

}
