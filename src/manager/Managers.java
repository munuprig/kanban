package manager;

import interfaces.HistoryManager;
import interfaces.TaskManager;

public abstract class Managers {

    private static final HistoryManager defaultHistory = new InMemoryHistoryManager();
    private static final TaskManager defaultTaskManager = new InMemoryTaskManager();

    public static HistoryManager getDefaultHistory(){
        return defaultHistory;
    }

    public static TaskManager getDefault(){
        return defaultTaskManager;
    }
}
