package api_tests;

import config.AuthenticationController;
import dto.ErrorMessageDto;
import dto.UserDto;
import dto.UserDtoLombok;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;

import static helper.RandomUtils.*;

import static helper.PropertiesReader.getProperty;

public class LoginTests extends AuthenticationController {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void loginPositiveTest() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 200);
    }

    @Test
    public void loginNegativeTest_wrongPassword() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                "Qwerty123");
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeTest_wrongPasswordIsEmpty() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                "");
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeTest_wrongEmailWOAt() {
        UserDto user = new UserDto("qa_wrongmail.com",
        getProperty("data.properties", "password"));
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeTest_wrongEmailIsEmpty() {
        UserDto user = new UserDto("",
                getProperty("data.properties", "password"));
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeTest_wrongEmailUnregUser() {
        UserDto user = new UserDto(generateEmail(12),
                getProperty("data.properties", "password"));
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginNegativeTest_wrongPasswordUnregUser() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                generateString(10));
        Assert.assertEquals(requestRegLogin(user,LOGIN_PATH).getStatusCode(), 401);
    }

    @Test
    public void loginPositiveTestAlex() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("data.properties", "email"))
                .password(getProperty("data.properties", "password"))
                .build();
        //System.out.println(requestRegLogin(user, LOGIN_PATH).getStatusCode());
        Response response = requestRegLogin(user, LOGIN_PATH);
        //TokenDto tokenDto = response.as(TokenDto.class);
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertTrue(response.getBody().print().contains("token"));
        softAssert.assertAll();
    }

    //Tests Alex

    @Test
    public void loginNegativeTest_wrongPasswordAlex() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(getProperty("data.properties", "email"))
                .password("passsword")
                .build();
        Response response = requestRegLogin(user, LOGIN_PATH);
        ErrorMessageDto message = ErrorMessageDto.builder().build();
        if (response.getStatusCode() == 401) {
            message = response.as(ErrorMessageDto.class);
        }
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertTrue(message.getMessage().toString().equals("Login or Password incorrect"));
        System.out.println("--> " + message.getTimestamp());
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        softAssert.assertEquals(message.getTimestamp().split("T")[0], localDate.toString());
        softAssert.assertAll();
    }



}
