package manager;

import data.ProgressTask;
import interfaces.TaskManager;
import models.Epic;
import models.SubTask;
import models.Task;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
    @Test
    public void testLoadAndSave() throws Exception {
        File tempFile = File.createTempFile("tasks", ".csv"); // Временный файл
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        TaskManager managerMemory = Managers.getDefault();

        // Добавляем тестовые задачи
        final Task task = new Task("1", ProgressTask.NEW, 0, "1");
        final Epic epic = new Epic("2", "2");

        manager.addNewTask(task);

        final int epicId = manager.addNewEpic(epic);
        final SubTask subTask = new SubTask("1", "3", epicId);

        manager.addNewSubTask(subTask);

        // Проверка существования файла
        assertTrue(tempFile.exists(), "Файл не был создан.");

        // Загрузка данных обратно
        manager.loadFromFile(tempFile, managerMemory);

        // Проверка наличия задач
        assertEquals(managerMemory.getTasks().size(), manager.getTasks().size());
        assertEquals(managerMemory.getEpics().size(), manager.getEpics().size());
        assertEquals(managerMemory.getSubTasks().size(), manager.getSubTasks().size());

        // Чистка ресурсов
        tempFile.deleteOnExit();
    }
}
