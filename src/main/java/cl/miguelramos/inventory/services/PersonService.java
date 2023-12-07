package cl.miguelramos.inventory.services;

import cl.miguelramos.inventory.entities.Person;
import cl.miguelramos.inventory.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {
  private final PersonRepository personRepository;

  @Autowired
  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Iterable<Person> findAll() {
    return personRepository.findAll();
  }

  public Optional<Person> findById(Long id) {
    return personRepository.findById(id);
  }

  public Person save(Person person) {
    return personRepository.save(person);
  }

  public void deleteById(Long id) {
    personRepository.deleteById(id);
  }
  // Métodos CRUD...
}