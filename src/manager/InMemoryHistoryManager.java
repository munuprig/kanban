package manager;

import interfaces.HistoryManager;
import models.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>(10);

    @Override
    public List<Task> getHistory() {
        return history;
    }

    @Override
    public void addHistory(Task task) {
        if(task != null){
            if (history.size() == 10){
                history.removeFirst();
            }
            history.addLast(task);
        }
    }
}
