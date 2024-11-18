package mobile_tests;

import config.AppiumConfig;
import dto.*;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.ErrorScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class RegistrationTests extends AppiumConfig {
    SoftAssert softAssert = new SoftAssert();
    AuthenticationScreen authenticationScreen;

    @BeforeMethod
    public void registrationTests() {
        new SplashScreen(driver).goToAuthScreen();
        authenticationScreen = new AuthenticationScreen(driver);
    }

    @Test
    public void registrationPositiveTest() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(generateEmail(10))
                .password("Qwerty123!")
                .build();
        ContactsScreen contactsScreen = new ContactsScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        softAssert.assertTrue(contactsScreen.isTextPopUpRresent());
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_class());
        softAssert.assertTrue(contactsScreen.isElementContactListPresent_text());
        softAssert.assertTrue(contactsScreen.validateHeader());
        softAssert.assertAll();
    }

    @Test
    public void registrationNegativeTest_wrongEmail() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(generateString(10))
                .password("Qwerty123!")
                .build();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));
        softAssert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 5));
        softAssert.assertAll();
    }

    @Test
    public void registrationNegativeTest_wrongPassword() {
        UserDtoLombok user = UserDtoLombok.builder()
                .username(generateEmail(10))
                .password("Qwerty123")
                .build();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));
        softAssert.assertTrue(errorScreen.validateErrorMessage("password= At least 8 characters", 5));
        softAssert.assertAll();
    }

    // **************** HW19 ******************

    @Test
    public void registrationNegativeTest_registeredUserAPI() {
        UserDto user = new UserDto(getProperty("data.properties", "email"),
                getProperty("data.properties", "password"));
        //UI
        ErrorScreen errorScreen = new ErrorScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnRegistration();
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));
        softAssert.assertTrue(errorScreen.validateErrorMessage("User already exists", 5));
        //API
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        Response responseRegistration = helperApiMobile.registration(user.getUsername(), user.getPassword());
        ErrorMessageDto errorMessageDto = responseRegistration.as(ErrorMessageDto.class);
        System.out.println(errorMessageDto);
        softAssert.assertTrue(errorMessageDto.getError().equals("Conflict"));
        softAssert.assertTrue(errorMessageDto.getMessage().toString().contains("User already exists"));
        softAssert.assertEquals(responseRegistration.getStatusCode(), 409);

        softAssert.assertAll();
    }




}