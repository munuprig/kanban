package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import interfaces.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new Gson();
    }

    public abstract void handle(HttpExchange exchange) throws IOException;

    // Метод для отправки общего ответа в случае успеха
    protected void sendText(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }

    // Метод для отправки ответа в случае, если объект не был найден
    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendText(exchange, 404, "Not found");
    }

    // Метод для отправки ответа, если при создании или обновлении задача пересекается с уже существующими
    protected void sendHasOverlaps(HttpExchange exchange) throws IOException {
        sendText(exchange, 406, "Task overlaps with existing tasks");
    }

    // Метод для чтения тела запроса
    protected String readRequestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }
}
