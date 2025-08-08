package handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import models.Epic;
import models.SubTask;
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

class SubtaskHandlerTest {

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
    public void testAddSubtask() throws IOException, InterruptedException {
        final Epic epic = new Epic("2", "2");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 2, 40),
                Duration.ofMinutes(30), epicId);
        String subtaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subtasks = (List<SubTask>) taskManager.getSubTasks();
        assertEquals(1, subtasks.size());
        assertEquals("Test Subtask", subtasks.getFirst().getName());
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        final Epic epic = new Epic("2", "2");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 2, 40),
                Duration.ofMinutes(30), epicId);
        taskManager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask retrievedSubtask = gson.fromJson(response.body(), SubTask.class);
        assertEquals("Test Subtask", retrievedSubtask.getName());
    }

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        final Epic epic = new Epic("2", "2");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 2, 40),
                Duration.ofMinutes(30), epicId);
        taskManager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subtasks = gson.fromJson(response.body(), new TypeToken<List<SubTask>>(){}.getType());
        assertEquals(1, subtasks.size());
        assertEquals("Subtask 1", subtasks.getFirst().getName());
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        final Epic epic = new Epic("2", "2");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 2, 40),
                Duration.ofMinutes(30), epicId);
        taskManager.addNewSubTask(subTask);

        subTask.setName("Updated Subtask");
        String subtaskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        SubTask updatedSubtask = taskManager.getSubTask(subTask.getId());
        assertEquals("Updated Subtask", updatedSubtask.getName());
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        final Epic epic = new Epic("2", "2");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 2, 40),
                Duration.ofMinutes(30), epicId);
        taskManager.addNewSubTask(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subTask.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

        List<SubTask> subtasks = (List<SubTask>) taskManager.getSubTasks();
        assertEquals(0, subtasks.size());
    }
}