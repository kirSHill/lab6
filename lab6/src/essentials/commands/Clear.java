package essentials.commands;

import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;
import java.util.PriorityQueue;

public class Clear implements Command {


    public Clear() {}

    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        collection.clear();
        return new Message(true, "Коллекция успешно очищена.");
    }
}
