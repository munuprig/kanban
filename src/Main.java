import interfaces.TaskManager;
import manager.FileBackedTaskManager;
import manager.Managers;
import models.Epic;
import models.SubTask;
import models.Task;

import java.io.File;
import java.io.IOException;

public class Main {
    static TaskManager inMemoryTaskManager = Managers.getDefault();

    public static void main(String[] args) {
        File file;

        try {
            file = File.createTempFile("Temp", ".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        FileBackedTaskManager fl = new FileBackedTaskManager(file);

        fl.addNewTask(new Task("Задача 1", "Текст"));
        int idFirstEpic = fl.addNewEpic(new Epic("Epic 1", "Текст"));
        System.out.println("Epic добавлен под id = " + idFirstEpic);

        fl.addNewSubTask(new SubTask("Подзадача 1", "Текст", idFirstEpic));
        fl.addNewSubTask(new SubTask("Подзадача 2", "Текст", idFirstEpic));

        fl.deleteEpic();
        fl.deleteTask();

        printAllTasks(fl);

        fl = FileBackedTaskManager.loadFromFile(file);

        printAllTasks(fl);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubTasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubTasks()) {
            System.out.println(subtask);
        }
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
