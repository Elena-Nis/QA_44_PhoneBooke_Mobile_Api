package mobile_tests;

import config.AppiumConfig;
import dto.ContactDtoLombok;
import dto.UserDtoLombok;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;
import static helper.RandomUtils.generateString;

public class EditContactTests extends AppiumConfig {
    UserDtoLombok user = UserDtoLombok.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();
    ContactsScreen contactsScreen;
    EditContactScreen editContactScreen;
    ContactDtoLombok contact = ContactDtoLombok.builder()
            .name("NEW_NAME" + generateString(5))
            .lastName("NEW_LastNAME" + generateString(10))
            .email(generateEmail(10))
            .phone(generatePhone(12))
            .address("NEW_ADDRESS" + generateString(8) + " app." + generatePhone(2))
            .description(generateString(15))
            .build();

    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        contactsScreen = new ContactsScreen(driver);
    }

    @Test
    public void editContactPositiveTest() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        Assert.assertTrue(editContactScreen.ispopUpDisplayed());
    }
}
