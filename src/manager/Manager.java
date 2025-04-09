package manager;

import data.ProgressTask;
import models.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Manager {
    HashMap<Integer, Task> taskList = new HashMap<>();
    HashMap<Integer, Epic> epicList = new HashMap<>();
    HashMap<Integer, SubTask> subTasksList = new HashMap<>();
    private int id = 0;

    public Task getTask(int id){
        for (Task task: getTasks()){
            if(task.getId() == id){
                return task;
            }
        }
        return null;
    }

    public SubTask getSubTask(int id){
        for (SubTask subTask: getSubTasks()){
            if(subTask.getId() == id){
                return subTask;
            }
        }
        return null;
    }

    public Epic getEpic(int id){
        for (Epic epic: getEpics()){
            if(epic.getId() == id){
                return epic;
            }
        }
        return null;
    }

    public Collection<Task> getTasks(){
        return taskList.values();
    }

    public Collection<SubTask> getSubTasks(){
        return subTasksList.values();
    }

    public Collection<Epic> getEpics(){
        return epicList.values();
    }

    public List<SubTask> getEpicSubTasks(int epicId){
        System.out.println("Все подзадачи Epic id " + epicId + " :");
        return getEpic(epicId).getSubTask();
    }

    public int addNewTask(Task task){
        task.setId(id);
        taskList.put(id,task);
        return id++;
    }

    public int addNewEpic(Epic epic){
        epic.setId(id);
        epicList.put(id,epic);
        return id++;
    }

    public int addNewSubTask(SubTask subTask){
        subTask.setId(id);
        subTasksList.put(id, subTask);
        getEpic(subTask.getIdEpic()).addSubTask(subTask);
        return id++;
    }

    public void updateTask(Task task){
        if (taskList.containsKey(task.getId())){
            taskList.put(task.getId(), task);
            System.out.println("Задача под id " + task.getId() + " обновлена.");
        }
    }

    public void updateEpic(Epic epic){
        if (epicList.containsKey(epic.getId())){
            epicList.put(epic.getId(), epic);
            System.out.println("Epic под id " + epic.getId() + " обновлен.");
        }
    }

    public void updateSubTask(SubTask subTask){
        if (subTasksList.containsKey(subTask.getId())){
            for(Epic epic: getEpics()){
                if (epic.updateSubTask(subTask)){
                    System.out.println("Подзадача под id " + subTask.getId() + " обновлен.");
                    subTasksList.put(subTask.getId(), subTask);
                    return;
                }
            }
        }
    }

    public void deleteTask(int id){
        taskList.remove(id);
        System.out.println("Задача id " + id + " - удалена");
    }

    public void deleteEpic(int id){
        for (SubTask subTask: getEpic(id).getSubTask()){
            subTasksList.remove(subTask.getId());
        }
        epicList.remove(id);
        System.out.println("Epic id " + id + " - удален");
    }

    public void deleteSubtask(int id){
        SubTask subTask = subTasksList.get(id);
        Epic epic = getEpic(subTask.getId());
        epic.deleteSubTask(subTask);
        epic.updateStatusEpic();
        subTasksList.remove(id);
        System.out.println("Подзадача id " + id + " - удалена");
    }

    public void deleteTask(){
        taskList.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteEpic(){
        epicList.clear();
        System.out.println("Все Epic удалены");
        if (!subTasksList.isEmpty()) {deleteSubTask();}
    }

    public void deleteSubTask(){
        for (Epic epic : getEpics()){
            epic.deleteSubTasks();
            epic.updateStatusEpic();
        }
        subTasksList.clear();
        System.out.println("Все подзадачи удалены");
    }

}
