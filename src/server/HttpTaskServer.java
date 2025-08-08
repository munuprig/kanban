package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpServer;
import handlers.*;
import interfaces.TaskManager;
import manager.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    private final TaskManager taskManager;
    private static final int PORT = 8080;
    private HttpServer server;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        System.out.println("Server started on port " + PORT);

        // Регистрируем обработчики для различных маршрутов
        server.createContext("/tasks", new TaskHandler(taskManager, getGson()));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, getGson()));
        server.createContext("/epics", new EpicHandler(taskManager, getGson()));
        server.createContext("/history", new HistoryHandler(taskManager, getGson()));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager, getGson()));

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
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public void write(JsonWriter jsonWriter, LocalDateTime value) throws IOException {
            if (value == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(value.format(formatter));
            }
        }

        @Override
        public LocalDateTime read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            String dateStr = jsonReader.nextString();
            return LocalDateTime.parse(dateStr, formatter);
        }
    }
}
