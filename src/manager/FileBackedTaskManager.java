package manager;

import data.ProgressTask;
import exceptions.ManagerSaveException;
import data.TypeTask;
import models.Epic;
import models.SubTask;
import models.Task;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    @Override
    public int addNewTask(Task task) {
        int id = super.addNewTask(task);
        save();
        return id;
    }

    @Override
    public int addNewEpic(Epic epic) {
        int id = super.addNewEpic(epic);
        save();
        return id;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        int id = super.addNewSubTask(subTask);
        save();
        return id;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
        save();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
        save();
    }

    @Override
    public void deleteSubTask() {
        super.deleteSubTask();
        save();
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            final String header = "id,type,name,status,description,epic\n";
            bw.write(header);

            for (Task task : getTasks()) {
                bw.write(toString(task, TypeTask.TASK));
            }

            for (Epic epic : getEpics()) {
                bw.write(toString(epic, TypeTask.EPIC));
            }

            for (SubTask subtask : getSubTasks()) {
                bw.write(toString(subtask, TypeTask.SUBTASK));
            }
        } catch (Exception ex) {
            throw new ManagerSaveException("Ошибка при сохранении в файл.", ex);
        }
    }

    private String toString(Task task, TypeTask typeTask) {
        StringBuilder sb = new StringBuilder();

        sb.append(task.getId())
                .append(',')
                .append(typeTask)
                .append(',')
                .append(task.getName())
                .append(',')
                .append(task.getStatus())
                .append(',')
                .append(task.getInfo());

        if (typeTask.equals(TypeTask.SUBTASK)) {
            sb.append(',').append(((SubTask) task).getIdEpic());
        }

        sb.append("\n");
        return sb.toString();
    }

    private static Task fromString(String str) {
        String[] stringTask = str.split(",");

        Integer id = Integer.valueOf(stringTask[0]);
        String name = stringTask[2];
        ProgressTask status = ProgressTask.valueOf(stringTask[3]);
        String info = stringTask[4];
        Integer idEpic = null;
        if (TypeTask.valueOf(stringTask[1]) == TypeTask.SUBTASK) {
            idEpic = Integer.valueOf(stringTask[5]);
        }

        Task currentTask = null;
        switch (TypeTask.valueOf(stringTask[1])) {
            case TASK -> currentTask = new Task(name, status, id, info);
            case EPIC -> currentTask = new Epic(name, status, id, info);
            case SUBTASK -> currentTask = new SubTask(name, status, id, info, idEpic);
        }
        return currentTask;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                Task task = fromString(line);

                if (task instanceof Epic) {
                    manager.epics.put(task.getId(), (Epic) task);
                } else if (task instanceof SubTask) {
                    manager.subTasks.put(task.getId(), (SubTask) task);
                } else if (task != null) {
                    manager.tasks.put(task.getId(), task);
                }
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка при загрузке файла.", ex); // Собственное исключение
        }
        return manager;
    }
}

