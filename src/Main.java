import data.ProgressTask;
import models.Epic;
import models.SubTask;
import models.Task;
import manager.Manager;

public class Main {
    static Manager manager = new Manager();

    public static void main(String[] args) {
        //Добавляем задачи
        int idTask = manager.addNewTask(new Task("Доделать 4 спринт", "Переделать программу"));
        System.out.println("Задача добавлена под id = " + idTask);
        System.out.println("Задача добавлена под id = " + manager.addNewTask(new Task("Начать 5 спринт",
                "Завершить финальную задачу 5 спринта")));

        //Добавляем Epic 1
        int idFirstEpic = manager.addNewEpic(new Epic("Epic 1", "ыыаы"));
        System.out.println("Epic добавлен под id = " + idFirstEpic);
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("adad",
                "dadad", idFirstEpic)));
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("adadaad",
                "dad", idFirstEpic)));

        //Добавляем Epic 2
        int idSecondEpic = manager.addNewEpic(new Epic("Epic 2", "фвфв"));
        System.out.println("Epic добавлен под id = " + idSecondEpic);
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("ad",
                "nuljklj", idSecondEpic)));

        //Печатаем
        print();

        //Печать подзадач эпиков
        System.out.println(manager.getEpicSubTasks(idFirstEpic));
        System.out.println(manager.getEpicSubTasks(idSecondEpic));

        //Изменяем
        manager.updateTask(new Task("Доделать 4 спринт", ProgressTask.IN_PROGRESS, idTask,
                "Переделать программу"));

        manager.updateSubTask(new SubTask("adad", ProgressTask.IN_PROGRESS, 3,
                "dadad"));

        //Печатаем
        print();

        //Удаляем
        manager.deleteTask(idTask);
        manager.deleteEpic(idSecondEpic);

        //Печатаем
        print();

        //Удаляем всё
        manager.deleteTask();
        manager.deleteEpic();

        //Печатаем
        print();
    }

    protected static void print(){
        System.out.println("Все задачи :\n" + manager.getTasks());
        System.out.println("Все Epic :\n" + manager.getEpics());
        System.out.println("Все подзадачи :\n" + manager.getSubTasks());
    }
}
