package cl.miguelramos.inventory.services;

import cl.miguelramos.inventory.entities.Person;
import cl.miguelramos.inventory.models.AuditDetails;
import cl.miguelramos.inventory.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;

@Service
public class PersonService {
  
  private final PersonRepository personRepository;

  @Autowired
  private KafkaTemplate<Integer, String> kafkaTemplate;

  //* Para enviar el mesaje en formato Json
  private ObjectMapper mapper = new ObjectMapper();

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

  public Person save(Person person) throws JsonProcessingException {
    Person personCreated = personRepository.save(person);
    AuditDetails details = new AuditDetails(personCreated.getId(), personCreated.getName());
    kafkaTemplate.send("myTopic", mapper.writeValueAsString(details));
    return personCreated; 
  }

  public void deleteById(Long id) {
    personRepository.deleteById(id);
  }
  // Métodos CRUD...
}