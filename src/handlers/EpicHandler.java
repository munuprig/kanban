package handlers;


import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.Epic;
import models.SubTask;

import java.io.IOException;
import java.util.Collection;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
        if (path.equals("/epics")) {
            Collection<Epic> epics = taskManager.getEpics();
            sendText(exchange, 200, gson.toJson(epics));
        } else {
            String[] parts = path.split("/");
            if (parts.length == 3 && parts[1].equals("epics")) {
                int epicId = Integer.parseInt(parts[2]);
                Epic epic = taskManager.getEpic(epicId);
                if (epic != null) {
                    sendText(exchange, 200, gson.toJson(epic));
                } else {
                    sendNotFound(exchange);
                }
            } else if (parts.length == 4) {
                int epicId = Integer.parseInt(parts[2]);
                Collection<SubTask> subTasks = taskManager.getEpicSubTasks(epicId);
                if (subTasks != null) {
                    sendText(exchange, 200, gson.toJson(subTasks));
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
        Epic epic = gson.fromJson(requestBody, Epic.class);
        taskManager.addNewEpic(epic);
        if (taskManager.getEpics() != null) {
            sendText(exchange, 201, gson.toJson(epic));
        } else {
            sendHasOverlaps(exchange);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("epics")) {
            int epicId = Integer.parseInt(parts[2]);
            taskManager.deleteEpic(epicId);
            sendText(exchange, 200, "");
        } else {
            sendNotFound(exchange);
        }
    }
}
