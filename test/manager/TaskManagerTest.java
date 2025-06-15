package manager;

import interfaces.TaskManager;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager>{
    protected T taskManager;

    @BeforeEach
    public abstract void setup();

    @Test
    void getTask() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
    }

    @Test
    void getSubTask() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);
        final int subTaskId = taskManager.addNewSubTask(subTask);
        final Task savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
    }

    @Test
    void getEpic() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найден.");
    }

    @Test
    void getTasks() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");

        taskManager.addNewTask(task);

        final Collection<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.iterator().next(), "Задачи не совпадают.");
    }

    @Test
    void getSubTasks() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);

        taskManager.addNewSubTask(subTask);

        final Collection<SubTask> subTasks = taskManager.getSubTasks();

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
        assertEquals(subTask, subTasks.iterator().next(), "Подзадачи не совпадают.");
    }

    @Test
    void getEpics() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");

        taskManager.addNewEpic(epic);

        final Collection<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Epic не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Epic.");
        assertEquals(epic, epics.iterator().next(), "Epic не совпадают.");
    }

    @Test
    void getEpicSubTasks() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);

        taskManager.addNewSubTask(subTask);

        final Collection<SubTask> subTasks = taskManager.getEpicSubTasks(epicId);

        assertNotNull(subTasks, "Подзадачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void addNewTask() {
        final Task task = new Task("Test addNewTask", "Test addNewTask description");
        final int taskId = taskManager.addNewTask(task);
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic не найдена.");
        assertEquals(epic, savedEpic, "Epic не совпадают.");
    }

    @Test
    void addNewSubTask() {
        final Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("Test addNewTask",
                "Test addNewTask description", epicId);
        final int subTaskId = taskManager.addNewSubTask(subTask);
        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        assertNotNull(savedSubTask, "Подзадача не найдена.");
        assertEquals(subTask, savedSubTask, "Подзадачи не совпадают.");
    }

    @Test
    void delete() {
        final Epic epic = new Epic("1", "1");
        final int epicId = taskManager.addNewEpic(epic);
        final SubTask subTask = new SubTask("2", "2", epicId);
        final Task task = new Task("3", "3");

        taskManager.addNewTask(task);
        taskManager.addNewSubTask(subTask);

        taskManager.getEpic(epic.getId());
        taskManager.getSubTask(subTask.getId());
        taskManager.getTask(task.getId());


        List<Task> oldHistory = taskManager.getHistory();

        final Collection<Task> oldTasks = taskManager.getTasks();

        taskManager.deleteTask();

        assertEquals(oldTasks, taskManager.getTasks(), "Задачи не удались.");
        assertNotEquals(oldHistory, taskManager.getHistory(), "Задачи не удались из истории.");

        taskManager.deleteEpic();
        assertEquals(0, taskManager.getEpics().size(), "Epic не удались.");
        assertEquals(0, taskManager.getHistory().size(), "Должны удалиться все задачи.");
    }

}
