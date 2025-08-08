package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.SubTask;

import java.io.IOException;
import java.util.Collection;

public class SubtaskHandler extends BaseHttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
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
        if (subtask.getId() == null) {
            taskManager.addNewSubTask(subtask);
            sendText(exchange, 201, gson.toJson(subtask));
        } else if (subtask.getId() >= 0) {
            taskManager.updateSubTask(subtask);
            sendText(exchange, 201, gson.toJson(subtask));
        }
        sendHasOverlaps(exchange);
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        System.out.println(path);
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("subtasks")) {
            int subtaskId = Integer.parseInt(parts[2]);
            taskManager.deleteSubtask(subtaskId);
            sendText(exchange, 200, "");
        } else {
            sendNotFound(exchange);
        }
    }
}
