# XmlandJsonCLIUtitlty
Java Command Line application to convert an Address Book to its XML or JSON equivalent.
This can also validate if a given XML or JSON file follows the schema provided in /src/main/resources/ad.xsd.

# Usage
There are two ways to use this application, either in "interactive mode" or just by passing the command line arguements.
## Interactive Mode
In this mode, the application will prompt the user with a list of options. After the selection is made, it will walk the user throught the required information.

The menu looks like this:

      [1] Convert address book from XML to JSON
      [2] Convert address book from JSON to XML
      [3] Validate schema of address book
      [4] Exit
     
Note that the output file does not have to exist, but if filepath are included, it must be a valid filepath.

## Command Line Arguments Mode
In this mode, the user can submit an input file (either .xml or .json) and it will convert the file to the specified output file.

You can also pass in a .xml or .json file to validate by passing the ```v``` or ```--version``` flag

Usage:

To convert a file:
```
java -jar [jarFileName].jar <input file> <output file>
```
To validate a file:
```
java -jar [jarFileName].jar <input file> --validate
java -jar [jarFileName].jar <input file> -v
```

# Quickstart
1. Clone the repository
2. Enter the project directory and run ```gradlew uberJar```
3. Enter the /build/libs directory, and you should see the fat jar, named address-book-converter-fat-jar.jar.
4. Run ```java -jar address-book-converter-fat-jar.jar``` to run the program in an interactive mode.
5. Run ```java -jar address-book-converter-fat-jar.jar <input file> <output file>``` to convert the file in command line arguments mode.
6. Run ```java -jar address-book-converter-fat-jar.jar <input file> -v``` or ```java -jar address-book-converter-fat-jar.jar <input file> --version``` to validate a given XML or JSON file to see if it's a valid address book.

A copy of the provided ad.xml is in /src/main/resources/ad.xml for convenience.
