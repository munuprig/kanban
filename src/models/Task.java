package models;

import data.ProgressTask;

import java.util.Objects;

public class Task {
    protected Integer id;
    protected String name;
    protected String info;
    protected ProgressTask status = ProgressTask.NEW;

    public Task(String name, String info) {
        this.name = name;
        this.info = info;
    }

    public Task(String name, ProgressTask status, Integer id, String info) {
        this.name = name;
        this.status = status;
        this.id = id;
        this.info = info;
    }

    public ProgressTask getStatus() {
        return status;
    }

    public void setStatus(ProgressTask status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    @Override
    public String toString() {
        return "TASK{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", taskInfo='" + info + '\'' +
                ", status=" + status +
                '}';
    }
}
