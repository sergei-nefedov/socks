package pers.nefedov.socks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import pers.nefedov.socks.models.Socks;
import pers.nefedov.socks.repositories.SocksRepository;

import java.util.List;
import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SocksControllerTests {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    SocksRepository socksRepository;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        socksRepository.deleteAll();
    }
    @Test
    void shouldGetAllCustomers() {
        List<Socks> socksList = List.of(
                new Socks(null, "Черный", 87.7, 1000),
                new Socks(null, "Серый", 50.0, 150)
        );
        socksRepository.saveAll(socksList);

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks")
                .then()
                .statusCode(200)
                .body(".", hasSize(2));
    }
    @Test
    void testUploadExcelFile() {

        File excelFile = new File("src/test/resources/test.xlsx");

        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", excelFile)
                .when()
                .post("/api/socks/batch")
                .then()
                .statusCode(200)
                .body(".", hasSize(3));
    }
}
