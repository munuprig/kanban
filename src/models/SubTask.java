package models;

import data.ProgressTask;

public class SubTask extends Task {
    private final Integer idEpic;

    public SubTask(String name, String taskInfo, Integer idEpic) {
        super(name, taskInfo);
        this.idEpic = idEpic;
    }

    public SubTask(String name, ProgressTask status, Integer id, String taskInfo, Integer idEpic) {
        super(name, status, id, taskInfo);
        this.idEpic = idEpic;
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    @Override
    public String toString() {
        return "SUBTASK{" +
                "idEpic=" + idEpic +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                '}';
    }
}
