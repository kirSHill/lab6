package essentials.commands;

import essentials.commands.interfaces.Date;
import essentials.elements.City;
import essentials.interaction.Message;
import java.time.LocalDateTime;
import java.util.PriorityQueue;

public class Info implements Date {


    public Info() {}


    @Override
    public Message execute(PriorityQueue<City> collection, LocalDateTime initDate) {
        return new Message(true, "Информация о коллекции: \n" +
                "Тип: " + City.class.getName() + "\n" +
                "Дата инициализации: " + initDate + "\n" +
                "Количество элементов: " + collection.size() + "\n");
    }

    @Override
    public Message execute(PriorityQueue<City> collection) throws Exception {
        return new Message(false, "");
    }
}
