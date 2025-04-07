package models;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task{
    private ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic (String name){
        super(name);
    }

    public ArrayList<SubTask> getSubTask() {
        return subTasks;
    }

    public void setSubTask(ArrayList<SubTask> subTask) {
        this.subTasks = subTask;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }
}
