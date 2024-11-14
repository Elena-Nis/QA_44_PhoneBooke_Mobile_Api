package helper;

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

public class HelperApiMobile implements BaseApi {
    TokenDto tokenDto;
    Response response;
    RequestSpecification requestSpecWithToken;

    public Response login(String email, String password) {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(email)
                .password(password)
                .build();
        return response = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + LOGIN_PATH)
                .thenReturn();
    }

    public Response registration(String email, String password) {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(email)
                .password(password)
                .build();
        return response = given()
                .body(user)
                .contentType(ContentType.JSON)
                .when()
                .post(BASE_URL + REGISTRATION_PATH)
                .thenReturn();

    }

    public void setRequestSpecWithToken() {
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

    public Response getUserContactsResponse() {
        setRequestSpecWithToken();
        return given()
                .spec(requestSpecWithToken)
                .when()
                .get(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    public Response getUserContactsResponse(String tokenDto) {
        setRequestSpecWithToken();
        return given()
                .header("Authorization", tokenDto)
                .when()
                .get(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }

    //    protected Response updateContactResponseWithToken(ContactDtoLombok contact) {
//        return given()
//                .spec(requestSpecWithToken)
//                .body(contact)
//                .log().all()
//                .when()
//                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
//                .thenReturn();
//    }
//
//    protected Response updateContactResponse(ContactDtoLombok contact, String token) {
//        return given()
//                .body(contact)
//                .header("Authorization", token)
//                .contentType(ContentType.JSON)
//                .when()
//                .put(BASE_URL + GET_ALL_CONTACTS_PATH)
//                .thenReturn();
//    }
//
    public Response addNewContact(ContactDtoLombok contact) {
        setRequestSpecWithToken();
        return given()
                .spec(requestSpecWithToken)
                .body(contact)
                .when()
                .post(BASE_URL + GET_ALL_CONTACTS_PATH)
                .thenReturn();
    }
//
//    protected Response  deleteContactById (String contactId) {
//        return given()
//                .spec(requestSpecWithToken)
//                .delete(BASE_URL + GET_ALL_CONTACTS_PATH + "/" + contactId)
//                .thenReturn();
//    }
//
//    protected Response  deleteContactByIdWrongToken (String contactId, String token) {
//        return given()
//                .header("Authorization", token)
//                .contentType(ContentType.JSON)
//                .log().all()
//                .when()
//                .delete(BASE_URL + GET_ALL_CONTACTS_PATH + "/" + contactId)
//                .thenReturn();
//    }
//
//    protected Response deleteContactByIdWrongURL (String contactId) {
//        return given()
//                .spec(requestSpecWithToken)
//                .log().all()
//                .when()
//                .delete("https://contactapp-backend.herokuapp.com/v1/contacts/" + contactId)
//                .thenReturn();
//
//    }


}