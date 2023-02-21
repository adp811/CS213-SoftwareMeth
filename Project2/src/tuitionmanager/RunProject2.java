package tuitionmanager;

/**
 * This class is used to run an instance of RosterManager. The run()
 * method of RosterManager is called, allowing the user to interact with
 * the program using the command line.
 *
 * @author Aryan Patel
 */
public class RunProject2 {

    /**
     * A new instance of RosterManager is created and the run() method
     * is called to begin the program.
     *
     * @param args no arguments passed
     */
    public static void main(String[] args) {
        new TuitionManager().run();
    }
}
