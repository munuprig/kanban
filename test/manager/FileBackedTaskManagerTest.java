package manager;

import data.ProgressTask;
import interfaces.TaskManager;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<TaskManager>{
    private File tempFile;

    @Override
    @BeforeEach
    public void setup() {
        try {
            tempFile = File.createTempFile("tasks", ".csv");
            this.taskManager = new FileBackedTaskManager(tempFile); // Создаем новый менеджер задач
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanup() {
        if (tempFile != null && !tempFile.delete()) {
            System.err.println("Failed to delete temporary file: " + tempFile.getAbsolutePath());
        }
    }

    @Test
    void testLoadAndSave(){
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);

        Duration durationExpectedTask = Duration.ofMinutes(30);
        LocalDateTime localDateTimeExpectedTask = LocalDateTime.of
                (2023, 10, 1, 9, 30);

        final Task task = new Task("1", "1", LocalDateTime.of
                (2023, 10, 1, 9, 30),
                Duration.ofMinutes(30));
        final Epic epic = new Epic("2", "2");

        manager.addNewTask(task);

        Duration durationExpected = Duration.ofMinutes(30);
        LocalDateTime localDateTimeExpected = LocalDateTime.of
                (2024, 10, 1, 9, 30);
        final int epicId = manager.addNewEpic(epic);
        final SubTask subTask = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 9, 30),
                Duration.ofMinutes(30), epicId);

        manager.addNewSubTask(subTask);

        assertTrue(tempFile.exists(), "Файл не был создан.");

        FileBackedTaskManager restoredManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertNotNull(restoredManager.getTask(task.getId()), "Обычная задача не найдена");
        assertEquals(ProgressTask.NEW, restoredManager.getTask(task.getId()).getStatus(),
                "Статус обычной задачи неверен");
        assertEquals("1", restoredManager.getTask(task.getId()).getName(), "Название задачи неверно");
        assertEquals("1", restoredManager.getTask(task.getId()).getInfo(), "Описание задачи неверно");
        assertEquals(restoredManager.getTask(task.getId()).getDuration(), durationExpectedTask,
                "Время выполнения задачи не правильно.");
        assertEquals(restoredManager.getTask(task.getId()).getStartTime(), localDateTimeExpectedTask,
                "Время начала выполнения задачи не правильно.");
        assertEquals(restoredManager.getTask(task.getId()).getEndTime(),
                localDateTimeExpectedTask.plus(durationExpectedTask),
                "Время конца выполнения задачи расчитывается не правильно.");

        assertNotNull(restoredManager.getEpic(epic.getId()), "Обычный эпик не найден");
        assertEquals(ProgressTask.NEW, restoredManager.getEpic(epic.getId()).getStatus(),
                "Статус эпика неверен");
        assertEquals("2", restoredManager.getEpic(epic.getId()).getName(), "Название эпика неверно");
        assertEquals("2", restoredManager.getEpic(epic.getId()).getInfo(), "Описание эпика неверно");
        assertEquals(epic.getSubTask().size(), restoredManager.getEpic(epic.getId()).getSubTask().size(),
                "Подзадачи не были востоновлены");
        assertEquals(restoredManager.getEpic(epic.getId()).getStartTime(),
                restoredManager.getSubTask(subTask.getId()).getStartTime(),
                "Время начала выполнения Эпика расчитывается не правильно.");
        assertEquals(restoredManager.getEpic(epic.getId()).getEndTime(),
                restoredManager.getSubTask(subTask.getId()).getEndTime(),
                "Время конца выполнения Эпика расчитывается не правильно.");
        assertEquals(restoredManager.getEpic(epic.getId()).getDuration(),
                restoredManager.getSubTask(subTask.getId()).getDuration(),
                "Время выполнения Эпика расчитывается не правильно.");

        assertNotNull(restoredManager.getSubTask(subTask.getId()), "Обычная подзадача не найдена");
        assertEquals(ProgressTask.NEW, restoredManager.getSubTask(subTask.getId()).getStatus(),
                "Статус подзадачи неверен");
        assertEquals("3", restoredManager.getSubTask(subTask.getId()).getName(),
                "Название подзадачи неверно");
        assertEquals("3", restoredManager.getSubTask(subTask.getId()).getInfo(),
                "Описание подзадачи неверно");
        assertEquals(epic.getId(),restoredManager.getSubTask(subTask.getId()).getIdEpic(),
                "Идентификатор эпика подзадачи неверен");
        assertEquals(restoredManager.getSubTask(subTask.getId()).getDuration(), durationExpected,
                "Время выполнения задачи не правильно.");
        assertEquals(restoredManager.getSubTask(subTask.getId()).getStartTime(), localDateTimeExpected,
                "Время начала выполнения задачи не правильно.");
        assertEquals(restoredManager.getSubTask(subTask.getId()).getEndTime(),
                localDateTimeExpected.plus(durationExpected),
                "Время конца выполнения задачи расчитывается не правильно.");

        assertEquals(manager.id, restoredManager.id, "Id неверен");

        tempFile.deleteOnExit();
    }

    @Test
    void testLoadEmptyFile() throws Exception {
        File tempFile = File.createTempFile("empty_tasks", ".csv");
        FileBackedTaskManager emptyManager = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(emptyManager.getTasks().isEmpty(), "Задачи не пусты");
        assertTrue(emptyManager.getEpics().isEmpty(), "Эпики не пусты");
        assertTrue(emptyManager.getSubTasks().isEmpty(), "Подзадачи не пусты");

        tempFile.deleteOnExit();
    }
}
