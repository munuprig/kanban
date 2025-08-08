package handlers;


import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import models.Epic;

import java.io.IOException;
import java.util.Collection;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
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
            case "PUT":
                handlePutRequest(exchange);
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
            } else {
                sendNotFound(exchange);
            }
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = readRequestBody(exchange);
        Epic epic = gson.fromJson(requestBody, Epic.class);
        int result = taskManager.addNewEpic(epic);
        if (result == -1) {
            sendHasOverlaps(exchange);
        } else if (result == 0) {
            sendNotFound(exchange);
        } else {
            sendText(exchange, 201, gson.toJson(epic));
        }
    }

    private void handlePutRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("epics")) {
            int epicId = Integer.parseInt(parts[2]);
            String requestBody = readRequestBody(exchange);
            Epic updatedEpic = gson.fromJson(requestBody, Epic.class);
            updatedEpic.setId(epicId);
            taskManager.updateEpic(updatedEpic);
            sendText(exchange, 200, gson.toJson(updatedEpic));
        } else {
            sendNotFound(exchange);
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");
        if (parts.length == 3 && parts[1].equals("epics")) {
            int epicId = Integer.parseInt(parts[2]);
            taskManager.deleteEpic(epicId);
            sendText(exchange, 204, "");
        } else {
            sendNotFound(exchange);
        }
    }
}
