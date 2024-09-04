// package com.quantumdragon.accountservice.config;

// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.clients.producer.ProducerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.apache.kafka.common.serialization.StringSerializer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.annotation.EnableKafka;
// import
// org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaProducerFactory;
// import org.springframework.kafka.core.KafkaTemplate;
// import org.springframework.kafka.core.ProducerFactory;
// import
// org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
// import org.springframework.kafka.support.serializer.JsonDeserializer;
// import org.springframework.kafka.support.serializer.JsonSerializer;

// import com.quantumdragon.accountservice.event.UserCreatedEvent;

// import java.util.HashMap;
// import java.util.Map;

// @Configuration
// @EnableKafka
// public class KafkaConfig {

// @Bean
// public ProducerFactory<String, UserCreatedEvent> producerFactory() {
// Map<String, Object> configProps = new HashMap<>();
// configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
// configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
// StringSerializer.class);
// configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
// JsonSerializer.class);
// return new DefaultKafkaProducerFactory<>(configProps);
// }

// @Bean
// public KafkaTemplate<String, UserCreatedEvent> kafkaTemplate() {
// return new KafkaTemplate<>(producerFactory());
// }

// @Bean
// public ConsumerFactory<String, UserCreatedEvent> consumerFactory() {
// Map<String, Object> props = new HashMap<>();
// props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
// props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
// StringDeserializer.class);
// props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
// ErrorHandlingDeserializer.class.getName());
// props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
// JsonDeserializer.class.getName());
// props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
// UserCreatedEvent.class.getName());
// props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
// return new DefaultKafkaConsumerFactory<>(props);
// }

// @Bean
// public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent>
// kafkaListenerContainerFactory() {
// ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> factory =
// new ConcurrentKafkaListenerContainerFactory<>();
// factory.setConsumerFactory(consumerFactory());
// return factory;
// }
// }
