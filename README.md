# Two parts: Address Book Converter and Word Shuffle Validator

# Part 1: Address Book Converter
Java Command Line application to convert an Address Book to its XML or JSON equivalent.
This can also validate if a given XML or JSON file follows the schema provided in /src/main/resources/ad.xsd.
A sample address book (in XML format) can be found in src/main/resources/ad.xml. 

## Usage
There are two ways to use this application, either in "interactive mode" or just by passing the command line arguments.
## Interactive Mode
In this mode, the application will prompt the user with a list of options. After the selection is made, it will walk the user throughout the required information.

The menu looks like this:

      [1] Convert address book from XML to JSON
      [2] Convert address book from JSON to XML
      [3] Validate schema of address book
      [4] Exit
     
Note that the output file does not have to exist, but if filepaths are included, it must be a valid filepath.

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

## Quickstart
1. Clone the repository
2. Enter the project directory and run ```gradlew uberJar```
3. Enter the /build/libs directory, and you should see the fat jar, named address-book-converter-fat-jar.jar.
4. Run ```java -jar address-book-converter-fat-jar.jar``` to run the program in an interactive mode.
5. Run ```java -jar address-book-converter-fat-jar.jar <input file> <output file>``` to convert the file in command line arguments mode.
6. Run ```java -jar address-book-converter-fat-jar.jar <input file> -v``` or ```java -jar address-book-converter-fat-jar.jar <input file> --version``` to validate a given XML or JSON file to see if it's a valid address book.

A copy of the provided ad.xml is in /src/main/resources/ad.xml for convenience.



# Part 2: Word Shuffle Validator
This program, located in src/main/kotlin/ , will take two words and the shuffled word and output whether the shuffled word is valid.

The shuffle is defined as: a combination of two words where the letters of the shuffle come from the original
words in such a way that the relative order of the letters coming from the same word is
maintained, however each letter of the shuffle can be drawn from either of the words.

## Usage
You must pass the two original words, and the shuffled word to validate as such:

```java -jar <program jar file> <first word> <second word> <shuffled word>```

It will output whether the shuffled word in correct. Optionally a file names 'words.txt'
may be placed in /src/main/resources/. The program will check for the existence of this file, and if present
will validate if the two given words are present in the file. If the given words are not in the present file, 
the shuffle will not be checked, and the program will display to the user that the given words
are not allowed. If the file src/main/resources/words.txt is not present, all passed words are valid.

## Quick Start
1. Clone the repository
2. Enter the project directory and run ```gradlew uberJarKotlin```
3. Enter the /build/libs directory, and you should see the fat jar, named word-shuffler-1.0-kotlin.jar.
4. Run ```java -jar word-shuffler-1.0-kotlin.jar TOURNAMENT DINNER TDINOURNANMENTER``` or ```java -jar <program jar file> <first word> <second word> <shuffled word>```

# Testing
Unit tests can be run by entering the project directory and typing ```gradlew test```.
The files are located in /src/test/.
JUnit5 was used for the testing framework.