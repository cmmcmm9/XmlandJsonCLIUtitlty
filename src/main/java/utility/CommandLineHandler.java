package utility;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class to handle Command line interface of the application.
 * Two modes can be used: interactive and command line arguments.
 * In interactive mode, the program will prompt the user for their desired selection.
 * When passed command line arguments, it should be in the form:
 *      To convert a address book:
 *          java -jar [jarFileName].jar <input file> <output file>
 *      To validate an address book:
 *          java -jar [jarFileName].jar <input file> --validate
 *          java -jar [jarFileName].jar <input file> -v
 */
public class CommandLineHandler {

    private final AddressBookParser addressBookParser = new AddressBookParser();
    private final Logger LOGGER = Logger.getLogger(getClass().getName());

    public CommandLineHandler(){
        LOGGER.setLevel(Level.SEVERE);
    }

    /**
     * Method to prompt the user for their desired action.
     * Will ask for a selection (number) from options 1-4
     * with the following menu:
     * [1] Convert address book from XML to JSON
     * [2] Convert address book from JSON to XML
     * [3] Validate schema of address book
     * [4] Exit
     */
    public void interactiveMode(){
        Scanner scanner = new Scanner(System.in);
        File inputFile;
        File outputFile;
        int optionSelection;
        do{
            System.out.println("Please select an option by typing the corresponding number:");
            System.out.println("[1] Convert address book from XML to JSON");
            System.out.println("[2] Convert address book from JSON to XML");
            System.out.println("[3] Validate schema of address book");
            System.out.println("[4] Exit");
            optionSelection = scanner.nextInt();
            scanner.nextLine();

            switch (optionSelection){
                case 1:
                    System.out.println("Please enter the filepath for the XML address document to be converted:");
                    inputFile = new File(scanner.nextLine());
                    System.out.println("Please enter the filepath for the output JSON file:");
                    outputFile = new File(scanner.nextLine());
                    convertXmlAddressBookToJson(inputFile, outputFile);
                    break;
                case 2:
                    System.out.println("Please enter the filepath for the JSON address document to be converted:");
                    inputFile = new File(scanner.nextLine());
                    System.out.println("Please enter the filepath for the output XML file:");
                    outputFile = new File(scanner.nextLine());
                    convertJsonAddressBookToXml(inputFile, outputFile);
                    break;
                case 3:
                    System.out.println("Please enter the filepath for the XML or JSON document to be validated: ");
                    inputFile = new File(scanner.nextLine());
                    validateAddressBookSchema(inputFile);
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid option selected!");
                    break;
            }

        }while(optionSelection != 4);
        scanner.close();
    }

    /**
     * Method to execute the given task when the program is passed command line arguments.
     * Will exit and display the command line arguments usage.
     *       To convert a address book:
     *           java -jar [jarFileName].jar <input file> <output file>
     *       To validate an address book:
     *           java -jar [jarFileName].jar <input file> --validate
     *           java -jar [jarFileName].jar <input file> -v
     * @param args String[]: array of command line options.
     * @see CommandLineHandler#printCommandLineArgumentUsage()
     */
    public void commandLineArgumentsMode(String[] args){

        if(args.length != 2){
            System.out.println("Invalid number of arguments passed.");
            printCommandLineArgumentUsage();
            return;
        }

        String argumentOne = args[0];
        String argumentTwo = args[1];

        if(argumentTwo.equals("-v") || argumentTwo.equals("--validate")){
            File inputFile = new File(argumentOne);
            validateAddressBookSchema(inputFile);
            return;
        }

        File inputFile = new File(argumentOne);
        File outputFile = new File(argumentTwo);

        if(inputFile.getName().endsWith(".xml") && outputFile.getName().endsWith(".json")){
            convertXmlAddressBookToJson(inputFile, outputFile);
            return;
        }

        if(inputFile.getName().endsWith(".json") && outputFile.getName().endsWith(".xml")){
            convertJsonAddressBookToXml(inputFile, outputFile);
            return;
        }

        System.out.println("Invalid arguments passed. Accepted file types are .xml or .json.");
        printCommandLineArgumentUsage();
    }

    /**
     * Wrapper method for AddressBookParser#convertXmlAddressBookToJson()
     * Will convert the given XML address book to its JSON equivalent.
     * Displays to the user upon successful conversion, or prints the error message.
     * @param xmlInputFile File: XML address book to convert
     * @param jsonOutputFile File: JSON output file for the conversion. Note: does not have to exists.
     * @see utility.AddressBookParser#convertXmlAddressBookToJson(File, File)
     */
    private void convertXmlAddressBookToJson(File xmlInputFile, File jsonOutputFile) {
        try {
            addressBookParser.convertXmlAddressBookToJson(xmlInputFile, jsonOutputFile);
            System.out.println("Successfully converted XML address book!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
            LOGGER.log(Level.WARNING, "Could not convert XML address book to JSON", e);
        }
    }

    /**
     * Wrapper method for AddressBookParser#convertJsonAddressBookToXml().
     * Will convert the given JSON address book to its XML equivalent.
     * Displays to the user upon successful conversion, or prints the error message.
     * @param jsonInputFile File: JSON address book to convert
     * @param xmlOutputFile File: XML output file for the conversion. Note: It does not have to exist.
     * @see utility.AddressBookParser#convertJsonAddressBookToXml(File, File)
     */
    private void convertJsonAddressBookToXml(File jsonInputFile, File xmlOutputFile){
        try {
            addressBookParser.convertJsonAddressBookToXml(jsonInputFile, xmlOutputFile);
            System.out.println("Successfully converted XML address book!");
        } catch (IOException e) {
            System.out.println("Something went wrong!");
            System.out.println(e.getMessage());
            LOGGER.log(Level.WARNING, "Could not convert JSON address book to XML", e);
        }
    }

    /**
     * Will print out whether the passed input file is a
     * valid address book (defined in src/main/resources/ad.xsd) in either JSON or XML format.
     * @param inputFile File: file to validate the schema
     * @see utility.AddressBookParser#isValidJsonAddressBook(File)
     * @see utility.AddressBookParser#isValidXmlAddressBook(File)
     */
    private void validateAddressBookSchema(File inputFile){
        boolean isValidXMLAddressBook = (inputFile.getName().endsWith(".xml")) ? addressBookParser.isValidXmlAddressBook(inputFile) : addressBookParser.isValidJsonAddressBook(inputFile);
        if(isValidXMLAddressBook){
            System.out.println("This document is a valid address book.");
        }
        else{
            System.out.println("This document is not a valid address book.");
        }
    }

    /**
     * Method to print the command line usage for the java application.
     */
    private void printCommandLineArgumentUsage(){
        System.out.println("To convert an address book:");
        System.out.println("Usage: java -jar XmlandJsonCLIUtility.jar <input file> <output file>");

        System.out.println("To validate a given XML document is following the AddressBook schema");
        System.out.println("Usage: java -jar XmlandJsonCLIUtility.jar <input file> --validate");
        System.out.println("Usage: java -jar XmlandJsonCLIUtility.jar <input file> -v");
    }
}
