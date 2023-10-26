package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task createTask(Task task);

    Optional<Task> findTaskById(int id);

    List<Task> findAllTask();

    List<Task> findNewTask();

    List<Task> findDoneTask();

    boolean update(int id, Task task);

    boolean deleteTaskById(int id);
}
