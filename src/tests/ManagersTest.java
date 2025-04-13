package tests;

import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void getDefaultHistory() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager, "Возвращает пустой класс");
    }

    @Test
    void getDefault() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Возвращает пустой класс");
    }
}