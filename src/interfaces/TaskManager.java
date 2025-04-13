package interfaces;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.Collection;

public interface TaskManager {
    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    Collection<Task> getTasks();

    Collection<SubTask> getSubTasks();

    Collection<Epic> getEpics();

    Collection<SubTask> getEpicSubTasks(int epicId);

    int addNewTask(Task task);

    int addNewEpic(Epic epic);

    int addNewSubTask(SubTask subTask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    void deleteTask(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void deleteTask();

    void deleteEpic();

    void deleteSubTask();
}
