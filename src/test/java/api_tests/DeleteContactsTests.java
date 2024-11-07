package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import dto.TokenDto;
import io.restassured.response.Response;
import okhttp3.Request;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

import static helper.RandomUtils.*;
import static helper.RandomUtils.generateString;

public class DeleteContactsTests extends ContactController {

    String contactId;
    SoftAssert softAssert = new SoftAssert();

    @BeforeTest
    public void addNewContactBeforeDelete() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        if (response != null) {
            Response response1 = getUserContactsResponse(tokenDto.getToken());
            ContactsDto contactsDto = response1.as(ContactsDto.class);
            ContactDtoLombok[] contacts = contactsDto.getContacts();
            int indexLast = contacts.length - 1;
            contactId = contacts[indexLast].getId();
        } else {
            System.out.println("Wrong in adding contact");
        }
        ;
    }

    @Test
    public void deleteContactPositiveTest() {
        Response response = deleteContactById(contactId);
        System.out.println("**** ResponseById **** " + response.body().print());
        Response response1 = getUserContactsResponse(tokenDto.getToken());
        ContactsDto contactsDto = response1.as(ContactsDto.class);
        ContactDtoLombok[] contacts = contactsDto.getContacts();
        boolean isContactDeleted = true;
        for (ContactDtoLombok contact : contacts) {
            if (contact.getId().equals(contactId)) {
                isContactDeleted = false;
            }
        }
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertEquals(response.jsonPath().getString("message"), "Contact was deleted!");
        softAssert.assertTrue(isContactDeleted);
        softAssert.assertAll();
    }

    @Test
    public void deleteNegativeTestServerError_500() {
        Response response = deleteContactById("");
        System.out.println("**** ResponseById **** " + response.body().print());
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        softAssert.assertEquals(response.getStatusCode(), 500);
        softAssert.assertEquals(response.jsonPath().getString("message"), "Request method 'DELETE' not supported");
        softAssert.assertTrue(errorMessageDto.getError().equals("Internal Server Error"));
        softAssert.assertAll();
    }

    @Test
    public void deleteNegativeTestForbiddenAccess_403() {
        Response response = deleteContactByIdWrongToken(contactId, "");
        System.out.println("**** ResponseById **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 403);
        softAssert.assertTrue(response.getBody().asString().isEmpty());
        softAssert.assertAll();
    }

    @Test
    public void deleteNegativeTestUnauthorized_401() {
        Response response = deleteContactByIdWrongToken(contactId, tokenDto.getToken()+"1234");
        System.out.println("**** ResponseById **** " + response.body().print());
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        softAssert.assertEquals(response.getStatusCode(), 401);
        softAssert.assertTrue(errorMessageDto.getError().equals("Unauthorized"));
        softAssert.assertEquals(response.jsonPath().getString("message"), "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.");
        softAssert.assertAll();
    }

    @Test
    public void deleteNegativeTestUnauthorized_404() {
        Response response = deleteContactByIdWrongURL(contactId);
        System.out.println("**** ResponseById **** " + response.asString());
        softAssert.assertEquals(response.getStatusCode(), 404);
        softAssert.assertTrue(response.asString().contains("Application Error"));
        softAssert.assertAll();
    }

}