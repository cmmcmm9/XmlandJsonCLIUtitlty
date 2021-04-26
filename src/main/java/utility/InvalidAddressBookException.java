package utility;

import java.io.IOException;

/**
 * Exception Class to signify that an invalid address book was given.
 * That is, an XML file that does not conform the the schema defined in
 * /src/main/resources/ad.xsd
 */
public class InvalidAddressBookException extends IOException {
    public InvalidAddressBookException(String errorMessage) {
        super(errorMessage);
    }
}
