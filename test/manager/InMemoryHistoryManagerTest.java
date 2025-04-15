package manager;

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
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не найдена.");
    }

    @Test
    void addHistory() {
        final Task task = new Task("dda", " adada");

        historyManager.addHistory(task);

        final List<Task> history = historyManager.getHistory();
        final Task savedTask = history.getLast();

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    }

    @BeforeEach
    void initHistoryManager(){
        historyManager = Managers.getDefaultHistory();
    }
}