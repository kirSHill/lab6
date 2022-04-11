package essentials;

import essentials.commands.interfaces.Command;
import essentials.commands.*;
import essentials.commands.Help;
import essentials.commands.Info;
import essentials.interaction.UserInteraction;
import java.util.ArrayList;

public abstract class Commander {

    /**
     * Метод работы с вводимыми пользователем командами
     */
    public static Command getCommand(String potencialCommand, boolean fromScript, UserInteraction interaction) {
        potencialCommand = potencialCommand.trim();
        String[] commandParts = potencialCommand.split("\\s+");
        String command = commandParts[0];
        ArrayList<String> args = new ArrayList<>();
        for (int i = 1; i < commandParts.length; i++) {
            String arg = commandParts[i].replaceAll("\\s+", "");
            if (!arg.isEmpty()) {
                args.add(arg);
            }
        }

        switch (command) {
            case "help":
                return new Help();
            case "info":
                return new Info();
            case "show":
                return new Show();
            case "add":
                return new Add(fromScript, 0);
            case "clear":
                return new Clear();
            case "removeById":
                if(args.size() == 0) {
                    interaction.print(true, "Отсутствуют необходимые параметры.");
                    return null;
                }
                return new RemoveById(interaction, args);
            case "updateId":
                if(args.size() == 0) {
                    interaction.print(true, "Отсутствуют необходимые параметры.");
                    return null;
                }
                return new UpdateId(args, interaction);
            case "executeScript":
                if(fromScript) {
                    interaction.print(true, "Запрещено использовать скрипт из другого скрипта!");
                    return null;
                }
                if (args.size() == 0) {
                    interaction.print(true, "Отстутствуют необходимые параметры.");
                    return null;
                }
                return new ExecuteScript(args);
            case "exit":
                  return new Exit();
            case "removeFirst":
                return new RemoveFirst();
            case "head":
                return new Head();
            case "addIfMin":
                return new AddIfMin(fromScript, 0);
            case "countByClimate":
                if (args.size() == 0) {
                    interaction.print(true, "Отсутствуют необходимые параметры.");
                    return null;
                }
                return new CountByClimate(args);
            case "countGreaterThanGovernment":
                if (args.size() == 0) {
                    interaction.print(true, "Отсутствуют необходимые параметры.");
                    return null;
                }
                return new CountGreaterThanGovernment(args);
            case "printDescending":
                return new PrintDescending();
            default:
                interaction.print(true,"Команды '" + command + "' не существует. " +
                        "Воспользуйтесь 'help' для получения списка команд.");
                return null;
        }
    }
}
