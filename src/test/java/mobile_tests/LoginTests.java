package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;

public class LoginTests extends AppiumConfig {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void loginPositiveTest() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        new SplashScreen(driver).goToAuthScreen();
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        ContactsScreen contactsScreen = new ContactsScreen(driver);
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_text());
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_class());
        softAssert.assertAll();
    }

}
