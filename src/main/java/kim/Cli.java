package kim;

import org.apache.commons.cli.*;

/**
 * commandline interface that is used to give an argument in the terminal for the program.
 * in this case the argument can be a datafile that is used to run the WekaRunner program.
 * also to start the program calling the jar file
 */

public class Cli {
    // set class variables
    String[] arguments;
    String fileName;
    private static final String FILE = "file-name";
    public Options options = new Options();

    public Cli(String[] args){
        this.arguments = args;
        buildOptions();
        this.fileName = parseArguments();
    }

    /**
     * the program can be used without using your own file, but this option gives you the chance to use your own file
     * containing different instances that need to be classified. the usage of this is linked to the option -f
     */
    public void buildOptions(){
        Option file = new Option("f", "path_to_file", true,    "/path to file/ + arff file ");
        file.setArgName("file");

        options.addOption(file);
    }

    /**
     * here we catch possible errors that can occur using the wrong commandline arguments.
     * to clarify what the argument needs to be, a message is printed when running the program trough terminal
     */
    public String parseArguments(){
        System.out.println("Weka program is started.");
        System.out.println("choose option -f to run the program with own file containing instances to be classified.");
        System.out.println("If no file is given the program will run with a standard file containing instances to be " +
                "classified. Unless any other error is shown below");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine command = parser.parse(options, arguments);
            if (command.hasOption(FILE)){
                return command.getOptionValue(FILE);

            }

        } catch (ParseException e) {
            System.out.println(" -f option selected, but no arff file found");

        }

        return null;
    }

}
