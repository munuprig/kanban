package models;

public class Node {
    public Task task;
    public Node nextNode;
    public Node prevNode;

    public Node(Task task) {
        this.task = task;
    }
}
