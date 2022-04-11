package essentials.commands.interfaces;

import essentials.elements.City;
import essentials.interaction.Message;
import essentials.interaction.UserInteraction;

import java.io.Serializable;
import java.util.PriorityQueue;

public interface ById extends Serializable {

    default Message search(UserInteraction interaction, PriorityQueue<City> collection, String argument) throws Exception {
        int count = 0;

        for (City cityy : collection) {
            if (cityy.getId() == Integer.parseInt(argument)) {
                count++;
            }
        }
        switch(count) {
            case 0:
                return new Message(true,"Элемент с id " + argument + " отсутствует в коллекции. \nВоспользуйтесь 'show' для просмотра всех элементов коллекции.");
            case 1:
                remove(interaction, collection, argument);
                break;
            default:
                return new Message(true,"В коллекции несколько элементов с одинаковыми id.");
        }
        return new Message(false, "");
    }

    default void remove(UserInteraction interaction, PriorityQueue<City> collection, String argument) throws Exception {
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
        interaction.print(true,"Элемент с id " + argument + " был удалён из коллекции.");

    }
}
