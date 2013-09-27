import java.util.*;
import java.io.*;

public class JosephusMain {
    public static void printOptions() {
        System.out.println("Invalid command");
    }

    public static final void main(String[] args) throws java.io.IOException {
        if (args.length != 1) {
            System.out.println("Usage: java JosephusMain inputFileName");
            return;
        }

        CircularLinkedList<String> list = new CircularLinkedList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
                list.setCurrentPosition(1);
            }
            br.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("File " + args[0] + " not found");
            return;
        }

        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.nextLine();
            if (line == null || line.length() == 0)
                continue;

            String[] command = line.split(" ");

            if (command[0].equalsIgnoreCase("a")) {
                if (command.length < 2) {
                    printOptions();
                    continue;
                }
                list.add(command[1]);
            } else if (command[0].equalsIgnoreCase("p")) {
                System.out.print(list.print(0));
            } else if (command[0].equalsIgnoreCase("r")) {
                if (command.length < 4) {
                    printOptions();
                    continue;
                }

                int stepSize = 0, cycles = 0;
                boolean dir = true;
                try {
                    stepSize = Integer.parseInt(command[1]);

                    if (command[2].equalsIgnoreCase("f") || command[2].equalsIgnoreCase("forward"))
                        dir = true;
                    else if (command[2].equalsIgnoreCase("b") || command[2].equalsIgnoreCase("backward"))
                        dir = false;
                    else {
                        printOptions();
                        continue;
                    }

                    cycles = Integer.parseInt(command[3]);
                } catch (NumberFormatException e) {
                    printOptions();
                    continue;
                }

                for (int c = 0; list.size() > 0 && (cycles == 0 || c < cycles); ++c) {
                    System.out.println("Cycle Number " + (c + 1) + " Has Begun");
                    int killCount = (stepSize + list.size() - 1) / stepSize;
                    for (int i = 0; list.size() > 0 && i < killCount; ++i) {
                        if (dir)
                            list.setCurrentPosition(stepSize - 1);
                        else
                            list.setCurrentPosition(1 - stepSize);
                        try {
                            System.out.println(list.get(0) + " has been eliminated by " + list.get(dir ? -1 : 1));
                            list.remove();
                        } catch (ElementNotFoundException e) {
                            System.out.println("Internal error");
                        }
                        if (!dir)
                            list.setCurrentPosition(-1);
                    }
                    System.out.println("Cycle Number " + (c + 1) + " Has Ended");
                }
                if (list.size() >= 2)
                    System.out.println(list.size() + " people still remain alive");
                else
                    System.out.println(list.size() + " person still remains alive");

            } else if (command[0].equalsIgnoreCase("x")) {
                System.out.println("exit");
                break;
            }
        }
    }
}
