package models;

import data.ProgressTask;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String taskInfo;
    protected ProgressTask status = ProgressTask.NEW;

    public Task (String name, String taskInfo) {
        this.name = name;
        this.taskInfo = taskInfo;
    }

    public Task (String name, ProgressTask status, Integer id, String taskInfo) {
        this.name = name;
        this.status = status;
        this.id = id;
        this.taskInfo = taskInfo;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taskInfo='" + taskInfo + '\'' +
                ", status=" + status +
                '}';
    }

    public ProgressTask getStatus() {
        return status;
    }

    public void setStatus(ProgressTask status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
