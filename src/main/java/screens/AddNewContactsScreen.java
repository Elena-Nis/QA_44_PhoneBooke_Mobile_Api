package screens;

import dto.ContactDtoLombok;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.support.FindBy;

public class AddNewContactsScreen extends BaseScreen{
    public AddNewContactsScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/inputName")
    AndroidElement inputName;
    @FindBy(id = "com.sheygam.contactapp:id/inputLastName")
    AndroidElement inputLastName;
    @FindBy(id = "com.sheygam.contactapp:id/inputEmail")
    AndroidElement inputEmail;
    @FindBy(id = "com.sheygam.contactapp:id/inputPhone")
    AndroidElement inputPhone;
    @FindBy(id = "com.sheygam.contactapp:id/inputAddress")
    AndroidElement inputAddress;
    @FindBy(id = "com.sheygam.contactapp:id/inputDesc")
    AndroidElement inputDescription;
    @FindBy(id = "com.sheygam.contactapp:id/createBtn")
    AndroidElement btnCreateNewContact;

    public void typeContactForm(ContactDtoLombok contact) {
        inputName.sendKeys(contact.getName());
        inputLastName.sendKeys(contact.getLastName());
        inputEmail.sendKeys(contact.getEmail());
        inputPhone.sendKeys(contact.getPhone());
        inputAddress.sendKeys(contact.getAddress());
        inputDescription.sendKeys(contact.getDescription());
    }

    public void clickBtnCreateContact() {
        btnCreateNewContact.click();
    }
}