// package com.quantumdragon.userservice;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.kafka.core.KafkaTemplate;

// import com.quantumdragon.userservice.event.UserCreatedEvent;

// @SpringBootTest
// public class KafkaTest {

// @Autowired
// private KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

// @Test
// public void testKafkaProducer() {
// UserCreatedEvent event = new UserCreatedEvent(); // Ensure this object is
// properly initialized
// kafkaTemplate.send("test_topic", event);
// // Add more logic to verify the message consumption
// }
// }
