package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if ("GET".equals(requestMethod)) {
            handleGetRequest(exchange);
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        sendText(exchange, 200, gson.toJson(prioritizedTasks));
    }
}
