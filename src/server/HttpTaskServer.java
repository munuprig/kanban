package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import interfaces.TaskManager;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    static TaskManager inMemoryTaskManager = Managers.getDefault();
    private static final int PORT = 8080;
    private HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        inMemoryTaskManager = taskManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("Server started on port " + PORT);

        // Регистрируем обработчики для различных маршрутов
        server.createContext("/tasks", new TaskHandler(inMemoryTaskManager));
        server.createContext("/subtasks", new SubtaskHandler(inMemoryTaskManager));
        server.createContext("/epics", new EpicHandler(inMemoryTaskManager));
        server.createContext("/history", new HistoryHandler(inMemoryTaskManager));
        server.createContext("/prioritized", new PrioritizedHandler(inMemoryTaskManager));

        server.start();
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(inMemoryTaskManager);
        server.start();
    }
}
