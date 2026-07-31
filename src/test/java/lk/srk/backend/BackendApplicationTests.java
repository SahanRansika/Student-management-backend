package lk.srk.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017/testdb",
        "spring.data.mongodb.database=testdb",
        "spring.main.allow-circular-references=true",
        "spring.main.allow-bean-definition-overriding=true"
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("✅ Application context loaded successfully!");
        System.out.println("✅ All beans are configured properly!");
    }
}