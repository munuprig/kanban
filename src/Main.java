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
//        //Добавляем задачи
//        int idFirstTask = inMemoryTaskManager.addNewTask(new Task("Задача 1", "Текст"));
//        int idSecondTask = inMemoryTaskManager.addNewTask(new Task("Задача 2", "Текст"));
//        System.out.println("Задача добавлена под id = " + idFirstTask);
//        System.out.println("Задача добавлена под id = " + idSecondTask);
//
//        //Добавляем Epic 1
//        int idFirstEpic = inMemoryTaskManager.addNewEpic(new Epic("Epic 1", "Текст"));
//        System.out.println("Epic добавлен под id = " + idFirstEpic);
//
//        int idFirstSubTask = inMemoryTaskManager.addNewSubTask(
//                new SubTask("Подзадача 1", "Текст", idFirstEpic));
//        int idSecondSubTask = inMemoryTaskManager.addNewSubTask(
//                new SubTask("Подзадача 2", "Текст", idFirstEpic));
//        System.out.println("Подзадача добавлен под id = " + idFirstSubTask);
//        System.out.println("Подзадача добавлен под id = " + idSecondSubTask);
//
//        //Добавляем Epic 2
//        int idSecondEpic = inMemoryTaskManager.addNewEpic(new Epic("Epic 2", "Tекст"));
//        System.out.println("Epic добавлен под id = " + idSecondEpic);
//
//
//        //Печатаем
//        //print();
//        inMemoryTaskManager.getTask(idFirstTask);
//        inMemoryTaskManager.getEpic(idFirstEpic);
//        inMemoryTaskManager.getTask(idSecondTask);
//        inMemoryTaskManager.getSubTask(idSecondSubTask);
//        inMemoryTaskManager.getEpic(idSecondEpic);
//        inMemoryTaskManager.getTask(idFirstTask);
//
//
//        printAllTasks(inMemoryTaskManager);
//
//        inMemoryTaskManager.getSubTask(idSecondSubTask);
//        inMemoryTaskManager.getEpic(idSecondEpic);
//        inMemoryTaskManager.getTask(idFirstTask);
//        inMemoryTaskManager.getTask(idFirstTask);
//        inMemoryTaskManager.getEpic(idFirstEpic);
//        inMemoryTaskManager.getTask(idSecondTask);
//
//        printAllTasks(inMemoryTaskManager);
//
//        inMemoryTaskManager.deleteEpic(idFirstEpic);
//
//        printAllTasks(inMemoryTaskManager);
//
//        inMemoryTaskManager.deleteTask(idSecondTask);
//
//        printAllTasks(inMemoryTaskManager);


        File file;
        try {
             file = File.createTempFile("Temp",".csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        FileBackedTaskManager fl = new FileBackedTaskManager(file);

        fl.addNewTask(new Task("Задача 1", "Текст"));
        int idFirstEpic = fl.addNewEpic(new Epic("Epic 1", "Текст"));
        System.out.println("Epic добавлен под id = " + idFirstEpic);

        int idFirstSubTask = fl.addNewSubTask(
                new SubTask("Подзадача 1", "Текст", idFirstEpic));
        int idSecondSubTask = fl.addNewSubTask(
                new SubTask("Подзадача 2", "Текст", idFirstEpic));

        inMemoryTaskManager.deleteEpic();
        inMemoryTaskManager.deleteTask();

        fl.loadFromFile(file);

        printAllTasks(inMemoryTaskManager);
    }

    protected static void print() {
        System.out.println("Все задачи :\n" + inMemoryTaskManager.getTasks());
        System.out.println("Все Epic :\n" + inMemoryTaskManager.getEpics());
        System.out.println("Все подзадачи :\n" + inMemoryTaskManager.getSubTasks());
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
