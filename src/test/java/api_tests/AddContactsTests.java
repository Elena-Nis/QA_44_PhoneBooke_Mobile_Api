package api_tests;

import config.ContactController;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import dto.ResponseMessageDto;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.LocalDate;
import java.util.Map;

import static helper.RandomUtils.*;

public class AddContactsTests extends ContactController {

    SoftAssert softAssert = new SoftAssert();

    @Test
    public void addNewContactPositiveTest() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertTrue(response.getBody().asString().contains("Contact was added!"));
        Response response1 = getUserContactsResponse(tokenDto.getToken());
        ContactsDto contactsDto = response1.as(ContactsDto.class);
        ContactDtoLombok[] contacts = contactsDto.getContacts();
        int indexLast = contacts.length - 1;
        softAssert.assertEquals(contacts[indexLast].getName(), contact.getName());
        softAssert.assertEquals(contacts[indexLast].getPhone(), contact.getPhone());
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTestNameEmpty() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(0))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        String responseBody = response.getBody().asString();
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.name");
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"name\":\"must not be blank\""));
        //Another way to get value by name key
        softAssert.assertEquals(nameErrorMessage, "must not be blank");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertAll();
        //System.out.println(response.getBody().toString());
       // System.out.println(response.getBody().asString());
    }

    @Test
    public void addNewContactNegativeTestLastNameEmpty() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(0))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        Map mapMessage = (Map<String, String>) errorMessageDto.getMessage();
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"lastName\":\"must not be blank\""));
        //Another way to get value by name key
        softAssert.assertEquals(mapMessage.get("lastName"), "must not be blank");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTestEmailEmpty() {

        //Bug: email cannot be empty according to the requirements

        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email(generateString(0))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertFalse(response.getBody().asString().contains("Contact was added!"));
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailTwoAt() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_mail" + "@" + generateEmail(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertTrue(response.getBody().asString().contains("\"email\":\"must be a well-formed email address\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        System.out.println("--> " + errorMessageDto.getTimestamp());
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.toString());
        softAssert.assertEquals(errorMessageDto.getTimestamp().split("T")[0], localDate.toString());
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailWithoutAt() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_mail" + generateString(7))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertTrue(response.getBody().asString().contains("\"email\":\"must be a well-formed email address\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailNoCharBeforeAt() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email(generateEmail(0))
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertTrue(response.getBody().asString().contains("\"email\":\"must be a well-formed email address\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailNoCharAfterAt() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email(generateString(5) + "@")
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertTrue(response.getBody().asString().contains("\"email\":\"must be a well-formed email address\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailWithCyrLet() {

        //Bug: email cannot be with Russian letters according to the requirements

        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("Add_new_email" + generateEmail(5) + "Ф")
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertFalse(response.getBody().asString().contains("Contact was added!"));
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTestEmailAlreadyExists() {

        //Bug: email cannot be same as an already created one according to the requirements

        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName("Add new last name" + generateString(5))
                .email("grom@net.com")
                .phone(generatePhone(10))
                .address("Add new address" + generateString(5))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertFalse(response.getBody().asString().contains("Contact was added!"));
        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeAddressEmpty() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(7))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(10))
                .address(generateString(0))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"address\":\"must not be blank\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneEmpty() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(7))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(0))
                .address(generateString(20))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneNotNum() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(7))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generateString(11))
                .address(generateString(20))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneTooShort() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(7))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(9))
                .address(generateString(20))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneTooLong() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name("Add new name" + generateString(5))
                .lastName(generateString(7))
                .email("Add_new_mail" + generateEmail(7))
                .phone(generatePhone(16))
                .address(generateString(20))
                .description(generateString(20))
                .build();
        Response response = addNewContact(contact);
        ErrorMessageDto errorMessageDto = response.getBody().as(ErrorMessageDto.class);
        System.out.println("**** RESPONSE **** " + response.body().print());
        softAssert.assertTrue(response.getBody().asString().contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertEquals(response.getStatusCode(), 400);
        softAssert.assertAll();
    }

}
