package models;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

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

    @Test
    void getSetTimeTest(){
        Duration durationExpected = Duration.ofMinutes(30);
        LocalDateTime localDateTimeExpected = LocalDateTime.of
                (2024, 10, 1, 9, 30);
        final Task task = new Task("3", "3", LocalDateTime.of
                (2024, 10, 1, 9, 30),
                Duration.ofMinutes(30));

        assertEquals(task.getDuration(), durationExpected,
                "Время выполнения задачи не правильно.");
        assertEquals(task.getStartTime(), localDateTimeExpected,
                "Время начала выполнения задачи не правильно.");
        assertEquals(task.getEndTime(), localDateTimeExpected.plus(durationExpected),
                "Время конца выполнения задачи расчитывается не правильно.");
    }
}