package models;

import data.ProgressTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final Integer idEpic;

    public SubTask(String name, String taskInfo, Integer idEpic) {
        super(name, taskInfo);
        this.idEpic = idEpic;
    }

    public SubTask(String name, String taskInfo, ProgressTask status, Integer idEpic) {
        super(name, taskInfo);
        this.setStatus(status);
        this.idEpic = idEpic;
    }

    public SubTask(String name, ProgressTask status, Integer id, String info,
                   LocalDateTime startTime, Duration duration, Integer idEpic) {
        super(name, status, id, info, startTime, duration);
        this.setStatus(status);
        this.idEpic = idEpic;
    }
    public SubTask(String name, String info, LocalDateTime startTime, Duration duration, Integer idEpic) {
        super(name, info, startTime, duration);
        this.idEpic = idEpic;
    }

    public SubTask(String name, ProgressTask status, Integer id, String info, Integer idEpic) {
        super(name, status, id, info);
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
