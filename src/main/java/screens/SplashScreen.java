package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class SplashScreen extends BaseScreen {
    AndroidElement versionApp;

    public SplashScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
     versionApp = driver.findElement(By.id("com.sheygam.contactapp:id/version_text"));
     //PageFactory.initElements(new AppiumFieldDecorator(driver), this);

    }

//      @FindBy(id = "com.sheygam.contactapp:id/version_text")
//      AndroidElement versionApp;

    public boolean validateVersion() {
        return textInElementPresent(versionApp, "Version 1.0.0", 5);
    }

    public void goToAuthScreen() {
        pause(10);
    }
}