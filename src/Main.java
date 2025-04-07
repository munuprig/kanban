import data.ProgressTask;
import models.Epic;
import models.SubTask;
import models.Task;
import workClass.Manager;

public class Main {
    static Manager manager = new Manager();

    public static void main(String[] args) {
        //Добавляем задачи
        System.out.println("Задача добавлена под id = " + manager.addNewTask(new Task("Доделать 4 спринт",
                "Переделать программу")));
        System.out.println("Задача добавлена под id = " + manager.addNewTask(new Task("Начать 5 спринт",
                "Завершить финальную задачу 5 спринта")));

        //Добавляем Epic 1
        System.out.println("Epic добавлен под id = " + manager.addNewEpic(new Epic("Epic 1")));
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("adad",
                        "dadad"), 2));
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("adadaad",
                "dad"), 2));

        //Добавляем Epic 2
        System.out.println("Epic добавлен под id = " + manager.addNewEpic(new Epic("Epic 2")));
        System.out.println("Подзадача добавлен под id = " + manager.addNewSubTask(new SubTask("ad",
                "nuljklj"), 5));

        //Печатаем
        print();

        //Печать подзадач эпиков
        System.out.println(manager.getEpicSubTasks(2));
        System.out.println(manager.getEpicSubTasks(5));

        //Изменяем
        manager.updateTask(new Task("Доделать 4 спринт", ProgressTask.IN_PROGRESS, 0,
                "Переделать программу"));

        manager.updateSubTask(new SubTask("adad", ProgressTask.IN_PROGRESS, 3,
                "dadad"));

        //Печатаем
        print();

        //Удаляем
        manager.deleteTask(1);
        manager.deleteEpic(5);

        //Печатаем
        print();

        //Удаляем всё
        manager.deleteTask();
        manager.deleteEpic();
        manager.deleteTask();
    }

    protected static void print(){
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
    }
}
