package config;

import dto.ContactDtoLombok;
import dto.TokenDto;
import dto.UserDtoLombok;
import interfaces.BaseApi;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeSuite;

import static helper.PropertiesReader.getProperty;
import static io.restassured.RestAssured.given;

public class ContactController implements BaseApi {
    protected TokenDto tokenDto;

    private RequestSpecification requestSpecWithToken;

    @BeforeSuite
    public void login() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("data.properties", "email"))
                .password(getProperty("data.properties", "password"))
                .build();
        Response response = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + LOGIN_PATH)
                .thenReturn();
        if (response.getStatusCode() == 200) {
            tokenDto = response.as(TokenDto.class);
            requestSpecWithToken = new RequestSpecBuilder()
                    .addHeader("Authorization", tokenDto.getToken())
                    .setContentType(ContentType.JSON)
                    .build();
        } else {
            System.out.println("Something went wrong, status code -->" + response.getStatusCode());
        }
    }

    protected Response getUserContactsResponse(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response updateContactResponseWithToken(ContactDtoLombok contact) {
        return given()
                .spec(requestSpecWithToken)
                .body(contact)
                .log().all()
                .when()
                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response updateContactResponse(ContactDtoLombok contact, String token) {
        return given()
                .body(contact)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .when()
                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response addNewContact(ContactDtoLombok contact) {
        return given()
                .spec(requestSpecWithToken)
                .body(contact)
                .when()
                .post(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    protected Response  deleteContactById (String contactId) {
        return given()
                .spec(requestSpecWithToken)
                .delete(BASE_URL + GET_ALL_CONTACTS_PATH + "/" + contactId)
                .thenReturn();
    }

    protected Response  deleteContactByIdWrongToken (String contactId, String token) {
        return given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete(BASE_URL + GET_ALL_CONTACTS_PATH + "/" + contactId)
                .thenReturn();
    }

    protected Response deleteContactByIdWrongURL (String contactId) {
        return given()
                .spec(requestSpecWithToken)
                .log().all()
                .when()
                .delete("https://contactapp-backend.herokuapp.com/v1/contacts/" + contactId)
                .thenReturn();

    }


}