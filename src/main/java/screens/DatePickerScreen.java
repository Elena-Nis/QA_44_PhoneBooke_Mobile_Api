package screens;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePickerScreen extends BaseScreen {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.now();
        System.out.println(localDate.getYear());
    }

    public DatePickerScreen(AppiumDriver<AndroidElement> driver) {
        super(driver);
    }

    @FindBy(id = "com.sheygam.contactapp:id/dateBtn")
    AndroidElement btnChangeDate;
    @FindBy(id = "android:id/prev")
    AndroidElement btnPrevMonth;
    @FindBy(id = "android:id/next")
    AndroidElement btnNextMonth;
    @FindBy(id = "android:id/date_picker_header_year")
    AndroidElement btnPickerYear;
    @FindBy(id = "android:id/button1")
    AndroidElement btnOk;
    @FindBy(id = "com.sheygam.contactapp:id/dateTxt")
    AndroidElement actualDateElement;
    @FindBy(xpath = "//android.view.View[@content-desc=\"29 December 2023\"]")
    AndroidElement btnDay;

    public void typeDate(String date) {  //29 November 2024   01 November 2024
        btnChangeDate.click();
        btnPrevMonth.click();

        String[] arrayDate = date.split(" ");
        LocalDate localDate = LocalDate.now();
        if (localDate.getYear() != Integer.parseInt(arrayDate[2])) {
            btnPickerYear.click();
            driver.findElement(By.xpath("//*[@text='" + arrayDate[2] + "']")).click();
        }

        btnDay.click();

        //        AndroidElement pickerDate = driver.findElement(By.xpath("//*[@content-desc='"+date+"']"));
//        pickerDate.click();
    }

    public void clickBtnOk() {
        btnOk.click();
    }

    public boolean isDatePresent(String date) {
        String[] arrayDate = date.split(" ");
        System.out.println("***** year ***** " + arrayDate[2]);
        int day = Integer.parseInt(arrayDate[0]);
        int year = Integer.parseInt(arrayDate[2]);
        String month = arrayDate[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        LocalDate expectedDate = LocalDate.parse(day + " " + month + " " + year, formatter);
        System.out.println("***** expectedDate ***** " + expectedDate);
        String[] arrayDate1 = actualDateElement.getText().split("/");
        int day1 = Integer.parseInt(arrayDate1[0]);
        int month1 = Integer.parseInt(arrayDate1[1]);
        int year1 = Integer.parseInt(arrayDate1[2]);
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate actualDate = LocalDate.parse(day1 + "/" + month1 + "/" + year1, formatter1);
        System.out.println("*****  actualDate  ***** " + actualDate);
        return expectedDate.equals(actualDate);

    }
}
