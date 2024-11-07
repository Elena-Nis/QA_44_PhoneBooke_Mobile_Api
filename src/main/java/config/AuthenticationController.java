package config;

import dto.UserDto;
import dto.UserDtoLombok;
import interfaces.BaseApi;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;


public class AuthenticationController implements BaseApi {

    public Response requestRegLogin(UserDto user, String url){
        return given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL+url)
                .thenReturn();
    }
    public Response requestRegLogin(UserDtoLombok user, String url){
        return given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL+url)
                .thenReturn();
    }
}