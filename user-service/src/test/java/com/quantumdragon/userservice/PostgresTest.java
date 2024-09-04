// package com.quantumdragon.userservice;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.jdbc.core.JdbcTemplate;

// @SpringBootTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// public class PostgresTest {

//     @Autowired
//     private JdbcTemplate jdbcTemplate;

//     @Test
//     public void testPostgresConnection() {
//         Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
//         assertEquals(1, result);
//     }
// }

