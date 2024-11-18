package screens;

import dto.ContactDtoLombok;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EditContactScreen extends BaseScreen {

    public EditContactScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/inputName")
    AndroidElement fieldName;
    @FindBy(id = "com.sheygam.contactapp:id/inputLastName")
    AndroidElement fieldLastName;
    @FindBy(id = "com.sheygam.contactapp:id/inputEmail")
    AndroidElement fieldEmail;
    @FindBy(id = "com.sheygam.contactapp:id/inputPhone")
    AndroidElement fieldPhone;
    @FindBy(id = "com.sheygam.contactapp:id/inputAddress")
    AndroidElement fieldAddress;
    @FindBy(id = "com.sheygam.contactapp:id/updateBtn")
    AndroidElement btnUpdate;
    @FindBy(xpath = "//android.widget.Toast[contains(@text, 'Contact was updated!')]")
    AndroidElement elementUpdated;

    public void fillEditContactForm(ContactDtoLombok contact) {
        fieldName.clear();
        fieldName.sendKeys(contact.getName());
        fieldLastName.clear();
        fieldLastName.sendKeys(contact.getLastName());
        fieldEmail.clear();
        fieldEmail.sendKeys(contact.getEmail());
        fieldPhone.clear();
        fieldPhone.sendKeys(contact.getPhone());
        fieldAddress.clear();
        fieldAddress.sendKeys(contact.getAddress());
    }

    public void clickBtnUpdate() {
        btnUpdate.click();
    }

    public boolean ispopUpDisplayed() {
        //AndroidElement popUp = driver.findElement(By.xpath("//*[contains(@text, 'Contact was updated!')]"));
        String toastMessage = driver.findElement(MobileBy.xpath("//android.widget.Toast[1]")).getAttribute("name");
        return toastMessage.equals("Contact was updated!");
    }
}