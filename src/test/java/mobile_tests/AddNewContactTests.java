package mobile_tests;

import config.AppiumConfig;
import dto.ContactDtoLombok;
import dto.ContactsDto;
import dto.ErrorMessageDto;
import dto.UserDtoLombok;
import helper.HelperApiMobile;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import screens.*;

import static helper.PropertiesReader.getProperty;
import static helper.RandomUtils.*;

public class AddNewContactTests extends AppiumConfig {
    UserDtoLombok user = UserDtoLombok.builder()
            .username(getProperty("data.properties", "email"))
            .password(getProperty("data.properties", "password"))
            .build();
    AddNewContactsScreen addNewContactsScreen;
    HelperApiMobile helperApiMobile;
    SoftAssert softAssert = new SoftAssert();

    @BeforeMethod
    public void loginAndGoToAddNewContactScreen() {
        new SplashScreen(driver).goToAuthScreen(5);
        AuthenticationScreen authenticationScreen = new AuthenticationScreen(driver);
        authenticationScreen.typeAuthenticationForm(user);
        authenticationScreen.clickBtnLogin();
        new ContactsScreen(driver).clickBtnAddNewContact();
        addNewContactsScreen = new AddNewContactsScreen(driver);
        helperApiMobile = new HelperApiMobile();
    }

    @Test
    public void addNewContactPositiveTest() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        Assert.assertTrue(new ContactsScreen(driver).validatePopMessage());
    }

    @Test
    public void addNewContactPositiveTestValidateContactApi() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(5))
                .lastName(generateString(10))
                .email(generateEmail(10))
                .phone(generatePhone(12))
                .address(generateString(8) + " app." + generatePhone(2))
                .description(generateString(15))
                .build();
        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        HelperApiMobile helperApiMobile = new HelperApiMobile();
        helperApiMobile.login(user.getUsername(), user.getPassword());
        Response responseGet = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGet.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
                break;
            }
        }
        System.out.println("--> " + flag);
        Assert.assertTrue(flag);
    }

    // **************** HW19 ******************

    @Test
    public void addNewContactNegativeTest_NameEmptyAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(0))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("name=must not be blank", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"name\":\"must not be blank\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.name");
        softAssert.assertEquals(nameErrorMessage, "must not be blank");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));
        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativeTest_LastNameEmptyAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(0))
                .email(generateEmail(7))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("lastName=must not be blank", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"lastName\":\"must not be blank\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.lastName");
        softAssert.assertEquals(nameErrorMessage, "must not be blank");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailTwoAtAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(9))
                .email("Add_new_mail" + "@" + generateEmail(7))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("email=must be a well-formed email address", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"email\":\"must be a well-formed email address\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.email");
        softAssert.assertEquals(nameErrorMessage, "must be a well-formed email address");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailWithoutAtAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(9))
                .email(generateString(7))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("email=must be a well-formed email address", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"email\":\"must be a well-formed email address\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.email");
        softAssert.assertEquals(nameErrorMessage, "must be a well-formed email address");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailNoCharBeforeAtAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(9))
                .email(generateEmail(0))
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("email=must be a well-formed email address", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"email\":\"must be a well-formed email address\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.email");
        softAssert.assertEquals(nameErrorMessage, "must be a well-formed email address");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeTestEmailNoCharAfterAtAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(9))
                .email(generateString(5) + "@")
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("email=must be a well-formed email address", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"email\":\"must be a well-formed email address\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.email");
        softAssert.assertEquals(nameErrorMessage, "must be a well-formed email address");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();

    }

    @Test
    public void addNewContactNegativeAddressEmptyAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(9))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generatePhone(10))
                .address(generateString(0))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("address=must not be blank", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"address\":\"must not be blank\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.address");
        softAssert.assertEquals(nameErrorMessage, "must not be blank");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneEmptyAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(9))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generatePhone(0))
                .address(generateString(20))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("phone=Phone number must contain only digits! And length min 10, max 15!", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.phone");
        softAssert.assertEquals(nameErrorMessage, "Phone number must contain only digits! And length min 10, max 15!");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneNotNumAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(9))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generateString(14))
                .address(generateString(20))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("phone=Phone number must contain only digits! And length min 10, max 15!", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.phone");
        softAssert.assertEquals(nameErrorMessage, "Phone number must contain only digits! And length min 10, max 15!");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneTooShortAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(9))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generatePhone(9))
                .address(generateString(20))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("phone=Phone number must contain only digits! And length min 10, max 15!", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.phone");
        softAssert.assertEquals(nameErrorMessage, "Phone number must contain only digits! And length min 10, max 15!");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();
    }

    @Test
    public void addNewContactNegativePhoneTooLongAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(9))
                .lastName(generateString(7))
                .email(generateEmail(7))
                .phone(generatePhone(16))
                .address(generateString(20))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("phone=Phone number must contain only digits! And length min 10, max 15!", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"phone\":\"Phone number must contain only digits! And length min 10, max 15!\""));
        //Another way to get value by name key
        ErrorMessageDto errorMessageDto = responsePOST.as(ErrorMessageDto.class);
        System.out.println("***** errorMessageDto " + errorMessageDto);
        String nameErrorMessage = JsonPath.from(responseBody).getString("message.phone");
        softAssert.assertEquals(nameErrorMessage, "Phone number must contain only digits! And length min 10, max 15!");
        softAssert.assertTrue(errorMessageDto.getError().equals("Bad Request"));

        softAssert.assertAll();
    }

    //Bug: email cannot be with Russian letters according to the requirements

    @Test
    public void addNewContactNegativeTestEmailWithCyrLetAPI() {
        ContactDtoLombok contact = ContactDtoLombok.builder()
                .name(generateString(7))
                .lastName(generateString(9))
                .email(generateEmail(7) + "Ф")
                .phone(generatePhone(10))
                .address(generateString(5))
                .description(generateString(20))
                .build();

        //***** UI

        addNewContactsScreen.typeContactForm(contact);
        addNewContactsScreen.clickBtnCreateContact();
        ErrorScreen errorScreen = new ErrorScreen(driver);
        softAssert.assertTrue(errorScreen.validateErrorMessage("email=must be a well-formed email address", 5));
        softAssert.assertTrue(errorScreen.validateErrorTitle("Error", 5));

        //***** API + Backend

        Response responseLOGIN = helperApiMobile.login(user.getUsername(), user.getPassword());
        //softAssert.assertEquals(responseLOGIN.getStatusCode(), 200);
        Response responseGET = helperApiMobile.getUserContactsResponse();
        ContactsDto contactsDto = responseGET.as(ContactsDto.class);
        boolean flag = false;
        for (ContactDtoLombok c : contactsDto.getContacts()) {
            if (c.equals(contact)) {
                flag = true;
            }
        }
        softAssert.assertFalse(flag);

        //***** API

        Response responsePOST = helperApiMobile.addNewContact(contact);
        String responseBody = responsePOST.asString();
        System.out.println("******** responseBody ***********" + responseBody);
        softAssert.assertEquals(responsePOST.getStatusCode(), 400);
        softAssert.assertTrue(responseBody.contains("\"email\":\"must be a well-formed email address\""));

        softAssert.assertAll();

    }
}