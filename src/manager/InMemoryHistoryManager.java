package manager;

import interfaces.HistoryManager;
import models.Node;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class InMemoryHistoryManager implements HistoryManager {
    private final HistoryLinkedList history = new HistoryLinkedList();

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void addHistory(Task task) {
        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    static class HistoryLinkedList {
        private final HashMap<Integer, Node> nodeHashMap = new HashMap<>();
        private Node head;
        private Node tail;

        public void add(Task task) {
            remove(task.getId());
            Node node = new Node(task);
            linkLast(node);
            nodeHashMap.put(task.getId(), node);
        }

        private void removeFromList(Node node) {
            if (node.prevNode != null) {
                node.prevNode.nextNode = node.nextNode;
            }
            if (node.nextNode != null) {
                node.nextNode.prevNode = node.prevNode;
            }
            if (node == head) {
                head = node.nextNode;
            }
            if (node == tail) {
                tail = node.prevNode;
            }
        }

        private void linkLast(Node node) {
            if (tail == null) {
                head = node;
            } else {
                tail.nextNode = node;
                node.prevNode = tail;
            }
            tail = node;
        }

        public List<Task> getTasks() {
            Node current = head;
            return new ArrayList<>(Stream.iterate(current, Objects::nonNull, node -> node.nextNode)
                    .map(node -> node.task)
                    .toList());
        }

        public void remove(int taskId) {
            Node node = nodeHashMap.get(taskId);
            if (node != null) {
                removeFromList(node);
                nodeHashMap.remove(taskId);
            }
        }

    }
}


