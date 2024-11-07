package api_tests;

import config.ContactController;
import dto.ErrorMessageDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetUserContactsTests extends ContactController {
    SoftAssert softAssert = new SoftAssert();

    @Test
    public void getUserContactsPositiveTest() {
        Response response = getUserContactsResponse(tokenDto.getToken());
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertAll();
    }

    @Test
    public void getUserContactsNegativeTest_wrongToken() {
        Response response = getUserContactsResponse(tokenDto.getToken()+"12345");
        softAssert.assertEquals(response.getStatusCode(), 401);
        ErrorMessageDto errorMessage = ErrorMessageDto.builder().build();
        if(response.getStatusCode()==401){
            errorMessage = response.as(ErrorMessageDto.class);
        }
        System.out.println(errorMessage.toString());
        softAssert.assertTrue(errorMessage.getError().equals("Unauthorized"));
        softAssert.assertAll();
    }
}
