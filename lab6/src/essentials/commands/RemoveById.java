package essentials.commands;

import essentials.commands.interfaces.ById;
import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;
import essentials.interaction.UserInteraction;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class RemoveById implements Command, ById {


    private final UserInteraction interaction;
    private final String argument;


    public RemoveById(UserInteraction interaction, ArrayList<String> args) {
        this.argument = args.get(0);
        this.interaction = interaction;
    }


    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        search(interaction, collection, argument);
        return new Message(true, "Элемент с id " + argument + " был удалён из коллекции.");
    }


}
