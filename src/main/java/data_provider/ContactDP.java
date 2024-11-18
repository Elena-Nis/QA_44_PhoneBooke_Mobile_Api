package data_provider;

import dto.ContactDtoLombok;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContactDP {

    @DataProvider
    public Iterator<ContactDtoLombok> addNewContactDPFile() {
        List<ContactDtoLombok> contactList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/dp_data/list_contact1.csv"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] splitArray = line.split(",");
                contactList.add(ContactDtoLombok.builder()
                        .name(splitArray[0])
                        .lastName(splitArray[1])
                        .email(splitArray[3])
                        .phone(splitArray[2])
                        .address(splitArray[4])
                        .description(splitArray[5])
                        .build());
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contactList.listIterator();
    }
}
