package mobile_tests;

import config.AppiumConfig;
import dto.UserDto;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.ErrorScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class LoginTests extends AppiumConfig {
    SoftAssert softAssert = new SoftAssert();
    AuthenticationScreen authenticationScreen;
    ContactsScreen contactsScreen;

    @BeforeMethod
    public void openLoginForm() {
        new SplashScreen(driver).goToAuthScreen();
        authenticationScreen = new AuthenticationScreen(driver);
    }

    @Test
    public void loginPositiveTest() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactsScreen = new ContactsScreen(driver);
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_text());
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_class());
        softAssert.assertTrue(contactsScreen.validateHeader());
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_unregEmail() {
        UserDto user = new UserDto(generateString(10),
                getProperty("data.properties", "password"));
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("Login or Password incorrect", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));
        softAssert.assertAll();
    }

    @Test
    public void loginNegativeTest_regEmailWrongPassword() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                "Privet123457890");
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("Login or Password incorrect", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));
        softAssert.assertAll();
    }

}
