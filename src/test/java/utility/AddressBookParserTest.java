package utility;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class AddressBookParserTest {

    private final File xmlExpectedFileResult = new File(getClass().getResource("/ad.xml").getFile());
    private final File jsonExpectedFileResult = new File(getClass().getResource("/jsonExpectedOutput.json").getFile());

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void convertXmlAddressBookToJson() {
        //setup
        AddressBookParser addressBookParser = new AddressBookParser();
        byte[] jsonExpectedFileResultBytes = null;
        byte[] jsonFileActualResultBytes = null;

        File jsonResultFile = new File("./result.json");
        jsonResultFile.deleteOnExit();

        try {
            jsonExpectedFileResultBytes = Files.readAllBytes(jsonExpectedFileResult.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read bytes from expected result for json file");
        }

        /*
        Convert the provided 'ad.xml' file to JSON. Expected Result is the contents (byte[]) should match
        those of /test/resources/jsonExpectedOutput.json
         */
        try {
            addressBookParser.convertXmlAddressBookToJson(xmlExpectedFileResult, jsonResultFile);
        } catch (IOException e) {
            fail("Could not convert XML address book to its JSON equivalent");
            e.printStackTrace();
        }


        try {
            jsonFileActualResultBytes = Files.readAllBytes(jsonResultFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read Bytes of Json Result File");
        }

        assertArrayEquals(jsonExpectedFileResultBytes, jsonFileActualResultBytes);
        assertNotNull(jsonFileActualResultBytes);

        //Assert IO exception is thrown when a fake xml input file is given
        Class<IOException> expectedException = IOException.class;
        File fakeXmlInputFile = new File("");

        assertThrows(expectedException, () -> addressBookParser.convertXmlAddressBookToJson(fakeXmlInputFile, jsonResultFile));

        //Assert IO exception is thrown when a fake json output file is given
        File fakeJsonOutputFile = new File("");
        assertThrows(expectedException, () -> addressBookParser.convertXmlAddressBookToJson(xmlExpectedFileResult, fakeJsonOutputFile));

        /*
        Assert InvalidAddressBookException is thrown when an invalid XML document is passed.
        Invalid meaning that is does not follow the Address Book schema definded in src/main/resources/ad.xsd
         */
        File invalidXmlFile = new File(getClass().getResource("/invalidSchema.xml").getFile());
        assertThrows(InvalidAddressBookException.class, () -> addressBookParser.convertXmlAddressBookToJson(invalidXmlFile, jsonResultFile));

    }

    @Test
    void convertJsonAddressBookToXml() {
        //setup
        AddressBookParser addressBookParser = new AddressBookParser();
        byte[] xmlExpectedFileResultBytes = null;
        byte[] xmlFileActualResultBytes = null;
        File xmlResultFile = new File("result.xml");
        xmlResultFile.deleteOnExit();

        try {
            xmlExpectedFileResultBytes = Files.readAllBytes(xmlExpectedFileResult.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read bytes from expected result for xml file");
        }

        /*
        Convert the provided 'ad.xml' file to JSON. Expected Result is the contents (byte[]) should match
        those of /test/resources/jsonExpectedOutput.json
         */
        try {
            addressBookParser.convertJsonAddressBookToXml(jsonExpectedFileResult, xmlResultFile);
        } catch (IOException e) {
            fail("Could not convert JSON address book to its XML equivalent");
            e.printStackTrace();
        }


        try {
            xmlFileActualResultBytes = Files.readAllBytes(xmlResultFile.toPath());

        } catch (IOException e) {
            e.printStackTrace();
            fail("Could not read Bytes of XML Result File");
        }

        assertArrayEquals(xmlExpectedFileResultBytes, xmlFileActualResultBytes);
        assertNotNull(xmlFileActualResultBytes);

        //Assert IO exception is thrown when a fake JSON input file is given
        File fakeJsonInputFile = new File("");

        assertThrows(IOException.class, () -> addressBookParser.convertJsonAddressBookToXml(fakeJsonInputFile, xmlResultFile));

        //Assert IO exception is thrown when a fake XML output file is given
        File fakeXmlOutputFile = new File("");
        assertThrows(IOException.class, () -> addressBookParser.convertJsonAddressBookToXml(jsonExpectedFileResult, fakeXmlOutputFile));

        /*
        Assert IOException is thrown when an invalid JSON is passed.
        Invalid meaning that is does not follow the Address Book schema defined in src/main/resources/ad.xsd
         */
        File invalidJsonFile = new File(getClass().getResource("/invalidJson.json").getFile());
        assertThrows(IOException.class, () -> addressBookParser.convertJsonAddressBookToXml(invalidJsonFile, xmlResultFile));


    }
}