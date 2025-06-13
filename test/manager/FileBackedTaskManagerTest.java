package manager;

import data.ProgressTask;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    @Test
    public void testLoadAndSave() throws Exception {
        File tempFile = File.createTempFile("tasks", ".csv"); // Временный файл
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        final Task task = new Task("1", ProgressTask.NEW, 0, "1");
        final Epic epic = new Epic("2", "2");

        manager.addNewTask(task);

        final int epicId = manager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", epicId);

        manager.addNewSubTask(subTask);

        assertTrue(tempFile.exists(), "Файл не был создан.");

        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(restoredManager.getTask(task.getId()), "Обычная задача не найдена");
        assertEquals(ProgressTask.NEW, restoredManager.getTask(task.getId()).getStatus(),
                "Статус обычной задачи неверен");
        assertEquals("1", restoredManager.getTask(task.getId()).getName(), "Название задачи неверно");
        assertEquals("1", restoredManager.getTask(task.getId()).getInfo(), "Описание задачи неверно");

        assertNotNull(restoredManager.getEpic(epic.getId()), "Обычная эпик не найдена");
        assertEquals(ProgressTask.NEW, restoredManager.getEpic(epic.getId()).getStatus(),
                "Статус эпика неверен");
        assertEquals("2", restoredManager.getEpic(epic.getId()).getName(), "Название эпика неверно");
        assertEquals("2", restoredManager.getEpic(epic.getId()).getInfo(), "Описание эпика неверно");

        assertNotNull(restoredManager.getSubTask(subTask.getId()), "Обычная подзадача не найдена");
        assertEquals(ProgressTask.NEW, restoredManager.getSubTask(subTask.getId()).getStatus(),
                "Статус подзадачи неверен");
        assertEquals("3", restoredManager.getSubTask(subTask.getId()).getName(),
                "Название подзадачи неверно");
        assertEquals("3", restoredManager.getSubTask(subTask.getId()).getInfo(),
                "Описание подзадачи неверно");
        assertEquals(epic.getId(),restoredManager.getSubTask(subTask.getId()).getIdEpic(),
                "Идентификатор эпика подзадачи неверен");

        tempFile.deleteOnExit();
    }

    @Test
    public void testLoadEmptyFile() throws Exception {
        File tempFile = File.createTempFile("empty_tasks", ".csv");
        FileBackedTaskManager emptyManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(emptyManager.getTasks().isEmpty(), "Задачи не пусты");
        assertTrue(emptyManager.getEpics().isEmpty(), "Эпики не пусты");
        assertTrue(emptyManager.getSubTasks().isEmpty(), "Подзадачи не пусты");

        tempFile.deleteOnExit();
    }
}
