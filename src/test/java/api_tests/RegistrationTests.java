package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.UserDto;
import dto.UserDtoLombok;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static helper.RandomUtils.*;
import static helper.PropertiesReader.getProperty;

public class RegistrationTests extends AuthenticationController {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void registrationPositiveTest() {
        UserDto user = new UserDto(generateEmail(12), "Password123!");
//        UserDtoLombok userDtoLombok = UserDtoLombok.builder()
//                .username(generateEmail(12))
//                .password("Pass123!")
//                .build();
        Assert.assertEquals(requestRegLogin(user, REGISTRATION_PATH).getStatusCode(), 200);
    }

    @Test
    public void registrationNegativeTest_wrongEmail400() {
        UserDto user = new UserDto(generateString(12), "Password123!");
        Response response = requestRegLogin(user, REGISTRATION_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("must be a well-formed email address"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }
    @Test
    public void registrationNegativeTest_duplicateUser409() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        Response response = requestRegLogin(user, REGISTRATION_PATH);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getError().equals("Conflict"));
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("User already exists"));
        softAssert.assertEquals(response.getStatusCode(), 409);
        softAssert.assertAll();
    }
}

