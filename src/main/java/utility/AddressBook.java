package utility;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO to hold the address book data
 */
public final class AddressBook {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JsonProperty("Contact")
    private List<Contact> contact = new ArrayList<>();
    public List<Contact> getContact() {
        return contact;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }
}
