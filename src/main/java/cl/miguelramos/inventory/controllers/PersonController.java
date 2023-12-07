package cl.miguelramos.inventory.controllers;

import cl.miguelramos.inventory.entities.Person;
import cl.miguelramos.inventory.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// La anotación @RestController indica que esta clase es un controlador REST.
// Esto significa que se encarga de manejar las solicitudes HTTP para la API.
@RestController
// @RequestMapping define la ruta base para todos los endpoints en este controlador.
// En este caso, todos los endpoints empezarán con '/api/persons'.
@RequestMapping("/api/persons")
public class PersonController {

  // Inyección de la dependencia PersonService.
  // Spring automáticamente instanciará y pasará una instancia de PersonService aquí.
  private final PersonService personService;

  // @Autowired es utilizado para la inyección de dependencias.
  // Aquí, Spring inyecta el servicio PersonService en este controlador.
  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  // Manejador de solicitudes GET para obtener todas las personas.
  // Al llamar a este endpoint con GET, se devolverá una lista de todas las personas.
  @GetMapping
  public ResponseEntity<Iterable<Person>> getAllPersons() {
    // ResponseEntity envuelve la respuesta y permite controlar aspectos como el código de estado HTTP.
    return ResponseEntity.ok(personService.findAll());
  }

  // Manejador de solicitud GET para obtener una persona por su ID.
  // El '{id}' en @GetMapping es un parámetro de la ruta que se reemplaza con el ID real en la URL.
  @GetMapping("/{id}")
  public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
    // Se utiliza el servicio para buscar la persona por ID.
    // Si se encuentra, se devuelve, de lo contrario, se devuelve un estado 404 Not Found.
    return personService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  // Manejador de solicitud POST para crear una nueva persona.
  // @RequestBody indica que un objeto Person será enviado en el cuerpo de la solicitud.
  @PostMapping
  public ResponseEntity<Person> createPerson(@RequestBody Person person) {
    // Guarda la nueva persona y devuelve el objeto guardado.
    return ResponseEntity.ok(personService.save(person));
  }

  // Manejador de solicitud PUT para actualizar una persona existente.
  // Aquí también se utiliza @PathVariable para capturar el ID de la persona a actualizar.
  @PutMapping("/{id}")
  public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
    // Se verifica primero si la persona con el ID dado existe.
    return personService.findById(id)
            .map(existingPerson -> {
              // Si existe, se actualiza con los nuevos datos y se guarda.
              person.setId(id);
              return ResponseEntity.ok(personService.save(person));
            })
            // Si no existe, se devuelve un estado 404 Not Found.
            .orElse(ResponseEntity.notFound().build());
  }

  // Manejador de solicitud DELETE para eliminar una persona por su ID.
  @DeleteMapping("/{id}")
  public ResponseEntity<?> deletePerson(@PathVariable Long id) {
    // Similar al método GET, primero se verifica si la persona existe.
    return personService.findById(id)
            .map(person -> {
              // Si existe, se elimina y se devuelve un estado 200 OK.
              personService.deleteById(id);
              return ResponseEntity.ok().build();
            })
            // Si no existe, se devuelve un estado 404 Not Found.
            .orElse(ResponseEntity.notFound().build());
  }
}
