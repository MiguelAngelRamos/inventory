package cl.miguelramos.inventory.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

public class ConfigKafka {

  // *Consumer
  private Map<String, Object> consumerProps() {
    // Se crea un mapa para almacenar las propiedades de configuración del
    // consumidor de Kafka
    Map<String, Object> props = new HashMap<>();
    // Se configura la dirección del servidor de Kafka al que se conectará el
    // consumidor (especificar la dirección del broker de Kafka al que se conectará
    // el consumido)
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    // Se configura el ID del grupo al que pertenecerá el consumidor
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "KiberGroup");
    // Se habilita la confirmación automática de los mensajes procesados por el
    // consumidor
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
    // Se establece el intervalo de confirmación automática en 100 milisegundos
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
    // Se establece el tiempo de espera de sesión en 15 segundos
    props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");

    // los mensajes en kafka viajan encriptados y la clave es un integer y el
    // contenido es un string con esto deseralizo el mensaje
    // Se configura el deserializador de claves para que sea IntegerDeserializer
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);

    // Se configura el deserializador de valores del consumidor de Kafka para que
    // interprete los valores de los mensajes como cadenas de texto.
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    // Se devuelve el mapa de propiedades de configuración del consumidor
    return props;
  }

   // * Este método crea una fábrica de consumidores de Kafka
  @Bean
  public ConsumerFactory<Integer, String> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerProps());
  }
 // * Este método crea una fábrica de contenedores de Kafka para los listeners
  @Bean
  public ConcurrentKafkaListenerContainerFactory<Integer, String> kafkaListenerContainerFactory() {
    // Se crea la fábrica de contenedores
    ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
    // Se configura la fábrica de contenedores para utilizar la fábrica de
    // consumidores creada previamente
    factory.setConsumerFactory(consumerFactory());
    // Devuelve la fábrica de contenedores configurada
    return factory;
  }


  // * Producer
  private Map<String, Object> producerProps() {
    // Se crea un mapa para almacenar las propiedades de configuración
    Map<String, Object> props = new HashMap<>();

    // Se configura la dirección del servidor de Kafka
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    // Número de intentos para reintentar enviar un mensaje en caso de fallas
    props.put(ProducerConfig.RETRIES_CONFIG, 0);
    // Tamaño del lote para agrupar las solicitudes de mensajes
    props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
    // Tiempo de espera antes de enviar un lote si no está lleno
    props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
    // Tamaño del búfer en memoria para almacenar registros no enviados
    props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
    // Se establece el serializador de claves para enviar mensajes
    // Aquí se usa IntegerSerializer, lo que significa que las claves son de tipo Integer
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,IntegerSerializer.class);
    // Se establece el serializador de valores para enviar mensajes
    // Aquí se usa StringSerializer, lo que significa que los valores son cadenas de texto (String)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    // Devuelve el mapa de propiedades
    return props;
}

// Bean para crear una plantilla de Kafka
@Bean
public KafkaTemplate<Integer, String> createTemplate() {
  // Se obtienen las propiedades del productor
  Map<String, Object> senderProps = producerProps();

  // Se crea una fábrica de productores con las propiedades definidas
  ProducerFactory<Integer, String> pf = new DefaultKafkaProducerFactory<Integer, String>(senderProps);

  // Se crea una plantilla de Kafka con la fábrica de productores
  // Esta plantilla se utiliza para enviar mensajes a los temas de Kafka
  KafkaTemplate<Integer, String> template = new KafkaTemplate<>(pf);

  // Se devuelve la plantilla creada
  return template;
}

}
