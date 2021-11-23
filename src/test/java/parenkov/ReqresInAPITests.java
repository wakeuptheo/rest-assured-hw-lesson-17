package parenkov;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ReqresInAPITests {

    // внесение данных пользователя
    @Test
    void createUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\":\"Neo\", \"job\":\"Hacker\" }")
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("Neo"),
                        "job", is("Hacker")
                );
    }

    // изменение данных пользователя
    @Test
    void updateUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"name\": \"Vitalik\", \"job\": \"QA Engineer\" }")
                .when()
                .put("https://reqres.in/api/users/{id}", 7)
                .then()
                .statusCode(200)
                .body("name", is("Vitalik"),
                        "job", is("QA Engineer")
                );
    }

    // запрос информации о пользователе
    @Test
    void getUserInfo() {
        given()
                .when()
                .get("https://reqres.in/api/users/{id}", 10)
                .then()
                .statusCode(200)
                .body("data.email", equalTo("byron.fields@reqres.in"),
                        "data.first_name", equalTo("Byron"),
                        "data.last_name", equalTo("Fields")
                );
    }

    // запрос информации о несуществующем пользователе [negative]
    @Test
    void notFoundUser() {
        given()
                .when()
                .get("https://reqres.in/api/users/{id}", 33)
                .then()
                .statusCode(404);
    }

    // успешная регистрация
    @Test
    void successfulRegistration() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"eve.holt@reqres.in\", \"password\": \"123\" }")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("id", is(4)
                );
    }

    // регистрация с незаполненным email [negative]
    @Test
    void unsuccessfulRegistration() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"email\": \"\", \"password\": \"123\" }")
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error", is("Missing email or username")
                );
    }
}
