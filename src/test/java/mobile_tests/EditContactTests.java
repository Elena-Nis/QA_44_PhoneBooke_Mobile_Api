package mobile_tests;

import config.AppiumConfig;

import data_provider.ContactDP_EditContact;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.UserDtoLombok;
import helper.HelperApiMobile;
import helper.RetryAnalyzer;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.*;

import java.util.Arrays;
import java.util.List;

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

    @Test
    public void editContactPositiveTestValidateApi(){
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("edit-" + generateString(2))
                .lastName("edit-"+generateString(10))
                .email("edit-"+generateEmail(10))
                .phone(generatePhone(12))
                .address("edit-"+generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        contactsScreen.goToEditScreen();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        List<ContactDtoLombok> contactList = Arrays.asList(contactsDto.getContacts());
        Assert.assertTrue(contactList.contains(contact));
    }

    //HW 21+22

   @Test(dataProvider = "editNewContactDPFile", dataProviderClass = ContactDP_EditContact.class, retryAnalyzer = RetryAnalyzer.class)
  //  @Test(dataProvider = "editNewContactDPFile", dataProviderClass = ContactDP_EditContact.class)
    public void editContactNegativeTests(ContactDtoLombok contact) {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        List<String> expectedMessages = Arrays.asList("must not be blank", "Phone number must contain only digits! And length min 10, max 15!");
        boolean isAnyMessageValid = false;
        for (String message : expectedMessages) {
            if (errorScreen.validateErrorMessage(message, 2)) {
                isAnyMessageValid = true;
                errorScreen.clickBtnOk();
            }
        }
        Assert.assertTrue(isAnyMessageValid);
    }

    @Test
    public void editContactNegativeTest_nameEmpty() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(0))
                .lastName("NEW_LastNAME" + generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address("NEW_ADDRESS" + generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must not be blank", 2));
    }

    @Test
    public void editContactNegativeTest_lastNameEmpty() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(0))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address("NEW_ADDRESS" + generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must not be blank", 2));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void editContactNegativeTest_emailEmpty() {
        //BUG
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateString(0))
                .phone(generatePhone(12))
                .address("NEW_ADDRESS" + generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must not be blank", 2));
    }

    @Test
    public void editContactNegativeTest_phoneEmpty() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(0))
                .address("NEW_ADDRESS" + generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("Phone number must contain only digits!", 2));
    }

    @Test
    public void editContactNegativeTest_addressEmpty() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(0))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must not be blank", 2));
    }

    @Test
    public void editContactNegativeTest_wrongEmailTwoAt() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(5)+generateEmail(5))
                .phone(generatePhone(12))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 2));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void editContactNegativeTest_wrongEmailCyr() {
        //BUG
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(5)+"Ф")
                .phone(generatePhone(12))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 2));
    }

    @Test
    public void editContactNegativeTest_wrongEmailAtBefore() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(0))
                .phone(generatePhone(12))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 2));
    }

    @Test
    public void editContactNegativeTest_wrongEmailAtAfter() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email("jkl@")
                .phone(generatePhone(12))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 2));
    }

    @Test
    public void editContactNegativeTest_wrongEmailWithoutAt() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email("jklmail.com")
                .phone(generatePhone(12))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("must be a well-formed email address", 2));
    }

    @Test
    public void editContactNegativeTest_wrongPhoneShort() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(5))
                .phone("123908790")
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("Phone number must contain only digits!", 2));
    }

    @Test
    public void editContactNegativeTest_wrongPhoneLong() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(5))
                .phone("1239087904123789")
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("Phone number must contain only digits!", 2));
    }

    @Test
    public void editContactNegativeTest_wrongPhoneNoNumber() {
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(10))
                .lastName(generateString(10))
                .email(generateEmail(5))
                .phone(generateString(11))
                .address(generateString(10))
                .description(generateString(15))
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("Phone number must contain only digits!", 2));
    }

    @Test(retryAnalyzer = RetryAnalyzer.class)
    public void editContactNegativeTest_contactExist() {
        //BUG
        contactsScreen.editContact();
        editContactScreen = new EditContactScreen(driver);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Stepa")
                .lastName("Korneev")
                .email("123@qwe.com")
                .phone("9090708007")
                .address("Saratov")
                .description("NewDesc")
                .build();
        editContactScreen.fillEditContactForm(contact);
        editContactScreen.clickBtnUpdate();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        Assert.assertTrue(errorScreen.validateErrorMessage("Phone number must contain only digits!", 2));
    }
}
