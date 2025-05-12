package models;

import data.ProgressTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public Epic(String name, String taskInfo) {
        super(name, taskInfo);
    }

    public Collection<SubTask> getSubTask() {
        return subTasks.values();
    }

    public void addSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic();
    }

    public void deleteSubTasks() {
        subTasks.clear();
        updateStatusEpic();
    }

    public void deleteSubTask(SubTask subTask) {
        subTasks.remove(subTask.getId());
        updateStatusEpic();
    }

    public void updateSubTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        updateStatusEpic();
    }

    private void updateStatusEpic() {
        int checkDone = 0;
        int checkInProgress = 0;

        if (subTasks.isEmpty()) {
            status = ProgressTask.NEW;
            return;
        }
        for (SubTask subTask : subTasks.values()) {
            if (ProgressTask.DONE.equals(subTask.getStatus())) {
                checkDone += 1;
            } else if (ProgressTask.IN_PROGRESS.equals(subTask.getStatus())) {
                checkInProgress += 1;
            }
        }
        if (checkDone == subTasks.size()) {
            status = ProgressTask.DONE;
            System.out.println("Статус Epic " + id + " обновлен.");
        } else if (checkInProgress > 0) {
            status = ProgressTask.IN_PROGRESS;
            System.out.println("Статус Epic " + id + " обновлен.");
        }

    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTasks=" + subTasks +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", taskInfo='" + info + '\'' +
                ", status=" + status +
                "}\n";
    }
}
