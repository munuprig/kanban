package manager;

import interfaces.TaskManager;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {

    @Override
    @BeforeEach
    public void setup() {
        this.taskManager = Managers.getDefault();
    }

    @Test
    void testCheckForConflicts() {
        TaskManager manager = Managers.getDefault();

        final Task task1 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 9, 30),
                Duration.ofMinutes(30));
        final Epic epic = new Epic("2", "2");

        manager.addNewTask(task1);

        final int epicId = manager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 9, 30),
                Duration.ofMinutes(30), epicId);

        manager.addNewSubTask(subTask);

        List<Task> list = manager.getPrioritizedTasks();

        assertEquals(2, list.size(),"Задачи не добавились из-за конфликта");

        final Task task2 = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 9, 30),
                Duration.ofMinutes(30));

        manager.addNewTask(task2);

        List<Task> listNew = manager.getPrioritizedTasks();

        assertEquals(listNew.size(), list.size(), "Задача добавилась из-за отстувия конфликтов");
    }

}