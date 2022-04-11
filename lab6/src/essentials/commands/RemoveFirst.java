package essentials.commands;

import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;

import java.util.PriorityQueue;

public class RemoveFirst implements Command {


    public RemoveFirst() {}


    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        if (collection.size() == 0) {
            return new Message(true, "В коллекции нет элементов.");
        } else {
            collection.remove();
            return new Message(true,"Первый элемент коллекции удалён.");
        }
    }
}
