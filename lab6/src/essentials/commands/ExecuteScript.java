package essentials.commands;

import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;
import essentials.Commander;
import essentials.interaction.ScriptInteraction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class ExecuteScript implements Command {


    private final String argument;


    public ExecuteScript(ArrayList<String> args) {
        this.argument = args.get(0);
    }

    @Override
    public Message execute(PriorityQueue<City> collection) {
        File file = new File(argument);

        Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return new Message(true,"Такого файла не существует.");
        }
        int line_num = 1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) {
                continue;
            }
            try {
                Command command = Commander.getCommand(line, true, new ScriptInteraction(scanner));
                if (command == null) {
                    continue;
                }
            } catch (Exception e) {
                return new Message(false,"Возникла ошибка при выполнении " + line_num + " строки:\n" + line);
            }
            line_num++;
        }
        return new Message(true,"Команды исполнены исправно.");
    }
    public String getArgument(){
        return argument;
    }
}