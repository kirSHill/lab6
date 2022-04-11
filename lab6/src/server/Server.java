package server;

import client.Client;
import essentials.Xml;
import essentials.commands.interfaces.Command;
import essentials.commands.interfaces.Date;
import essentials.elements.QueueInfo;
import essentials.interaction.ConsoleInteraction;
import essentials.interaction.Message;
import essentials.elements.City;
import essentials.interaction.UserInteraction;
import java.io.*;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.logging.Logger;

public class Server {

    private static final UserInteraction interaction = new ConsoleInteraction();
    private static PriorityQueue<City> collection = new PriorityQueue<>();
    public static File file;
    public static final int port = 7182;
    private static LocalDateTime creationDate;
    private static LocalDateTime initDate;
    private static final Logger log = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) throws Exception {
        if (System.getenv("FILE_LOC") != null && !System.getenv("FILE_LOC").trim().isEmpty()) {
            file = new File(System.getenv("FILE_LOC"));
            log.info("Переменная окружения установлена!\n\nПодготовка к запуску.");
        }
        if(!prepare()) {
            log.info("Остановка запуска.");
            return;
        }
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(Xml.toXml(new QueueInfo(collection, City.getMaxId(), creationDate, initDate)));
                    fileWriter.flush();
                } catch (Exception e) {
                    log.info(e.getMessage());
                }
                log.info("Остановка сервера.");
            }));
        } catch (Exception e) {
            log.info("Не удалось настроить условие выхода.");
            return;
        }
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            log.info(String.format("Невозможно запустить сервер (%s)%n", e.getMessage()));
            return;
        }
        InetAddress inetAddress = serverSocket.getInetAddress();
        log.info("Сервер запущен по адресу: " + inetAddress.getHostAddress());
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                log.info(String.format("Клиент %s:%s присоединился!",socket.getInetAddress(),socket.getPort()));
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                while (!(socket.isClosed())) {
                    Command command = (Command) inputStream.readObject();
                    log.info("Введена команда "+command);
                    Message message;
                    if (command instanceof Date) {
                        message = ((Date) command).execute(collection, initDate);
                    } else {
                        message = command.execute(collection);
                    }
                    outputStream.writeObject(message);
                }
                inputStream.close();
                outputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                log.info("Соединение потеряно! ");
            }
        }
    }

    private static void uploadInformation() throws FileNotFoundException, IllegalAccessException, NoSuchFieldException {
        log.info("Загрузка записей из файла " + file);
        QueueInfo queueInfo = Xml.fromXml(file);
        collection = Objects.requireNonNull(queueInfo).getCollection();
        creationDate = queueInfo.getCreationDate();
        initDate = queueInfo.getInitDate();
        Field field = City.class.getDeclaredField("maxId");
        field.setAccessible(true);
        field.setInt(null, queueInfo.getMaxId());
        log.info("Коллекция была успешно загружена!\n");
    }

    private static boolean prepare() {
        try {
            uploadInformation();
        } catch (FileNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            if (e instanceof NoSuchFieldException || e instanceof IllegalAccessException || e instanceof NullPointerException) {
                log.info("Возникли проблемы при обработке файла. Данные не считаны. Создаём новый файл.");
            }
            initDate = LocalDateTime.now();
            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(file);
                fileWriter.close();
            } catch (IOException ex) {
                log.info("Файл не может быть создан, недостаточно прав доступа или формат имени файла неверен.");
                log.info("Сообщение об ошибке: " + ex.getMessage());
                return false;
            }
        }
        return true;
    }
}


