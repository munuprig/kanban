package workClass;

import models.Epic;
import models.SubTask;
import models.Task;

import java.util.ArrayList;

public class Manager {
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Epic> epicList = new ArrayList<>();
    ArrayList<SubTask> subTasksList = new ArrayList<>();

    public Task getTask(int id){
        return  taskList.get(id);
    }

    public SubTask getSubtask(int id){
        return  subTasksList.get(id);
    }

    public Epic getEpic(int id){
        return  epicList.get(id);
    }

}
