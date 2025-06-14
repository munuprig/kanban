package manager;

import interfaces.*;
import models.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int id = 0;
    private final HistoryManager defaultHistory = Managers.getDefaultHistory();

    @Override
    public Task getTask(int id) {
        defaultHistory.addHistory(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getSubTask(int id) {
        defaultHistory.addHistory(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        defaultHistory.addHistory(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    @Override
    public Collection<SubTask> getSubTasks() {
        return subTasks.values();
    }

    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    @Override
    public Collection<SubTask> getEpicSubTasks(int epicId) {
        if (epics.containsKey(epicId)) {
            System.out.println("Все подзадачи Epic id " + epicId + " :");
            return getEpic(epicId).getSubTask();
        } else {
            return null;
        }
    }

    @Override
    public int addNewTask(Task task) {
        task.setId(id);
        tasks.put(id, task);
        return id++;
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(id);
        epics.put(id, epic);
        return id++;
    }

    @Override
    public int addNewSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getIdEpic())) {
            subTask.setId(id);
            subTasks.put(id, subTask);
            getEpic(subTask.getIdEpic()).addSubTask(subTask);
            return id++;
        } else {
            return 0;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
            System.out.println("Задача под id " + task.getId() + " обновлена.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.get(epic.getId()).setName(epic.getName());
            epics.get(epic.getId()).setInfo(epic.getInfo());
            System.out.println("Epic под id " + epic.getId() + " обновлен.");
        }
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (epics.containsKey(subTask.getIdEpic()) && subTasks.containsKey(subTask.getId())) {
            Epic epic = epics.get(subTask.getIdEpic());
            epic.updateSubTask(subTask);
            System.out.println("Подзадача под id " + subTask.getId() + " обновлен.");
            subTasks.put(subTask.getId(), subTask);
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            defaultHistory.remove(id);
            System.out.println("Задача id " + id + " - удалена");
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            for (SubTask subTask : getEpic(id).getSubTask()) {
                subTasks.remove(subTask.getId());
                defaultHistory.remove(subTask.getId());
            }
            epics.remove(id);
            defaultHistory.remove(id);
            System.out.println("Epic id " + id + " - удален");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (epics.containsKey(subTasks.get(id).getIdEpic()) && subTasks.containsKey(id)) {
            SubTask subTask = subTasks.get(id);
            getEpic(subTask.getId()).deleteSubTask(subTask);
            subTasks.remove(id);
            defaultHistory.remove(id);
            System.out.println("Подзадача id " + id + " - удалена");
        }
    }

    @Override
    public void deleteTask() {
        deleteInHistory(tasks.keySet());
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    @Override
    public void deleteEpic() {
        deleteInHistory(epics.keySet());
        deleteInHistory(subTasks.keySet());
        epics.clear();
        subTasks.clear();
        System.out.println("Все Epic удалены");
    }

    @Override
    public void deleteSubTask() {
        for (Epic epic : getEpics()) {
            epic.deleteSubTasks();
        }
        deleteInHistory(subTasks.keySet());
        subTasks.clear();
        System.out.println("Все подзадачи удалены");
    }

    @Override
    public List<Task> getHistory() {
        return defaultHistory.getHistory();
    }

    private void deleteInHistory(Set<Integer> idSet) {
        for (Integer id : idSet) {
            defaultHistory.remove(id);
        }
    }
}
