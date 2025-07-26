package manager;

import interfaces.*;
import models.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    protected int id = 0;
    private final HistoryManager defaultHistory = Managers.getDefaultHistory();
    protected final Set<Task> allTasksSort = new TreeSet<>((t1, t2) -> {
        if (t1.getStartTime() == null || t2.getStartTime() == null) {
            throw new IllegalArgumentException("У задача не указано стартовое время");
        }
        return t1.getStartTime().compareTo(t2.getStartTime());
    });


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
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(allTasksSort);
    }

    @Override
    public Integer addNewTask(Task task) {
        if (checkForConflicts(task, getPrioritizedTasks())) {
            System.out.println("Задача конфликтует по времени!");
            return null;
        }
        if (task.getStartTime() != null) {
            allTasksSort.add(task);
        }
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
    public Integer addNewSubTask(SubTask subTask) {
        if (checkForConflicts(subTask, getPrioritizedTasks())) {
            System.out.println("Задача конфликтует по времени!");
            return null;
        }
        if (epics.containsKey(subTask.getIdEpic())) {
            if (subTask.getStartTime() != null) {
                allTasksSort.add(subTask);
            }
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
        allTasksSort.remove(tasks.get(task.getId()));
        if (tasks.containsKey(task.getId())) {
            if (checkForConflicts(task, getPrioritizedTasks())) {
                System.out.println("Задача конфликтует по времени!");
                return;
            }
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
        allTasksSort.remove(tasks.get(subTask.getId()));
        if (epics.containsKey(subTask.getIdEpic()) && subTasks.containsKey(subTask.getId())) {
            if (checkForConflicts(subTask, getPrioritizedTasks())) {
                System.out.println("Задача конфликтует по времени!");
                return;
            }
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
            epics.get(id).getSubTask().forEach(subTask -> {
                subTasks.remove(subTask.getId());
                defaultHistory.remove(subTask.getId());
                allTasksSort.remove(subTask);
            });
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
        getEpics().forEach(epic -> {
            epic.deleteSubTasks();
            epic.getSubTask().forEach(allTasksSort::remove);
        });
        deleteInHistory(subTasks.keySet());
        subTasks.clear();
        System.out.println("Все подзадачи удалены");
    }

    @Override
    public List<Task> getHistory() {
        return defaultHistory.getHistory();
    }

    private void deleteInHistory(Set<Integer> idSet) {
        idSet.forEach(defaultHistory::remove);
    }

    private boolean checkForConflicts(Task newTask, List<Task> prioritizedTasks) {
        return prioritizedTasks.stream()
                .anyMatch(existingTask ->
                        existingTask.getStartTime().isBefore(newTask.getEndTime()) &&
                                existingTask.getEndTime().isAfter(newTask.getStartTime())
                );
    }

}
