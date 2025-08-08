package handlers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.TaskManager;
import manager.InMemoryTaskManager;
import models.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EpicHandlerTest {
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
    public void testAddEpic() throws IOException, InterruptedException {
        final Epic epic = new Epic("1", "1");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epics = (List<Epic>) taskManager.getEpics();
        assertEquals(1, epics.size());
        assertEquals("Test Epic", epics.getFirst().getName());
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        final Epic epic = new Epic("1", "1");
        taskManager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic retrievedEpic = gson.fromJson(response.body(), Epic.class);
        assertEquals("Test Epic", retrievedEpic.getName());
    }

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        final Epic epic1 = new Epic("1", "1");
        final Epic epic2 = new Epic("2", "2");
        taskManager.addNewEpic(epic1);
        taskManager.addNewEpic(epic2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epics = gson.fromJson(response.body(), new TypeToken<List<Epic>>(){}.getType());
        assertEquals(2, epics.size());
        assertEquals("Epic 1", epics.get(0).getName());
        assertEquals("Epic 2", epics.get(1).getName());
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        final Epic epic = new Epic("1", "1");
        taskManager.addNewEpic(epic);

        epic.setName("Updated Epic");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic updatedEpic = taskManager.getEpic(epic.getId());
        assertEquals("Updated Epic", updatedEpic.getName());
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        final Epic epic = new Epic("1", "1");
        taskManager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(204, response.statusCode());

        List<Epic> epics = (List<Epic>) taskManager.getEpics();
        assertEquals(0, epics.size());
    }
}