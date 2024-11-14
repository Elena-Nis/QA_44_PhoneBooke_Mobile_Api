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
    @FindBy(xpath = "//android.widget.FrameLayout/android.view.ViewGroup/android.widget.FrameLayout[1]/android.view.ViewGroup/android.widget.TextView")
    AndroidElement headerContactsScreen;
    @FindBy(xpath = "//*[contains(@text, 'No Contacts')]")
    AndroidElement popUpRegistration;
    @FindBy(id = "com.sheygam.contactapp:id/add_contact_btn")
    AndroidElement btnAddNewContact;
    @FindBy(xpath = "/hierarchy/android.widget.Toast")
    AndroidElement popUpMessage;

    public boolean isElementContactListPresent_text() {
        return textContactList.isDisplayed();
    }

    public boolean isElementContactListPresent_class() {
        return classContactList.isDisplayed();
    }

    public boolean validateHeader() {
        return textInElementPresent(headerContactsScreen, "Contact list", 5);
    }

    public boolean isTextPopUpRresent() {
        return popUpRegistration.isDisplayed();
    }

    public void clickBtnAddNewContact() {
        clickWait(btnAddNewContact, 5);
    }

    public boolean validatePopMessage() {
        return textInElementPresent(popUpMessage, "Contact was added!", 5);
    }
}
