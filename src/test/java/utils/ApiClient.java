package utils;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ApiClient {

    private static String base() {
        return ConfigReader.get("baseUrl") + "/services/bank";
    }

    public static Response login(String username, String password) {
        return given()
                .baseUri(base())
                .accept(ContentType.JSON)
                .when()
                .get("/login/{u}/{p}", username, password);
    }

    public static Response accounts(int customerId) {
        return given()
                .baseUri(base())
                .accept(ContentType.JSON)
                .when()
                .get("/customers/{id}/accounts", customerId);
    }
}
