package cl.miguelramos.inventory.repository;

import cl.miguelramos.inventory.entities.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
}
