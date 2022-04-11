package client;

import essentials.Commander;
import essentials.commands.ExecuteScript;
import essentials.commands.Exit;
import essentials.commands.interfaces.Command;
import essentials.commands.interfaces.Preprocessable;
import essentials.interaction.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
   private static final UserInteraction interaction = new ConsoleInteraction();
   private static ServerInteraction serverInteraction;
   private static int port;
   private static String ip;
   private static Socket socket;
   private static final Logger log = Logger.getLogger(Client.class.getName());

   public static void main(String[] args) {
      try {
         String[] argument = args[0].split(":");
         if (argument.length != 2) {
            throw new Exception();
         }
         ip = argument[0];
         port = Integer.parseInt(argument[1]);
      } catch (Exception e) {
         log.info("Неверно указан адрес и/или порт. Введите в формате '*.*.*.*:????'");
         return;
      }
      connect();
      run();
   }
   public static void connect() {
      log.info("Подключение к серверу.");
      while (true) {
         try {
            socket = new Socket(ip, port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            serverInteraction = new ServerInteraction(inputStream, outputStream);
            return;
         } catch (IOException e) {
            log.info("Неудачное подключение к серверу " + e.getMessage());
         }
      }
   }

   public static void run() {
      interaction.print(true, "Для просмотра полного списка команд введите 'help'.");
      boolean run = true;
      boolean reconnect = false;
      while (run) {
         try {
            if (reconnect | socket.isClosed() | !socket.isConnected()) {
               connect();
               reconnect = false;
            }
            interaction.print(false, "\nВведите команду: ");
            String potencialCommand = interaction.getData();
            if (potencialCommand == null) {
               continue;
            }
            Command command = Commander.getCommand(potencialCommand, false, interaction);
            if (command == null) {
               continue;
            }
            if (command instanceof Exit) {
               return;
            }
            if (command instanceof Preprocessable) {
               ((Preprocessable) command).preprocess(interaction);
            }
            if (command instanceof ExecuteScript) {
               boolean result = true;
               File file = new File(((ExecuteScript) command).getArgument());
               Scanner scanner;
               try {
                  scanner = new Scanner(file);
               } catch (FileNotFoundException e) {
                  interaction.print(true, "Введённого Вами файла не существует!");
                  continue;
               }
               int number = 1;
               while (scanner.hasNextLine()) {
                  String line = scanner.nextLine();
                  if (line.trim().isEmpty()) {
                     continue;
                  }
                  try {
                     ScriptInteraction scriptInteraction = new ScriptInteraction(scanner);
                     Command command1 = Commander.getCommand(line, true, scriptInteraction);
                     if (command1 == null) {
                        continue;
                     }
                     if (command1 instanceof Exit) {
                        return;
                     }
                     if (command1 instanceof Preprocessable) {
                        ((Preprocessable) command1).preprocess(scriptInteraction);
                     }
                     serverInteraction.sendData(command1);
                     Message message = (Message) serverInteraction.readData();
                     if (!message.isSuccessful()) {
                        throw new Exception();
                     }
                  } catch (Exception e) {
                     log.info("Ошибка при выполнении строки " + number);
                     result = false;
                     break;
                  }
                  number++;
               }
               if (result) {
                  log.info("Команды выполнились успешно!");
               }
               continue;
            }
            try {
               serverInteraction.sendData(command);
            } catch (IOException e) {
               log.info("Ошибка. "+e.getMessage());
               reconnect = true;
               continue;
            }
            Message message;
            try {
               message = (Message) serverInteraction.readData();
            } catch (Exception e) {
               log.info("Ошибка."+e.getMessage());
               reconnect = true;
               continue;
            }
            interaction.print(true,message.getText());
            if (!message.isSuccessful()) {
               run = false;
            }
         } catch (Exception e) {
            log.info("Ошибка." + e.getMessage());
         }
      }
   }
}