package config;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class AppiumConfig {

    public static AppiumDriver<AndroidElement> driver;
    public int height = 0, width = 0;


    //          "platformName": "Android",
    //          "deviceName": "Nex5",
    //          "platformVersion": "8.0",
    //          "appPackage": "com.sheygam.contactapp",
    //          "appActivity": ".SplashActivity"
    @BeforeMethod
    public void setup() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Pix2");
        desiredCapabilities.setCapability("platformVersion", "8.0");
        desiredCapabilities.setCapability("appPackage", "com.sheygam.contactapp");
        desiredCapabilities.setCapability("appActivity", ".SplashActivity");
        //  desiredCapabilities.setCapability("automationName", "UiAutomator2");

        desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "Appium");
        try {
            driver = new AppiumDriver<>(new URL("http://localhost:4723/wd/hub"), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        height = driver.manage().window().getSize().getHeight();
        width = driver.manage().window().getSize().getWidth();
        System.out.println(width + "X" + height);
    }

    @AfterMethod
    public void tearDown() {
       //  driver.quit();
    }
}
