package handlers;

import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.SubTask;

import java.io.IOException;
import java.util.Collection;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        switch (requestMethod) {
            case "GET":
                handleGetRequest(exchange);
                break;
            case "POST":
                handlePostRequest(exchange);
                break;
            case "DELETE":
                handleDeleteRequest(exchange);
                break;
            default:
                exchange.sendResponseHeaders(405, -1); // Метод не поддерживается
                break;
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        if (path.equals("/subtasks")) {
            Collection<SubTask> subtasks = taskManager.getSubTasks();
            sendText(exchange, 200, gson.toJson(subtasks));
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3 && parts[1].equals("subtasks")) {
                int subtaskId = Integer.parseInt(parts[2]);
                SubTask subtask = taskManager.getSubTask(subtaskId);
                if (subtask != null) {
                    sendText(exchange, 200, gson.toJson(subtask));
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        SubTask subtask = gson.fromJson(requestBody, SubTask.class);
        int result = taskManager.addNewSubTask(subtask);
        if (result == -1) {
            sendHasOverlaps(exchange);
        } else if (result == 0) {
            sendNotFound(exchange);
        } else {
            sendText(exchange, 201, gson.toJson(subtask));
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("subtasks")) {
            int subtaskId = Integer.parseInt(parts[2]);
            taskManager.deleteSubtask(subtaskId);
            sendText(exchange, 204, "");
        } else {
            sendNotFound(exchange);
        }
    }
}
