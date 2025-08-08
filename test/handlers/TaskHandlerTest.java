package handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskHandlerTest {

    private TaskManager taskManager;
    private HttpTaskServer taskServer;
    private Gson gson;

    @BeforeEach
    public void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(taskManager);
        gson = HttpTaskServer.getGson();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 1, 0),
                Duration.ofMinutes(30));
        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasks = (List<Task>) taskManager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.getFirst().getName());
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 1, 0),
                Duration.ofMinutes(30));
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task retrievedTask = gson.fromJson(response.body(), Task.class);
        assertEquals("Test Task", retrievedTask.getName());
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 1, 0),
                Duration.ofMinutes(30));
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasks = gson.fromJson(response.body(), new TypeToken<List<Task>>() {
        }.getType());
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.getFirst().getName());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 1, 0),
                Duration.ofMinutes(30));
        taskManager.addNewTask(task1);

        task1.setName("Updated Task");
        String taskJson = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.
                ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task updatedTask = taskManager.getTask(task1.getId());
        assertEquals("Updated Task", updatedTask.getName());
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 1, 0),
                Duration.ofMinutes(30));
        taskManager.addNewTask(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + task1.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

        List<Task> tasks = (List<Task>) taskManager.getTasks();
        assertEquals(0, tasks.size());
    }

}