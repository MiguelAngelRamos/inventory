package cl.miguelramos.inventory.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;



//* clase que va procesar nuestros mensajes 
@Component
public class ListenerService {
  
  private static final Logger log = LoggerFactory.getLogger(ListenerService.class);

  @KafkaListener(topics = "myTopic", groupId = "KiberGroup")
  public void listen(String message) throws InterruptedException {
    log.info("Message received {}", message); //* Imprimer el mensaje que estamos recibiendo
    //* Aqui pondriamos el codigo para enviar al api que lo necesite, puede ser feign o con restTemplate 

    //* Simulemos que esto dura 5 seg */
    Thread.sleep(5000);
  }

}
