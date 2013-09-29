import java.util.*;

public class JosephusMain {
    /* You may create additional private static 
        variables or methods as needed */

    public static final void main(String[] args) {
        // *** Step 1: Check whether exactly one command-line argument is given *** 
        // *** Add code for step1 here ***

        // *** Step 2: Check whether the input file exists and is readable *** 
        // *** Add code for step2 here ***

        // *** Step 3: Load the data from the input file *** 
        // *** Add code for step3 here ***
        
        Scanner scan = new Scanner(System.in);

        //** You may also add additional variables as needed **//

        while (true) {
            String line = scan.nextLine();
            if (line == null || line.length() == 0)
                continue;

            String[] command = line.split(" ");

            if (command.length == 0)
                continue;

            if (command[0].equalsIgnoreCase("a")) {
                // add code to implement this option
            
            } else if (command[0].equalsIgnoreCase("p")) {
                // add code to implement this option
            
            } else if (command[0].equalsIgnoreCase("r")) {
                // add code to implement this option
            
            } else if (command[0].equalsIgnoreCase("x")) {
                System.out.println("exit");
                break;
            } else {
                System.out.println("Unknown command");
            }
        }
    }
}
