package models;

import data.ProgressTask;

public class SubTask extends Task{

    public SubTask (String name, String taskInfo){
        super(name, taskInfo);
    }

    public SubTask (String name, ProgressTask status, Integer id, String taskInfo){
        super(name, status, id, taskInfo);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", status=" + status +
                '}';
    }
}
