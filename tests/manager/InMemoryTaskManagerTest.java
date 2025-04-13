package tests;

import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private final static TaskManager inMemoryTaskManager = Managers.getDefault();
    private final static Task task = new Task("Test addNewTask", "Test addNewTask description");
    private final static Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
    static final int epicId = inMemoryTaskManager.addNewEpic(epic);
    static final int taskId = inMemoryTaskManager.addNewTask(task);
    private static final SubTask subTask = new SubTask("Test addNewTask",
            "Test addNewTask description", epicId);
    static final int subTaskId = inMemoryTaskManager.addNewSubTask(subTask);

    @Test
    void getTask() {
        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    void getSubTask() {
        final Task savedTask = inMemoryTaskManager.getSubTask(subTaskId);

        assertNotNull(savedTask, "Подзадача не найдена.");
    }

    @Test
    void getEpic() {
        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найден.");
    }

    @Test
    void getTasks() {
        final Collection<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.iterator().next(), "Задачи не совпадают.");
    }

    @Test
    void getSubTasks() {
        final Collection<SubTask> subTasks = inMemoryTaskManager.getSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.iterator().next(), "Подзадачи не совпадают.");
    }

    @Test
    void getEpics() {
        final Collection<Epic> epics = inMemoryTaskManager.getEpics();

        assertNotNull(epics, "Epic не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Epic.");
        assertEquals(epic, epics.iterator().next(), "Epic не совпадают.");
    }

    @Test
    void getEpicSubTasks() {
        final Collection<SubTask> subTasks = inMemoryTaskManager.getEpicSubTasks(epicId);

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
    }

    @BeforeEach
    void addNewTask() {
        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @BeforeEach
    void addNewEpic() {
        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найдена.");
        assertEquals(epic, savedEpic, "Epic не совпадают.");
    }

    @BeforeEach
    void addNewSubTask() {
        final SubTask savedSubTask = inMemoryTaskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
    }

    @AfterAll
    static void deleteTest() {

        final Collection<Task> oldTasks= inMemoryTaskManager.getTasks();

        inMemoryTaskManager.deleteTask();
        assertEquals(oldTasks ,inMemoryTaskManager.getTasks(), "Задачи не удались.");

        final Collection<Epic> oldEpics= inMemoryTaskManager.getEpics();
        final Collection<SubTask> oldSubTasks= inMemoryTaskManager.getSubTasks();

        inMemoryTaskManager.deleteEpic();
        assertEquals(oldEpics ,inMemoryTaskManager.getEpics(), "Epic не удались.");
        assertEquals(oldSubTasks ,inMemoryTaskManager.getSubTasks(), "Подзадачи не удались.");
    }

}