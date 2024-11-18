package mobile_tests;

import config.AppiumConfig;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.UserDtoLombok;
import helper.HelperApiMobile;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import screens.AddNewContactsScreen;
import screens.AuthenticationScreen;
import screens.ContactsScreen;
import screens.SplashScreen;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;
import static helper.RandomUtils.generateString;

public class DeleteContactTest extends AppiumConfig {
    UserDtoLombok user = UserDtoLombok.builder()
            .username(getProperty("data1.properties", "email"))
            .password(getProperty("data1.properties", "password"))
            .build();
    ContactsScreen contactsScreen;
    AddNewContactsScreen addNewContactsScreen;
    ContactDtoLombok contact = ContactDtoLombok.builder()
            .name(generateString(5))
            .lastName(generateString(10))
            .email(generateEmail(10))
            .phone(generatePhone(12))
            .address(generateString(8) + " app." + generatePhone(2))
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
    public void deleteContactPositiveTest() {
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        int quantityContactsBeforeDelete = contactsDto.getContacts().length;
        contactsScreen.deleteContact();
        contactsScreen.clickBtnYes();
        Response responseGetAfter = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDtoAfter = responseGetAfter.as(ContactsDto.class);
        int quantityContactsAfterDelete = contactsDtoAfter.getContacts().length;
        System.out.println(quantityContactsBeforeDelete + "X" + quantityContactsAfterDelete);
    }

    @Test
    public void deleteContactP0sitiveTest_withoutAPI() {
       //Adding new contact
        contactsScreen.clickBtnAddNewContact();
        addNewContactsScreen = new AddNewContactsScreen(driver);
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        //Let's scroll down and count quantity of contacts before deleting
        int quantityContactsBeforeDelete = contactsScreen.getAllNamesPhones();
        System.out.println("***** quantityContactsBeforeDelete UI ***** " + quantityContactsBeforeDelete);

        //Confirm through API that UI calculated correctly
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        int quantityContactsBeforeDeleteAPI = contactsDto.getContacts().length;
        System.out.println("$$$$$ quantityContactsBeforeDelete API $$$$$ " + quantityContactsBeforeDeleteAPI);

        //Delete contact
        contactsScreen.deleteContact();
        contactsScreen.clickBtnYes();
        //Let's scroll down and count quantity of contacts after deleting
        int quantityContactsAfterDelete = contactsScreen.getAllNamesPhones();
        System.out.println("***** quantityContactsAfterDelete UI ***** " + quantityContactsAfterDelete);

        //Confirm through API that UI calculated correctly
        Response responseGetAfter = helperApiMobile.getUserContactsResponse();
         contactsDto = responseGetAfter.as(ContactsDto.class);
        int quantityContactsAfterDeleteAPI = contactsDto.getContacts().length;
        System.out.println("$$$$$ quantityContactsBeforeDelete API $$$$$ " + quantityContactsAfterDeleteAPI);


        Assert.assertEquals(quantityContactsAfterDelete, quantityContactsBeforeDelete - 1);
    }

}