package manager;

import data.ProgressTask;
import interfaces.HistoryManager;
import models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @Test
    void getHistory() {
        final Task task1 = new Task("1", ProgressTask.NEW, 0 , "1");
        final Task task2 = new Task("2", ProgressTask.NEW, 1 , "1");
        final Task task3 = new Task("3", ProgressTask.NEW, 2 , "1");

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);

        assertNotNull(historyManager.getHistory(), "История не найдена.");
    }

    @Test
    void addHistory() {
        final Task task1 = new Task("1", ProgressTask.NEW, 0 , "1");
        final Task task2 = new Task("2", ProgressTask.NEW, 1 , "1");
        final Task task3 = new Task("3", ProgressTask.NEW, 2 , "1");

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);

        List<Task> expected = List.of(task1, task2, task3);

        assertNotNull(historyManager.getHistory(), "После добавления задачи, история не должна быть пустой.");
        assertEquals(expected, historyManager.getHistory(), "После добавления задачи, история не должна быть пустой.");
    }

    @Test
    void remove() {
        final HistoryLinkedList history = new HistoryLinkedList();
        final Task task1 = new Task("1", ProgressTask.NEW, 0 , "1");
        final Task task2 = new Task("2", ProgressTask.NEW, 1 , "1");
        final Task task3 = new Task("3", ProgressTask.NEW, 2 , "1");

        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);

        List<Task> oldHistory = history.getTasks();

        historyManager.remove(1);

        assertNotNull(historyManager.getHistory(), "Должны удалиться не все задачи.");
        assertNotEquals(oldHistory, historyManager.getHistory(), "Задачи не удались.");
    }

    @BeforeEach
    void initHistoryManager(){
        historyManager = Managers.getDefaultHistory();
    }
}