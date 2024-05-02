package by.bsuir.controllers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class EditorControllerTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 24110;
    }

    @Test
    public void testGetEditors() {
        given()
                .when()
                .get("/api/v1.0/editors")
                .then()
                .statusCode(200);
    }

    @Test
    public void testGetEditorById() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"editor2019\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(201)
                .extract().response();

        long editorId = response.jsonPath().getLong("id");
        given()
                .pathParam("id", editorId)
                .when()
                .get("/api/v1.0/editors/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteEditor() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"editor2019\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(201)
                .extract().response();

        long editorId = response.jsonPath().getLong("id");

        given()
                .pathParam("id", editorId)
                .when()
                .delete("/api/v1.0/editors/{id}")
                .then()
                .statusCode(204);
    }

    @Test
    public void testSaveEditor() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"editor2019\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(201);
    }

    @Test
    public void testUpdateEditor() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"editor2019\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(201)
                .extract().response();

        long editorId = response.jsonPath().getLong("id");

        String body = "{ \"id\": " + editorId + ", \"login\": \"updatedEditor109\", \"password\": \"updatedPass5907\", \"firstname\": \"updatedFirstname7007\", \"lastname\": \"updatedLastname3768\" }";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .put("/api/v1.0/editors")
                .then()
                .statusCode(200)
                .body("login", equalTo("updatedEditor109"));
    }

    @Test
    public void testGetEditorByIdWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/api/v1.0/editors/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Editor not found!"))
                .body("errorCode", equalTo(40004));
    }

    @Test
    public void testDeleteEditorWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .delete("/api/v1.0/editors/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("The editor has not been deleted"))
                .body("errorCode", equalTo(40003));
    }

    @Test
    public void testSaveEditorWithWrongLogin() {
        given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"x\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetEditorByIssueId() {
        Response editorResponse = given()
                .contentType(ContentType.JSON)
                .body("{ \"login\": \"editor2019\", \"password\": \"pass6459\", \"firstname\": \"firstname4155\", \"lastname\": \"lastname7290\" }")
                .when()
                .post("/api/v1.0/editors")
                .then()
                .statusCode(201)
                .extract().response();

        long editorId = editorResponse.jsonPath().getLong("id");
        String body = "{ \"editorId\": "+ editorId +", \"title\": \"title3190\", \"content\": \"content9594\" }";

        Response issueResponse = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/v1.0/issues")
                .then()
                .statusCode(201)
                .extract().response();

        long issueId = issueResponse.jsonPath().getLong("id");

        given()
                .pathParam("id", issueId)
                .when()
                .get("/api/v1.0/editors/byIssue/{id}")
                .then()
                .statusCode(200)
                .body("login", equalTo("editor2019"));
    }

    @Test
    public void testGetEditorByIssueIdWithWrongArgument() {
        given()
                .pathParam("id", 999999)
                .when()
                .get("/api/v1.0/editors/byIssue/{id}")
                .then()
                .statusCode(400)
                .body("errorMessage", equalTo("Issue not found!"))
                .body("errorCode", equalTo(40004));
    }
}
