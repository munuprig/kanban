package models;

import data.ProgressTask;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic (String name, String taskInfo){
        super(name, taskInfo);
    }

    public ArrayList<SubTask> getSubTask() {
        return subTasks;
    }

    public void addSubTask(SubTask subTask){
        subTasks.add(subTask);
    }

    public void deleteSubTasks(){
        subTasks.clear();
    }

    public void deleteSubTask(SubTask subTask){
        subTasks.remove(subTask);
    }

    public boolean updateSubTask(SubTask subTask){
        for (SubTask oldSubTask: subTasks){
            if(oldSubTask.equals(subTask)){
                subTasks.remove(oldSubTask);
                subTasks.add(subTask);
                updateStatusEpic();
                return true;
            }
        }
        return false;
    }

    public void updateStatusEpic(){
        int checkDone = 0;
        int checkInProgress = 0;

        if (subTasks.isEmpty()){
            status = ProgressTask.NEW;
            return;
        }
        for (SubTask subTask: subTasks){
            if(ProgressTask.DONE.equals(subTask.getStatus())){
                checkDone+= 1;
            } else if (ProgressTask.IN_PROGRESS.equals(subTask.getStatus())) {
                checkInProgress+= 1;
            }
        }
        if (checkDone == subTasks.size()){
            status = ProgressTask.DONE;
            System.out.println("Статус Epic "+ id +" обновлен." );
        }else if (checkInProgress > 0){
            status = ProgressTask.IN_PROGRESS;
            System.out.println("Статус Epic "+ id +" обновлен." );
        }

    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", status=" + status +
                "}\n";
    }
}
