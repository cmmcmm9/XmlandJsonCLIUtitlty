package utility;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle the conversion of XML address books to JSON, JSON to XML, and validate the schema
 * (either in JSON or XML format) of a given address book
 * in accordance to the schema in /src/main/resources/ad.xsd
 */
public final class AddressBookParser {
    private final XmlMapper xmlMapper = (XmlMapper) new XmlMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final JsonMapper jsonMapper = (JsonMapper) new JsonMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    public AddressBookParser(){
        LOGGER.setLevel(Level.SEVERE);
    }

    /**
     * Method to ensure that the given input file exists.
     * Will throw an IOException if the file does not exist.
     * @param inputFile File: input file to ensure existence
     * @throws IOException if the File does not exist
     */
    private void ensureInputFileExits(File inputFile) throws IOException {
        if(inputFile == null){
            throw new IOException("Address book must be specified.");
        }
        if(!inputFile.exists()){
            throw new IOException("Invalid address book given: " + inputFile.getAbsolutePath());
        }
    }

    /**
     * Method to convert the given XML address book to its JSON equivalent.
     * @param xmlInputFile File: XML address book to convert
     * @param jsonOutputFile File: JSON output file for the conversion.
     * @throws IOException if the input file does not exists
     * @throws InvalidAddressBookException if the given input file is not a valid address book
     */
    public void convertXmlAddressBookToJson(File xmlInputFile, File jsonOutputFile) throws IOException, InvalidAddressBookException {
        ensureInputFileExits(xmlInputFile);

        if(!isValidXmlAddressBook(xmlInputFile)){
            throw new InvalidAddressBookException("Invalid XML address book given!");
        }

        AddressBook addressBook = getAddressBookFromXmlFile(xmlInputFile);
        jsonMapper.writeValue(jsonOutputFile, addressBook);

    }

    /**
     * Method to convert the given XML address book to its JSON equivalent.
     * @param jsonInputFile File: JSON address book to convert
     * @param xmlOutputFile File: XML output file for the conversion.
     * @throws IOException if the input file does not exists
     * @throws InvalidAddressBookException if the given input file is not a valid address book
     */
    public void convertJsonAddressBookToXml(File jsonInputFile, File xmlOutputFile) throws IOException {
        ensureInputFileExits(jsonInputFile);

        AddressBook addressBook = getAddressBookFromJson(jsonInputFile);
        xmlMapper.writeValue(xmlOutputFile, addressBook);

        if(!isValidXmlAddressBook(xmlOutputFile)){
            throw new InvalidAddressBookException("Invalid XML address book conversion!");
        }
    }

    /**
     * Method to validate if the given JSON file is a valid address book.
     * @param jsonInputFile File: file to validate the schema
     * @return true if the file is a valid address book, otherwise false
     */
    public boolean isValidJsonAddressBook(File jsonInputFile){
        String tempFileName = "./" + UUID.randomUUID() + ".json";
        File tempOutputFile = new File(tempFileName);
        tempOutputFile.deleteOnExit();
        try {
            convertJsonAddressBookToXml(jsonInputFile, tempOutputFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Method to validate if the given XML file is a valid address book.
     * @param xmlInputFile File: file to validate the schema
     * @return true if the file is a valid address book, otherwise false
     */
    public boolean isValidXmlAddressBook(File xmlInputFile){
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema addressBookSchema;
        try {
            addressBookSchema = schemaFactory.newSchema(getClass().getResource("/ad.xsd"));
            Validator addressBookValidator = addressBookSchema.newValidator();
            addressBookValidator.validate(new StreamSource(xmlInputFile));
            return true;
        } catch (SAXException | IOException e) {
            LOGGER.log(Level.WARNING, "Could not validate given XML file.", e);
            return false;
        }
    }

    /**
     * Method to deserialize the given XML file to an AddressBook POJO.
     * @param xmlInputFile File: given XML address book to deserialize
     * @return AddressBook: The XML file represented as the AddressBook POJO
     * @throws IOException if their was a problem deserializing the XML file to an AddressBook POJO.
     */
    private AddressBook getAddressBookFromXmlFile(File xmlInputFile) throws IOException {
        return xmlMapper.readValue(xmlInputFile, AddressBook.class);
    }

    /**
     * Method to deserialize the given JSON file to an AddressBook POJO.
     * @param jsonInputFile File: given JSON address book to deserialize
     * @return AddressBook: The XML file represented as the AddressBook POJO
     * @throws IOException if their was a problem deserializing the JSON file to an AddressBook POJO.
     */
    private AddressBook getAddressBookFromJson(File jsonInputFile) throws IOException {
        return jsonMapper.readValue(jsonInputFile, AddressBook.class);
    }
}
