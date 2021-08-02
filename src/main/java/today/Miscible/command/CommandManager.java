package today.Miscible.command;

import java.util.ArrayList;
import today.Miscible.command.impl.*;

public class CommandManager {

    private static ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        add(new CommandBind(new String[] {"bind"}));
        add(new CommandToggle(new String[] {"toggle", "t"}));
     //   add(new CommandHide(new String[] {"hide"}));
    }

    public void add(Command c) {
        this.commands.add(c);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}
