package api_tests;

import config.AuthenticationController;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import static helper.RandomUtils.*;

import static helper.PropertiesReader.getProperty;

public class LoginTests extends AuthenticationController {

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

}
