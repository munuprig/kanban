package manager;

import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.jupiter.api.*;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager inMemoryTaskManager;

    @BeforeEach
    void initInMemoryTaskManager() {
        inMemoryTaskManager = Managers.getDefault();
    }

    @Test
    void getTask() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = inMemoryTaskManager.addNewTask(task);
        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    void getSubTask() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);
        final int subTaskId = inMemoryTaskManager.addNewSubTask(subTask);
        final Task savedSubTask = inMemoryTaskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
    }

    @Test
    void getEpic() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найден.");
    }

    @Test
    void getTasks() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");

        inMemoryTaskManager.addNewTask(task);

        final Collection<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.iterator().next(), "Задачи не совпадают.");
    }

    @Test
    void getSubTasks() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);

        inMemoryTaskManager.addNewSubTask(subTask);

        final Collection<SubTask> subTasks = inMemoryTaskManager.getSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.iterator().next(), "Подзадачи не совпадают.");
    }

    @Test
    void getEpics() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");

        inMemoryTaskManager.addNewEpic(epic);

        final Collection<Epic> epics = inMemoryTaskManager.getEpics();

        assertNotNull(epics, "Epic не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Epic.");
        assertEquals(epic, epics.iterator().next(), "Epic не совпадают.");
    }

    @Test
    void getEpicSubTasks() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);

        inMemoryTaskManager.addNewSubTask(subTask);

        final Collection<SubTask> subTasks = inMemoryTaskManager.getEpicSubTasks(epicId);

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void addNewTask() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = inMemoryTaskManager.addNewTask(task);
        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найдена.");
        assertEquals(epic, savedEpic, "Epic не совпадают.");
    }

    @Test
    void addNewSubTask() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);
        final int subTaskId = inMemoryTaskManager.addNewSubTask(subTask);
        final SubTask savedSubTask = inMemoryTaskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
    }

    @Test
    void delete() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = inMemoryTaskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);
        final Task task = new Task("Test addNewTask", "Test addNewTask description");

        inMemoryTaskManager.addNewTask(task);
        inMemoryTaskManager.addNewSubTask(subTask);

        final Collection<Task> oldTasks = inMemoryTaskManager.getTasks();

        inMemoryTaskManager.deleteTask();
        assertEquals(oldTasks, inMemoryTaskManager.getTasks(), "Задачи не удались.");

        final Collection<Epic> oldEpics = inMemoryTaskManager.getEpics();
        final Collection<SubTask> oldSubTasks = inMemoryTaskManager.getSubTasks();

        inMemoryTaskManager.deleteEpic();
        assertEquals(oldEpics, inMemoryTaskManager.getEpics(), "Epic не удались.");
        assertEquals(oldSubTasks, inMemoryTaskManager.getSubTasks(), "Подзадачи не удались.");
    }

}