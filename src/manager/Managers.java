package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;

public abstract class Managers {
    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }
}
