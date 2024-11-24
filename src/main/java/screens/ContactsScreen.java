package screens;

import dto.ContactDtoLombok;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import io.appium.java_client.touch.offset.PointOption;
import io.appium.java_client.TouchAction;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContactsScreen extends BaseScreen {
    public int height;
    public int width;

        public ContactsScreen(AppiumDriver<AndroidElement> driver) {
            super(driver);
            height = driver.manage().window().getSize().getHeight();
            width = driver.manage().window().getSize().getWidth();
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

    @FindBy(xpath = "//*[@resource-id='com.sheygam.contactapp:id/rowContainer']")
    AndroidElement firstElementContactsList;
    @FindBy(id = "android:id/button1")
    AndroidElement popUpBtnYes;

    @FindBy(id = "com.sheygam.contactapp:id/rowContainer")
    List<AndroidElement> contactContainers;
    @FindBy(id = "com.sheygam.contactapp:id/rowName")
    List<AndroidElement> nameList;
    @FindBy(id = "com.sheygam.contactapp:id/rowPhone")
    List<AndroidElement> phoneList;

    @FindBy(xpath = "//*[@text = 'Logout']")
    AndroidElement btnLogout;
    @FindBy(xpath = "//*[@text = 'Date picker']")
    AndroidElement btnDataPicker;
    @FindBy(xpath = "//android.widget.ImageView[@content-desc='More options']")
    AndroidElement btnMoreOptions;


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

    public void deleteContact() {
        pause(3);
        int xLeftUpCorner = firstElementContactsList.getLocation().getX();
        int yLeftUpCorner = firstElementContactsList.getLocation().getY();
        int heightElement = firstElementContactsList.getSize().getHeight();
        int wightElement = firstElementContactsList.getSize().getWidth();
//        System.out.println("y --> " + firstElementContactsList.getLocation().getY());
//        System.out.println("x --> " + firstElementContactsList.getLocation().getX());
//        System.out.println("h --> " + firstElementContactsList.getSize().getHeight());
//        System.out.println("w --> " + firstElementContactsList.getSize().getWidth());
        TouchAction<?> touchAction = new TouchAction(driver);
//        touchAction.longPress(PointOption.point(wightElement/6,(yLeftUpCorner+heightElement/2)))
//                .moveTo(PointOption.point(wightElement/6*5, (yLeftUpCorner+heightElement/2)))
//                        .release().perform();
        touchAction.longPress(PointOption.point(wightElement / 6, (yLeftUpCorner + heightElement / 2)))
                .moveTo(PointOption.point(wightElement / 6 * 5, (yLeftUpCorner + heightElement / 2)))
                .release().perform();
    }

    public void clickBtnYes() {
        popUpBtnYes.click();
    }


    public int getAllNamesPhones() {
        nameList.clear();
        Set<String> unuqueContacts = new HashSet<>();
        boolean canscroll = true;
        boolean canscrollUp = true;
        TouchAction<?> touchAction = new TouchAction(driver);
        while (canscroll) {
            for (AndroidElement element : nameList) {
                String contactName = element.getText();
                if (!unuqueContacts.contains(contactName)) {
                    unuqueContacts.add(contactName);
                }
            }
            Point lastElementLocation = nameList.get(nameList.size() - 1).getLocation();
            touchAction.longPress(PointOption.point(width / 2, height * 8 / 10))
                    .moveTo(PointOption.point(width / 2, 0))
                    .release()
                    .perform();
            Point newLastElementLocation = nameList.get(nameList.size() - 1).getLocation();
            if (lastElementLocation.equals(newLastElementLocation)) {
                canscroll = false;
            }

        }
        while(canscrollUp) {
            Point firstElementLocation = nameList.get(0).getLocation();
            touchAction.longPress(PointOption.point(width/2, height/8))
                    .moveTo(PointOption.point(width/2, height*7/8))
                    .release()
                    .perform();
            Point newFirstElementLocation = nameList.get(0).getLocation();
            if(firstElementLocation.equals(newFirstElementLocation)) {
                canscrollUp=false;
            }
        }
        return unuqueContacts.size();
    }

    public void editContact() {
       // pause(5);
        int xLeftUpCorner = firstElementContactsList.getLocation().getX();
        int yLeftUpCorner = firstElementContactsList.getLocation().getY();
        int heightElement = firstElementContactsList.getSize().getHeight();
        int wightElement = firstElementContactsList.getSize().getWidth();
        TouchAction<?> touchAction = new TouchAction(driver);
        touchAction.longPress(PointOption.point((xLeftUpCorner+wightElement) * 5 / 6, (yLeftUpCorner + heightElement / 2)))
                .moveTo(PointOption.point((xLeftUpCorner+wightElement)/6, (yLeftUpCorner + heightElement / 2)))
                .release().perform();
    }

    public boolean validateUIListContact(ContactDtoLombok contact) {
        String nameFamily = contact.getName() + " " + contact.getLastName();
        List<AndroidElement> listContactsOnScreen = new ArrayList<>();
        System.out.println("list size --> " + listContactsOnScreen.size());
        boolean flagEqualsNameFamily = false;
        boolean flagEndOfList = false;
        listContactsOnScreen = driver.findElements(By.xpath("//*[@resource-id='com.sheygam.contactapp:id/rowName']"));
        while (!flagEndOfList) {
            AndroidElement lastElementListPrev = listContactsOnScreen.get(listContactsOnScreen.size() - 1);
            for (AndroidElement e : listContactsOnScreen) {
                System.out.println(e.getText());
                if (e.getText().equals(nameFamily)) {
                    flagEqualsNameFamily = true;
                    flagEndOfList = true;
                    break;
                }
            }
            scrollUp();
            listContactsOnScreen = driver.findElements(By.xpath("//*[@resource-id='com.sheygam.contactapp:id/rowName']"));
            if (lastElementListPrev.getLocation().equals(listContactsOnScreen.get(listContactsOnScreen.size() - 1).getLocation())) {
                flagEndOfList = true;
            }

        }
        return flagEqualsNameFamily;
    }

    private void scrollUp() {
        int height = driver.manage().window().getSize().getHeight();
        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.longPress(PointOption.point(10, height / 8 * 7))
                .moveTo(PointOption.point(10, height / 8))
                .release().perform();
    }

    public void goToEditScreen() {
        int yLeftUpCorner = firstElementContactsList.getLocation().getY();
        int heightElement = firstElementContactsList.getSize().getHeight();
        int wightElement = firstElementContactsList.getSize().getWidth();
        System.out.println("y --> " + firstElementContactsList.getLocation().getY());
        System.out.println("x --> " + firstElementContactsList.getLocation().getX());
        System.out.println("h --> " + firstElementContactsList.getSize().getHeight());
        System.out.println("w --> " + firstElementContactsList.getSize().getWidth());
        TouchAction<?> touchAction = new TouchAction(driver);
        touchAction.longPress(PointOption.point(wightElement / 6 * 5, (yLeftUpCorner + heightElement / 2)))
                .moveTo(PointOption.point(wightElement / 6, (yLeftUpCorner + heightElement / 2)))
                .release().perform();
    }

    public void logout() {
        clickWait(btnMoreOptions, 3);
        clickWait(btnLogout, 3);
    }

    public void goToDatePicker() {
        clickWait(btnMoreOptions, 3);
        clickWait(btnDataPicker, 3);
    }
}
