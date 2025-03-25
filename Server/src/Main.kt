import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

const val PORT = 12345
const val LOG_FILE = "server_log.txt"
const val SERVER_DELAY_MS = 30000L // Задержка сервера для эмуляции работы
const val CLIENT_TIMEOUT_MS = 10000L // Время работы клиента до отключения

fun main() {
    val server = ServerSocket(PORT)
    println("Сервер запущен на порту $PORT")
    logToFile("Сервер запущен. Время запуска: ${getCurrentTime()}")

    while (true) {
        val clientSocket = server.accept()
        println("Клиент подключен: ${clientSocket.inetAddress}")
        logToFile("Клиент подключен: ${clientSocket.inetAddress}. Время: ${getCurrentTime()}")

        thread {
            handleClient(clientSocket)
        }
    }
}

fun handleClient(clientSocket: Socket) {
    val startTime = System.currentTimeMillis()
    val clientAddress = clientSocket.inetAddress

    try {
        val input = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val output = PrintWriter(clientSocket.getOutputStream(), true)

        val clientMessage = input.readLine()
        println("Получено сообщение от клиента: $clientMessage")
        logToFile("Получено сообщение от клиента: $clientMessage. Время: ${getCurrentTime()}")

        Thread.sleep(SERVER_DELAY_MS)

        val reversedMessage = clientMessage.reversed()

        val response = "$reversedMessage. Сервер написан Бандуковым И.Ю. БСБО-12-22"

        output.println(response)
        println("Отправлено сообщение клиенту: $response")
        logToFile("Отправлено сообщение клиенту: $response. Время: ${getCurrentTime()}")

        while (System.currentTimeMillis() - startTime < CLIENT_TIMEOUT_MS) {
            // Ожидание до истечения времени
        }

        println("Клиент отключен: $clientAddress")
        logToFile("Клиент отключен: $clientAddress. Время: ${getCurrentTime()}")
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        clientSocket.close()
    }
}

fun logToFile(message: String) {
    try {
        FileWriter(LOG_FILE, true).use { writer ->
            writer.write("$message\n")
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun getCurrentTime(): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
}