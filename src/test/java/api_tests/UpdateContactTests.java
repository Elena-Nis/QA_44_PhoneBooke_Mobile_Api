package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Random;

import static helper.RandomUtils.*;

public class UpdateContactTests extends ContactController {

    ContactsDto contacts;
    SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void getContact() {
        Response response = getUserContactsResponse(tokenDto.getToken());
        if (response.getStatusCode() == 200) {
            contacts = response.as(ContactsDto.class);
        } else {
            System.out.println("Something went wrong, status code --> " + response.getStatusCode());
        }
    }


    @Test
    public void updateContactPositiveTest() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id --> " + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("new name" + generateString(5))
                .lastName("new last name" + generateString(5))
                .email("new_mail" + generateEmail(7))
                .phone("1234567890")
                .address("new address" + generateString(5))
                .description("new desc")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertTrue(getUserContactsResponse(tokenDto.getToken())
                .as(ContactsDto.class)
                .getContacts()[0].equals(contact));
        softAssert.assertAll();
    }
    @Test
    public void updateContactPositiveTestRequiredFields() {
        int number = new Random().nextInt(contacts.getContacts().length);
        System.out.println("number of contact --> "+number);
        String idUpdateContact = contacts.getContacts()[number].getId();
        System.out.println("id --> " + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("new name" + generateString(5))
                .lastName("new last name" + generateString(5))
                //.email("new_mail" + generateEmail(7))
                //.phone("1234567890")
                .address("new address" + generateString(5))
                //.description("new desc")
                .build();
        System.out.println(contact);
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertTrue(getUserContactsResponse(tokenDto.getToken())
                .as(ContactsDto.class)
                .getContacts()[number].equals(contact), "validate equals contact");
        softAssert.assertAll();
    }
    @Test
    public void updateContactNegativeTest_wrongId() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id --> " + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id("contact123")
                .name("new name" + generateString(5))
                .lastName("new last name" + generateString(5))
                .email("new_mail" + generateEmail(7))
                .phone("1234567890")
                .address("new address" + generateString(5))
                .description("new desc")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(), 400);
        ErrorMessageDto errorMessageDto = ErrorMessageDto.builder().build();
        if (response.getStatusCode() == 400) {
            errorMessageDto = response.as(ErrorMessageDto.class);
        }
        System.out.println(errorMessageDto.getError());
        softAssert.assertAll();
    }

    @Test
    public void updateContactNegativeTest_NameIsNull() {
        int number = new Random().nextInt(contacts.getContacts().length);
        System.out.println("number of contact --> "+number);
        String idUpdateContact = contacts.getContacts()[number].getId();
        System.out.println("id --> " + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .lastName("new last name" + generateString(5))
                .email("new_mail" + generateEmail(7))
                .phone("1234567890")
                .address("new address" + generateString(5))
                .description("new desc")
                .build();
        Response response = updateContactResponseWithToken(contact);
        softAssert.assertEquals(response.getStatusCode(), 400);
        ErrorMessageDto errorMessageDto = ErrorMessageDto.builder().build();
        if (response.getStatusCode() == 400) {
            errorMessageDto = response.as(ErrorMessageDto.class);
        }
        System.out.println(errorMessageDto.getError());
        softAssert.assertAll();
    }
    @Test
    public void updateContactNegativeTest_wrongToken401() {
        String idUpdateContact = contacts.getContacts()[0].getId();
        System.out.println("id --> " + idUpdateContact);
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .id(idUpdateContact)
                .name("new name" + generateString(5))
                .lastName("new last name" + generateString(5))
                .email("new_mail" + generateEmail(7))
                .phone("1234567890")
                .address("new address" + generateString(5))
                .description("new desc")
                .build();
      //  Response response = updateContactResponse(contact, "string token");
        Response response = updateContactResponse(contact, "12345");
        softAssert.assertEquals(response.getStatusCode(), 401);
        ErrorMessageDto errorMessageDto = ErrorMessageDto.builder().build();
        if (response.getStatusCode() == 401) {
            errorMessageDto = response.as(ErrorMessageDto.class);
        }
        System.out.println(errorMessageDto.getError());
        softAssert.assertAll();
    }
}
