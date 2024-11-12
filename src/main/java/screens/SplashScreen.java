package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;


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
        pause(10);
    }
}