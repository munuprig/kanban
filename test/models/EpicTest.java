package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEquals() {
        final Task epic1 = new Epic("Test1", "test1");
        final Task epic2 = new Epic("Test2", "test2");

        epic1.setId(0);
        epic2.setId(0);

        assertEquals(epic1, epic2, "Разные задачи");
    }

    @Test
    void getSubTask() {
        final Epic epic = new Epic("Test1", "test1");
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);

        final Collection<SubTask> savedSubTasks= epic.getSubTask();

        assertNotNull(savedSubTasks, "Подзадачи не найдена.");
    }

    @Test
    void addSubTask() {
        final Epic epic = new Epic("Test1", "test1");
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);
        assertEquals(1, epic.getSubTask().size(), "Неверное количество задач.");
    }

    @Test
    void deleteSubTasks() {
        final Epic epic = new Epic("Test1", "test1");
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);

        final Collection<SubTask> oldSubTasks= epic.getSubTask();

        epic.deleteSubTasks();
        assertEquals(oldSubTasks ,epic.getSubTask(), "Подзадачи не удались.");
    }
}