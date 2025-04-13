package tests;

import models.SubTask;
import models.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    @Test
    void testEquals() {
        Task subTask1 = new SubTask("Test1", "test1", 0);
        Task subTask2 = new SubTask("Test2", "test2", 0);

        subTask1.setId(1);
        subTask2.setId(1);

        assertEquals(subTask1, subTask2, "Разные задачи");
    }
}