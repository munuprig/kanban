package models;

import data.ProgressTask;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;
    Integer idEpic;
    TaskManager inMemoryTaskManager;

    @BeforeEach
    public void setUp() {
        inMemoryTaskManager = Managers.getDefault();
        epic = new Epic("1", "1");
        idEpic = inMemoryTaskManager.addNewEpic(epic);
    }

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
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);

        final Collection<SubTask> savedSubTasks = epic.getSubTask();

        assertNotNull(savedSubTasks, "Подзадачи не найдена.");
    }

    @Test
    void addSubTask() {
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);
        assertEquals(1, epic.getSubTask().size(), "Неверное количество задач.");
    }

    @Test
    void deleteSubTasks() {
        final SubTask subTask = new SubTask("Test", "Test", 0);

        subTask.setId(1);
        epic.addSubTask(subTask);

        final Collection<SubTask> oldSubTasks = epic.getSubTask();

        epic.deleteSubTasks();
        assertEquals(oldSubTasks, epic.getSubTask(), "Подзадачи не удались.");
    }

    @Test
    void allNewStatus() {
        SubTask subTask1 = new SubTask("Task1", "Desc1", ProgressTask.NEW , epic.getId());
        SubTask subTask2 = new SubTask("Task2", "Desc2", ProgressTask.NEW , epic.getId());

        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(ProgressTask.NEW, epic.getStatus());
    }

    @Test
    void allDoneStatus() {
        SubTask subTask1 = new SubTask("Task1", "Desc1", ProgressTask.DONE , epic.getId());
        SubTask subTask2 = new SubTask("Task2", "Desc2", ProgressTask.DONE , epic.getId());

        subTask1.setId(1);
        subTask2.setId(2);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(ProgressTask.DONE, epic.getStatus());
    }

    @Test
    void mixedStatuses() {
        SubTask subTask1 = new SubTask("Task1", "Desc1", ProgressTask.NEW , epic.getId());
        SubTask subTask2 = new SubTask("Task2", "Desc2", ProgressTask.DONE , epic.getId());

        subTask1.setId(1);
        subTask2.setId(2);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(ProgressTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void inProgressStatus() {
        SubTask subTask1 = new SubTask("Task1", "Desc1", ProgressTask.IN_PROGRESS , epic.getId());
        SubTask subTask2 = new SubTask("Task2", "Desc2", ProgressTask.DONE , epic.getId());

        subTask1.setId(1);
        subTask2.setId(2);
        epic.addSubTask(subTask1);
        epic.addSubTask(subTask2);

        assertEquals(ProgressTask.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void getSetTimeTest(){
        final SubTask subTask1 = new SubTask("3", "3", LocalDateTime.of
                (2024, 10, 1, 9, 30),
                Duration.ofMinutes(30), epic.getId());
        final SubTask subTask2 = new SubTask("3", "3", LocalDateTime.of
                (2025, 10, 1, 9, 30),
                Duration.ofMinutes(30), epic.getId());

        inMemoryTaskManager.addNewSubTask(subTask1);
        inMemoryTaskManager.addNewSubTask(subTask2);

        assertEquals(epic.getDuration(), subTask1.getDuration().plus(subTask2.getDuration()),
                "Время выполнения Эпика расчитывается не правильно.");
        assertEquals(epic.getStartTime(), subTask1.getStartTime(),
                "Время начала выполнения Эпика расчитывается не правильно.");
        assertEquals(epic.getEndTime(), subTask2.getEndTime(),
                "Время конца выполнения Эпика расчитывается не правильно.");
    }
}