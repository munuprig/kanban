package manager;

import data.ProgressTask;
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
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
    }

    @Override
    public void deleteTask() {
        super.deleteTask();
    }

    @Override
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Override
    public void deleteSubTask() {
        super.deleteSubTask();
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epic\n");

            for (Task task : super.getTasks()) { // Перебор задач
                bw.write(toString(task,TypeTask.TASK));
            }

            for (Epic epic : super.getEpics()) { // Перебор эпиков
                bw.write(toString(epic, TypeTask.EPIC));
            }

            for (SubTask subtask : super.getSubTasks()) { // Перебор субзадач
                bw.write(toString(subtask, TypeTask.SUBTASK));
            }
        } catch (IOException ex) {
            //throw new ManagerSaveException("Ошибка при сохранении в файл.", ex); // Собственное исключение
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
                .append(task.getInfo()); // Допустим, что описания не содержат спецсимволов

        if (typeTask.equals(TypeTask.SUBTASK)) {
            sb.append(',').append(((SubTask) task).getIdEpic());
        }

         sb.append("\n");
        return sb.toString();
    }

    private Task fromString(String str){
        String[] stringTask = str.split(",");

        int id = Integer.parseInt(stringTask[0]);
        TypeTask type = TypeTask.valueOf(stringTask[1]);
        String name = stringTask[2];
        ProgressTask status = ProgressTask.valueOf(stringTask[3]);
        String taskInfo = stringTask[4];

        Task currentTask = null;
        switch (TypeTask.valueOf(stringTask[1])){
            case TASK -> currentTask = new Task(name, status, id ,taskInfo);
            case EPIC -> currentTask = new Epic(name, status, id ,taskInfo);
            case SUBTASK -> currentTask = new SubTask( name, status, id, taskInfo, Integer.parseInt(stringTask[5]));
        }
        return currentTask;
    }

    public void loadFromFile(File file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();

            String line;

            while((line = br.readLine()) != null){
                Task task = fromString(line);

                if (task instanceof Epic){
                    super.addNewEpic((Epic) task);
                } else if (task instanceof  SubTask) {
                    super.addNewSubTask((SubTask) task);
                } else if (task != null) {
                    super.addNewTask(task);
                }
            }
        } catch (IOException ex) {
            //throw new ManagerSaveException("Ошибка при сохранении в файл.", ex); // Собственное исключение
        }
    }
}
