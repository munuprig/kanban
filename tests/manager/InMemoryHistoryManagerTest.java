package tests;

import interfaces.HistoryManager;
import manager.Managers;
import models.Epic;
import models.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static final HistoryManager historyManager = Managers.getDefaultHistory();
    private static final Task task = new Task("dda", " adada");

    @Test
    void getHistory() {
        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не найдена.");
    }

    @BeforeAll
    static void addHistory() {
        historyManager.addHistory(task);

        final List<Task> history = historyManager.getHistory();
        final Task savedTask = history.getLast();

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
    }

}