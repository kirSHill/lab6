package essentials.commands;

import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.elements.Climate;
import essentials.interaction.Message;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class CountByClimate implements Command {

    private final String argument;

    public CountByClimate(ArrayList<String> args) {
        this.argument = args.get(0);
    }


    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        Climate climate = Climate.getByName(argument);
        String result = collection.stream().filter((city) -> city.getClimate() == climate).collect(Collectors.toList()).toString();
        return new Message(true, result);
    }
}