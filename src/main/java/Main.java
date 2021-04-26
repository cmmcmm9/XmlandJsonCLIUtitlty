import utility.CommandLineHandler;

/**
 * Main entry point for the application.
 * If there are no arguments passed in the main method, then it will launch in "interactive mode".
 * Otherwise, it will launch in command line option mode.
 */
public class Main {

    public static void main(String[] args){
        CommandLineHandler commandLineHandler = new CommandLineHandler();
        if(args.length == 0){
            commandLineHandler.interactiveMode();
            return;
        }

        commandLineHandler.commandLineArgumentsMode(args);
    }
}
