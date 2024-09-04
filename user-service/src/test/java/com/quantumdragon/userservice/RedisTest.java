// package com.quantumdragon.userservice;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.data.redis.core.StringRedisTemplate;

// @SpringBootTest
// public class RedisTest {

// @Autowired
// private StringRedisTemplate stringRedisTemplate;

// @Test
// public void testRedisConnection() {
// stringRedisTemplate.opsForValue().set("testKey", "testValue");
// String value = stringRedisTemplate.opsForValue().get("testKey");
// assertEquals("testValue", value);
// }
// }