import java.io.IOException;
import java.util.Scanner;

public class ProcessManager {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

      for (int i = 1; i <= 3; i++) {
      System.out.println("\n=== Работа с процессом #" + i + " ===");
      System.out.print("Введите имя или путь к процессу: ");
      String processName = scanner.nextLine().trim();

      if (processName.isEmpty()) {
        System.out.println("Имя процесса не может быть пустым.");
        continue;
      }

      Process process = null;
      try {

        System.out.println("Запуск " + processName + "...");
        process = new ProcessBuilder(processName).start();


        ProcessHandle handle = process.toHandle();
        System.out.println("--- Информация о процессе ---");
        System.out.println("PID (Идентификатор): " + handle.pid());
        System.out.println("Статус активности: " + (handle.isAlive() ? "Запущен" : "Завершен"));
        System.out.println("Доступная память: "+Runtime.getRuntime().totalMemory());
        System.out.println("Доступные ядра: "+ Runtime.getRuntime().availableProcessors());

        handle.info().startInstant().ifPresent(start ->
                System.out.println("Время запуска: " + start));
        handle.info().command().ifPresent(cmd ->
                System.out.println("Путь к файлу: " + cmd));
        System.out.println("-----------------------------");


        boolean inputValid = false;
        while (!inputValid) {
          System.out.print("Завершить д/н? ");
          String answer = scanner.nextLine().trim().toLowerCase();

          if (answer.equals("д") || answer.equals("l")) {

            System.out.println("Завершение процесса...");
            process.destroy();


            int exitCode = process.waitFor();
            System.out.println("Процесс успешно завершен. Код выхода: " + exitCode);
            inputValid = true;
          } else if (answer.equals("н") || answer.equals("y")) {
            System.out.println("Процесс оставлен работать в системе.");
            inputValid = true;
          } else {
            System.out.println("Неверный ввод. Пожалуйста, введите 'д' (да) или 'н' (нет).");
          }
        }

      } catch (IOException e) {
        System.err.println("Ошибка: Не удалось запустить '" + processName + "'. Проверьте имя или путь.");
      } catch (InterruptedException e) {
        System.err.println("Поток ожидания был прерван.");
        Thread.currentThread().interrupt();
      }
    }

    System.out.println("\nРабота программы завершена.");
    scanner.close();
  }
}
