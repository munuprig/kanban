package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskTest {

    @Test
    void testEquals() {
        Task task1 = new Task("Test1", "test1");
        Task task2 = new Task("Test2", "test2");

        task1.setId(0);
        task2.setId(0);

        assertEquals(task1, task2, "Разные задачи");
    }
}