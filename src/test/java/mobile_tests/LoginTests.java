package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import screens.AuthenticationScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;

public class LoginTests extends AppiumConfig {

    @Test
    public void loginPositiveTest() {
        System.out.println("**** driver ****" + driver);
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        System.out.println("**** email = " + user.getUsername());
        System.out.println("**** driver ****" + driver);
        new SplashScreen(driver).goToAuthScreen();
        System.out.println("**** driver ****" + driver);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
    }

}
