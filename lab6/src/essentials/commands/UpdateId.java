package essentials.commands;

import essentials.commands.interfaces.ById;
import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;
import essentials.interaction.UserInteraction;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class UpdateId extends Add implements Command, ById {


    private final String argument;
    private final UserInteraction interaction;


    public UpdateId(ArrayList<String> args, UserInteraction interaction) {
        super(false, Integer.parseInt(args.get(0)));
        this.argument = args.get(0);
        this.interaction = interaction;
    }


    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        search(interaction, collection, argument);
        this.city.setId(Integer.parseInt(argument));
        collection.add(city);
        return new Message(true, "Элемент с id " + argument + " обновлён.");
    }


    @Override
    public void remove(UserInteraction interaction, PriorityQueue<City> collection, String argument) {
        int size = collection.size() + 1;
        PriorityQueue<City> helper = new PriorityQueue<>();
        City city;

        for (int i = 1; i < size; i++) {
            city = collection.remove();
            if (city.getId() != Integer.parseInt(argument)) {
                helper.add(city);
            } else {
                break;
            }
        }
        for (City cityy : helper) {
            while (!helper.isEmpty()) {
                cityy = helper.remove();
                collection.add(cityy);
            }
        }
    }
}
