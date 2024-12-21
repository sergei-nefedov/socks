package pers.nefedov.socks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import pers.nefedov.socks.repositories.SocksRepository;


import java.util.List;
import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
//        socksRepository.deleteAll();
    }

    @Test
    @Order(10)
    void testUploadExcelFile() {
        File excelFile = new File("src/test/resources/test.xlsx");
        given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", excelFile)
                .when()
                .post("/api/socks/batch")
                .then()
                .assertThat()
                .statusCode(200)
                .body(".", hasSize(3))
                .contentType(ContentType.JSON)
                .body("socksDtoList", isA(List.class))
                .body("[0].id", equalTo(1))
                .body("[0].color", equalTo("Красный"))
                .body("[0].cottonPercentage", equalTo(50.0F))
                .body("[1].id", equalTo(2))
                .body("[1].color", equalTo("Черный"))
                .body("[1].cottonPercentage", equalTo(75.0F))
                .body("[2].id", equalTo(3))
                .body("[2].color", equalTo("Белый"))
                .body("[2].cottonPercentage", equalTo(55.0F));
    }

    @Test
    @Order(20)
    void testIncomeSocksThatNotAlreadyInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Фиолетовый",
                        "80",
                        "255"
                ))
                .when()
                .post("/api/socks/income")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(4))
                .body("color", equalTo("Фиолетовый"))
                .body("cottonPercentage", equalTo(80.0F));
    }

    @Test
    @Order(30)
    void testIncomeSocksThatAlreadyInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Фиолетовый",
                        "80",
                        "255"
                ))
                .when()
                .post("/api/socks/income")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(4))
                .body("color", equalTo("Фиолетовый"))
                .body("cottonPercentage", equalTo(80.0F));
    }

    @Test
    @Order(40)
    void testOutcomeSocksThatAlreadyInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Фиолетовый",
                        "80",
                        "500"
                ))
                .when()
                .post("/api/socks/outcome")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(4))
                .body("color", equalTo("Фиолетовый"))
                .body("cottonPercentage", equalTo(80.0F))
                .body("quantity", equalTo(10));
    }

    @Test
    @Order(50)
    void testOutcomeSocksThatAlreadyInStockButLessThanRequested() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Фиолетовый",
                        "80",
                        "500"
                ))
                .when()
                .post("/api/socks/outcome")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        INSUFFICIENT_STORAGE.value())
                .contentType(ContentType.JSON);
    }

    @Test
    @Order(60)
    void testOutcomeSocksThatNotInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Розовый",
                        "80",
                        "1"
                ))
                .when()
                .post("/api/socks/outcome")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    @Order(70)
    void testUpdateSocksThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Желтый",
                        "90",
                        "55"
                ))
                .when()
                .put("/api/socks/4")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(4))
                .body("color", equalTo("Желтый"))
                .body("cottonPercentage", equalTo(90.0F))
                .body("quantity", equalTo(55));
    }

    @Test
    @Order(80)
    void testUpdateSocksThatNotInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "id": "%s",
                                  "color": "%s",
                                  "cottonPercentage": "%s",
                                  "quantity": "%s"
                                }
                                """,
                        "0",
                        "Желтый",
                        "100",
                        "99"
                ))
                .when()
                .put("/api/socks/9900")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        NOT_FOUND.value())
                .contentType(ContentType.JSON);
    }

    @Test
    @Order(90)
    void testUpdateColorOnlyForSocksThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("""
                                {
                                  "color": "%s"
                                }
                                """,
                        "Синий"
                ))
                .when()
                .put("/api/socks/4")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .contentType(ContentType.JSON)
                .body("id", equalTo(4))
                .body("color", equalTo("Синий"))
                .body("cottonPercentage", equalTo(90.0F))
                .body("quantity", equalTo(55));
    }

    @Test
    @Order(100)
    void testGetQuantityOfALLSocksThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .body(equalTo("855"));
    }

    @Test
    @Order(110)
    void testGetQuantityOfBlueSocksThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks?color=Синий")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .body(equalTo("55"));
    }

    @Test
    @Order(120)
    void testGetQuantityOfSocksWithCottonPercentageEqual90ThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks?cottonPercentage=90&comparison=equal")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .body(equalTo("55"));
    }

    @Test
    @Order(130)
    void testGetQuantityOfBlueSocksWithCottonPercentageLessThan90ThatInStock() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks?color=Синий&cottonPercentage=90&comparison=lessThan")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        OK.value())
                .body(equalTo("0"));
    }

    @Test
    @Order(140)
    void testGetQuantityOfBlueSocksWithInvalidCottonPercentage() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/socks?color=Синий&cottonPercentage=150&comparison=lessThan")
                .then()
                .assertThat()
                .statusCode(HttpStatus.
                        BAD_REQUEST.value());
    }
}
