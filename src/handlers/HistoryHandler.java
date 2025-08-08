package handlers;

import interfaces.TaskManager;
import models.Task;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if ("GET".equals(requestMethod)) {
            handleGetRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1); // Метод не поддерживается
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Task> history = taskManager.getHistory();
        sendText(exchange, 200, gson.toJson(history));
    }
}