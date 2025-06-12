package manager;

import exceptions.ManagerSaveException;
import data.TypeTask;
import interfaces.TaskManager;
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

    private void save(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epic\n");

            for (Task task : super.getTasks()) {
                bw.write(toString(task,TypeTask.TASK));
            }

            for (Epic epic : super.getEpics()) {
                bw.write(toString(epic, TypeTask.EPIC));
            }

            for (SubTask subtask : super.getSubTasks()) {
                bw.write(toString(subtask, TypeTask.SUBTASK));
            }
        } catch (Exception ex) {
            throw new ManagerSaveException("Ошибка при сохранении в файл.", ex);
        }
    }

    private String toString(Task task, TypeTask typeTask){
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

    private Task fromString(String str){
        String[] stringTask = str.split(",");

        String name = stringTask[2];
        String taskInfo = stringTask[4];

        Task currentTask = null;
        switch (TypeTask.valueOf(stringTask[1])){
            case TASK -> currentTask = new Task(name,taskInfo);
            case EPIC -> currentTask = new Epic(name,taskInfo);
            case SUBTASK -> currentTask = new SubTask( name, taskInfo, Integer.parseInt(stringTask[5]));
        }
        return currentTask;
    }

    public void loadFromFile(File file, TaskManager manager){

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            String line;

            while((line = br.readLine()) != null){
                Task task = fromString(line);

                if (task instanceof Epic){
                    manager.addNewEpic((Epic) task);
                } else if (task instanceof  SubTask) {
                    manager.addNewSubTask((SubTask) task);
                } else if (task != null) {
                    manager.addNewTask(task);
                }
            }
        } catch (IOException ex) {
            throw new ManagerSaveException("Ошибка при загрузке файла.", ex); // Собственное исключение
        }
    }
}

