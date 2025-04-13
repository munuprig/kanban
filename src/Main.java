import data.ProgressTask;
import interfaces.HistoryManager;
import interfaces.TaskManager;
import manager.Managers;
import models.Epic;
import models.SubTask;
import models.Task;

public class Main {
    static TaskManager inMemoryTaskManager = Managers.getDefault();

    public static void main(String[] args) {
        //Добавляем задачи
        int idTask = inMemoryTaskManager.addNewTask(new Task("Доделать 4 спринт", "Переделать программу"));
        System.out.println("Задача добавлена под id = " + idTask);
        System.out.println("Задача добавлена под id = " + inMemoryTaskManager.addNewTask(new Task("Начать 5 спринт",
                "Завершить финальную задачу 5 спринта")));

        //Добавляем Epic 1
        int idFirstEpic = inMemoryTaskManager.addNewEpic(new Epic("Epic 1", "ыыаы"));
        System.out.println("Epic добавлен под id = " + idFirstEpic);
        System.out.println("Подзадача добавлен под id = " + inMemoryTaskManager.addNewSubTask(new SubTask("adad",
                "dadad", idFirstEpic)));
        System.out.println("Подзадача добавлен под id = " + inMemoryTaskManager.addNewSubTask(new SubTask("adadaad",
                "dad", idFirstEpic)));

        //Добавляем Epic 2
        int idSecondEpic = inMemoryTaskManager.addNewEpic(new Epic("Epic 2", "фвфв"));
        System.out.println("Epic добавлен под id = " + idSecondEpic);
        System.out.println("Подзадача добавлен под id = " + inMemoryTaskManager.addNewSubTask(new SubTask("ad",
                "nuljklj", idSecondEpic)));

        //Печатаем
        print();

        //Печать подзадач эпиков
        System.out.println(inMemoryTaskManager.getEpicSubTasks(idFirstEpic));
        System.out.println(inMemoryTaskManager.getEpicSubTasks(idSecondEpic));

        //Изменяем
        inMemoryTaskManager.updateTask(new Task("Доделать 4 спринт", ProgressTask.IN_PROGRESS, idTask,
                "Переделать программу"));

        inMemoryTaskManager.updateSubTask(new SubTask("adad", ProgressTask.IN_PROGRESS, 3,
                "dadad", idFirstEpic));

        //Печатаем
        print();

        printAllTasks(inMemoryTaskManager);

        //Удаляем
        inMemoryTaskManager.deleteTask(idTask);
        inMemoryTaskManager.deleteEpic(idSecondEpic);

        //Печатаем
        print();

        //Удаляем всё
        inMemoryTaskManager.deleteTask();
        inMemoryTaskManager.deleteEpic();

        //Печатаем
        print();
    }

    protected static void print(){
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
