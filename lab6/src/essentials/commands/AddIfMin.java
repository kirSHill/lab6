package essentials.commands;


import essentials.commands.interfaces.Command;
import essentials.elements.City;
import essentials.interaction.Message;
import java.util.PriorityQueue;

public class AddIfMin extends Add {


    public AddIfMin(boolean fromScript, int update) {
        super(fromScript, update);
    }


    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        this.city.setId();
        if (city.getPopulation() < collection.peek().getPopulation()) {
            collection.add(city);
            return new Message(true, "В введённом Вами городе население ниже, чем в минимальном элементе коллекции, поэтому элемент добавлен в коллекцию.");
        } else {
            return new Message(true, "В введённом Вами городе население выше, чем в минимальном элементе коллекции. Выберите другое значение. (начните с 'addIfMin')");
        }
    }
}
