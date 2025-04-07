package workClass;

import data.ProgressTask;
import models.Epic;
import models.SubTask;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public class Manager {
    ArrayList<Task> taskList = new ArrayList<>();
    ArrayList<Epic> epicList = new ArrayList<>();
    ArrayList<SubTask> subTasksList = new ArrayList<>();
    private int id = 0;

    public Task getTask(int id){
        for (Task task: taskList){
            if(task.id == id){
                return task;
            }
        }
        return null;
    }

    public SubTask getSubTask(int id){
        for (SubTask subTask: subTasksList){
            if(subTask.id == id){
                return subTask;
            }
        }
        return null;
    }

    public Epic getEpic(int id){
        for (Epic epic: epicList){
            if(epic.id == id){
                return epic;
            }
        }
        return null;
    }

    public List<Task> getTasks(){
        System.out.println("Все задачи :");
        return taskList;
    }

    public List<SubTask> getSubTasks(){
        System.out.println("Все подзадачи :");
        return subTasksList;
    }

    public List<Epic> getEpics(){
        System.out.println("Все Epic :");
        return epicList;
    }

    public List<SubTask> getEpicSubTasks(int epicId){
        System.out.println("Все подзадачи Epic id " + epicId + " :");
        return getEpic(epicId).getSubTask();
    }

    public int addNewTask(Task task){
        task.id = id;
        taskList.add(task);
        return id++;
    }

    public int addNewEpic(Epic epic){
        epic.id = id;
        epicList.add(epic);
        return id++;
    }

    public int addNewSubTask(SubTask subTask, Integer idEpic){
        subTask.id = id;
        getEpic(idEpic).getSubTask().add(subTask);
        return id++;
    }

    public void updateTask(Task newTask){
        Task task = getTask(newTask.id);
        taskList.remove(task);
        taskList.add(newTask);
        System.out.println("Задача под id " + newTask.id + " обновлена.");
    }

    public void updateEpic(Epic newEpic){
        Epic epic = getEpic(newEpic.id);
        taskList.remove(epic);
        taskList.add(newEpic);
        System.out.println("Epic под id " + newEpic.id + " обновлен.");
    }

    public void updateSubTask(SubTask newSubTask){
        for(Epic epic: getEpics()){
            for(SubTask subTask: epic.getSubTask()){
                if (subTask.equals(newSubTask)){
                    epic.getSubTask().remove(subTask);
                    subTasksList.remove(subTask);
                    epic.getSubTask().add(newSubTask);
                    subTasksList.add(newSubTask);
                    System.out.println("Подзадача под id " + newSubTask.id + " обновлена.");
                    if(subTask.getStatus() != newSubTask.getStatus()){
                        updateStatusEpic(epic);
                    }
                    break;
                }
            }
        }
    }

    public void deleteTask(int id){
        taskList.remove(getTask(id));
        System.out.println("Задача id " + id + " - удалена");
    }

    public void deleteEpic(int id){
        epicList.remove(getEpic(id));
        System.out.println("Epic id " + id + " - удален");
    }


    public void deleteTask(){
        taskList.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteEpic(){
        epicList.clear();
        System.out.println("Все Epic удалены");
    }

    public void deleteSubTask(){
        subTasksList.clear();
        System.out.println("Все подзадачи удалены");
    }

    private void updateStatusEpic(Epic epic){
        int check = 0;

        for (SubTask subTask: epic.getSubTask()){
            if(ProgressTask.DONE.equals(subTask.getStatus())){
                check+= 2;
            } else if (ProgressTask.IN_PROGRESS.equals(subTask.getStatus())) {
                check+= 1;
            }
        }
        if (check == (epic.getSubTask().size()*2)){
            epic.setStatus(ProgressTask.DONE);
            System.out.println("Статус Epic "+ epic.id +" обновлен." );
        }else if (check != 0){
            epic.setStatus(ProgressTask.IN_PROGRESS);
            System.out.println("Статус Epic "+ epic.id +" обновлен." );
        }else{
            epic.setStatus(ProgressTask.NEW);
        }

    }
}
