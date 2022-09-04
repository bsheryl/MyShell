package MyShellForWindows;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Terminal terminal = new Terminal(System.getProperty("user.home"));

        while (true) {
            System.out.print(terminal.getCurrentDirectory() + "> ");
            String input = scanner.nextLine();
            String[] commands = input.split(" ");
            if (commands[0].equalsIgnoreCase("dir")) {
                terminal.commandDir(commands);
            } else if (commands[0].equalsIgnoreCase("sort")) {
                terminal.commandSort(commands);
            } else if (commands[0].equalsIgnoreCase("mkdir")) {
                terminal.commandMkdir(commands);
            } else if (commands[0].equalsIgnoreCase("date")) {
                terminal.commandDate();
            }
        }
    }
}
