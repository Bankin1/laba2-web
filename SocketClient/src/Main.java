import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Main {
    private static final String CONFIG_FILE = "D:\\Study\\РТУ МИРЭА\\3 курс 6 сем\\Разработка веб приложений\\2\\SocketClient\\src\\config.properties";
    private static final String LOG_FILE = "client_log.txt";
    private static final int SEND_INTERVAL = 2000; // Интервал отправки сообщений в миллисекундах

    public static void main(String[] args) {
        Properties config = loadConfig();
        if (config == null) {
            System.out.println("Не удалось загрузить конфигурацию.");
            return;
        }

        String serverAddress = config.getProperty("server.address");
        int serverPort = Integer.parseInt(config.getProperty("server.port"));

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {

            logConnection(logWriter, serverAddress, serverPort);

            while (true) {
                String messageToSend = "Бандуков Илья Юрьевич и БСБО-12-22";
                logMessageSent(logWriter, messageToSend);
                out.println(messageToSend);

                String response = in.readLine();
                logMessageReceived(logWriter, response);

                Thread.sleep(SEND_INTERVAL);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Properties loadConfig() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void logConnection(BufferedWriter logWriter, String address, int port) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logWriter.write(timeStamp + " - Подключение к серверу: " + address + ":" + port);
        logWriter.newLine();
        logWriter.flush(); // Очистка буфера и запись в файл
    }

    private static void logMessageSent(BufferedWriter logWriter, String message) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logWriter.write(timeStamp + " - Отправлено сообщение: " + message);
        logWriter.newLine();
        logWriter.flush(); // Очистка буфера и запись в файл
    }

    private static void logMessageReceived(BufferedWriter logWriter, String message) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        logWriter.write(timeStamp + " - Получено сообщение: " + message);
        logWriter.newLine();
        logWriter.flush(); // Очистка буфера и запись в файл
    }
}