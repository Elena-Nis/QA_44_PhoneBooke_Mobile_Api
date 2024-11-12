package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class ContactsScreen extends BaseScreen {
    public ContactsScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(xpath = "//*[@text='Contact list']")
    AndroidElement textContactList;
    @FindBy(className = "android.widget.TextView")
    AndroidElement classContactList;

    public boolean isElementContactListPresent_text() {
        return textContactList.isDisplayed();
    }

    public boolean isElementContactListPresent_class() {
        return classContactList.isDisplayed();
    }
}
