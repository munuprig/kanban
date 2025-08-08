package handlers;

import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.Task;

import java.io.IOException;
import java.util.Collection;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
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
        if (path.equals("/tasks")) {
            Collection<Task> tasks = taskManager.getTasks();
            sendText(exchange, 200, gson.toJson(tasks));
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3 && parts[1].equals("tasks")) {
                int taskId = Integer.parseInt((parts[2]));
                Task task = taskManager.getTask(taskId);
                if (task != null) {
                    sendText(exchange, 200, gson.toJson(task));
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
        Task task = gson.fromJson(requestBody, Task.class);
        taskManager.addNewTask(task);
//        sendText(exchange, 201, gson.toJson(task));
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("tasks")) {
            int taskId = Integer.parseInt((parts[2]));
            taskManager.deleteTask(taskId);
            sendText(exchange, 204, "");
        } else {
            sendNotFound(exchange);
        }
    }
}
