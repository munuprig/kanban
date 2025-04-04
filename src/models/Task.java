package models;

import data.ProgressTask;
import data.TypeTask;

import java.util.Objects;

public class Task {
    public Integer id;
    public String name;
    public String taskInfo;
    private ProgressTask status = ProgressTask.NEW;
    private final TypeTask type;
    //
    public Task (String name, int id, TypeTask type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    /// ///
    public Task(String name, int id, String text, TypeTask type) {
        this.id = id;
        this.name = name;
        this.taskInfo = text;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

    @Override
    public String toString() {
        return  "ID задачи = " + id + " {" +
                "name='" + name +
                ", status=" + status +
                "}\n";
    }

    public ProgressTask getStatus() {
        return status;
    }

    public void setStatus(ProgressTask status) {
        this.status = status;
    }

    public TypeTask getType() {
        return type;
    }
}
