package manager;

import models.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class Manager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private int id = 0;

    public Task getTask(int id){
        return tasks.get(id);
    }

    public SubTask getSubTask(int id){
        return subTasks.get(id);
    }

    public Epic getEpic(int id){
        return epics.get(id);
    }

    public Collection<Task> getTasks(){
        return tasks.values();
    }

    public Collection<SubTask> getSubTasks(){
        return subTasks.values();
    }

    public Collection<Epic> getEpics(){
        return epics.values();
    }

    public Collection<SubTask> getEpicSubTasks(int epicId){
        if (epics.containsKey(epicId)){
            System.out.println("Все подзадачи Epic id " + epicId + " :");
            return getEpic(epicId).getSubTask();
        }else{
            return null;
        }
    }

    public int addNewTask(Task task){
        task.setId(id);
        tasks.put(id,task);
        return id++;
    }

    public int addNewEpic(Epic epic){
        epic.setId(id);
        epics.put(id,epic);
        return id++;
    }

    public int addNewSubTask(SubTask subTask){
        if (epics.containsKey(subTask.getIdEpic())){
            subTask.setId(id);
            subTasks.put(id, subTask);
            getEpic(subTask.getIdEpic()).addSubTask(subTask);
            return id++;
        }else{
            return 0;
        }
    }

    public void updateTask(Task task){
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(), task);
            System.out.println("Задача под id " + task.getId() + " обновлена.");
        }
    }

    public void updateEpic(Epic epic){
        if (epics.containsKey(epic.getId())){
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setInfo(epic.getInfo());
            System.out.println("Epic под id " + epic.getId() + " обновлен.");
        }
    }

    public void updateSubTask(SubTask subTask){
        if (epics.containsKey(subTask.getIdEpic()) && subTasks.containsKey(subTask.getId())){
            Epic epic = epics.get(subTask.getIdEpic());
            epic.updateSubTask(subTask);
            System.out.println("Подзадача под id " + subTask.getId() + " обновлен.");
            subTasks.put(subTask.getId(), subTask);
        }
    }

    public void deleteTask(int id){
        if (tasks.containsKey(id)){
            tasks.remove(id);
            System.out.println("Задача id " + id + " - удалена");
        }
    }

    public void deleteEpic(int id){
        if (epics.containsKey(id)){
            for (SubTask subTask: getEpic(id).getSubTask()){
                subTasks.remove(subTask.getId());
            }
            epics.remove(id);
            System.out.println("Epic id " + id + " - удален");
        }
    }

    public void deleteSubtask(int id){
        if (epics.containsKey(subTasks.get(id).getIdEpic()) && subTasks.containsKey(id)){
            SubTask subTask = subTasks.get(id);
            getEpic(subTask.getId()).deleteSubTask(subTask);
            subTasks.remove(id);
            System.out.println("Подзадача id " + id + " - удалена");
        }
    }

    public void deleteTask(){
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteEpic(){
        epics.clear();
        subTasks.clear();
        System.out.println("Все Epic удалены");
    }

    public void deleteSubTask(){
        for (Epic epic : getEpics()){
            epic.deleteSubTasks();
        }
        subTasks.clear();
        System.out.println("Все подзадачи удалены");
    }

}
