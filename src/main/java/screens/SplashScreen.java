package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SplashScreen extends BaseScreen {
    public SplashScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

      @FindBy(id = "com.sheygam.contactapp:id/version_text")
      AndroidElement versionApp;

    public boolean validateVersion() {
        return textInElementPresent(versionApp, "Version 1.0.0", 5);
    }

    public void goToAuthScreen() {
        pause(5);
    }

    public void goToAuthScreen(int time) {
        try {
            new WebDriverWait(driver, time)
                    .until(ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[@text='Authentication']")));
        }catch (TimeoutException e){
            e.printStackTrace();
            System.out.println("element Authentication not find");
        }
    }
}