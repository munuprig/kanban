package tests;

import models.Epic;
import models.SubTask;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private static final Epic epic = new Epic("Test", "Test");
    private static final SubTask subTask = new SubTask("Test", "Test", 0);

    @Test
    void getSubTask() {
        final Collection<SubTask> savedTasks= epic.getSubTask();
        assertNotNull(savedTasks, "Подзадача не найдена.");
    }

    @BeforeAll
    static void addSubTask() {
        subTask.setId(1);
        epic.addSubTask(subTask);
        assertEquals(1, epic.getSubTask().size(), "Неверное количество задач.");
    }

    @AfterAll
    static void deleteSubTasks() {
        final Collection<SubTask> oldSubTasks= epic.getSubTask();

        epic.deleteSubTasks();
        assertEquals(oldSubTasks ,epic.getSubTask(), "Подзадачи не удались.");
    }
}