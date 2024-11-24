package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ErrorScreen extends BaseScreen {
    public ErrorScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "android:id/message")
    AndroidElement errorMessage;
    @FindBy(id = "android:id/alertTitle")
    AndroidElement errorTitle;
    @FindBy(id="android:id/button1")
    AndroidElement btnOk;

    public boolean validateErrorMessage(String text, int time) {
        //  driver.findElement(By.id("android:id/message")).clear();
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.visibilityOf(errorMessage));
//        System.out.println("##### errorMessage ###### " +errorMessage);
//        System.out.println("%%%%% errorMessage.getText %%%%% " + errorMessage.getText());
        return textInElementPresent(errorMessage, text, time);
    }
    public boolean validateErrorTitle(String text, int time) {
        return textInElementPresent(errorTitle, text, time);
    }

    public void clickBtnOk () {
        btnOk.click();
    }

}
