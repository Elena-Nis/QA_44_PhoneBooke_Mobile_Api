package data_provider;

import dto.ContactDtoLombok;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ContactDP_EditContact {

    @DataProvider
    public Object[][] editNewContactDPFile()
    {
        List<ContactDtoLombok> contactList = new ArrayList<>();
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader("src/main/resources/dp_data/list_contact_edit.csv"));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] splitArray = line.split(",");
                contactList.add(ContactDtoLombok.builder()
                        .name(splitArray[0])
                        .lastName(splitArray[1])
                        .email(splitArray[2])
                        .phone(splitArray[3])
                        .address(splitArray[4])
                        .description(splitArray[5])
                        .build());
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Object[][] data = new Object[contactList.size()][1];
        for (int i=0; i<contactList.size(); i++)
        {
            data[i][0] = contactList.get(i);
        }
        return data;
    }
}