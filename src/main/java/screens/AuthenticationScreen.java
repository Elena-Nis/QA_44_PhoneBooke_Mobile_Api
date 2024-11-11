package screens;

import config.AppiumConfig;
import dto.UserDto;
import dto.UserDtoLombok;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class AuthenticationScreen extends BaseScreen {
    public AuthenticationScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/inputEmail']")
  //  @FindBy(id = "com.sheygam.contactapp:id/inputEmail")
    AndroidElement inputEmail;
    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/inputPassword']")
    // @FindBy(id="com.sheygam.contactapp:id/inputPassword")
    AndroidElement inputPassword;
    @FindBy(id = "com.sheygam.contactapp:id/regBtn")
    AndroidElement btnRegistartion;
    @FindBy(id = "com.sheygam.contactapp:id/loginBtn")
    AndroidElement btnLogin;


    public void typeAuthenticationForm(UserDtoLombok user) {
        inputEmail.sendKeys(user.getUsername());
        inputPassword.sendKeys(user.getPassword());
    }

    public void typeAuthenticationForm(UserDto user) {
        inputEmail.sendKeys(user.getUsername());
        inputPassword.sendKeys(user.getPassword());
    }


    public void clickBtnRegistration() {
        btnRegistartion.click();
    }

    public void clickBtnLogin() {
        btnLogin.click();
    }
}