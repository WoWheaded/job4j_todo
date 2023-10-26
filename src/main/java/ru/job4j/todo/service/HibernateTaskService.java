package ru.job4j.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.HibernateTaskRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HibernateTaskService implements TaskService {

    private final HibernateTaskRepository hibernateTaskRepository;

    @Autowired
    public HibernateTaskService(HibernateTaskRepository hibernateTaskRepository) {
        this.hibernateTaskRepository = hibernateTaskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return hibernateTaskRepository.createTask(task);
    }

    @Override
    public Optional<Task> findTaskById(int id) {
        return hibernateTaskRepository.findTaskById(id);
    }

    @Override
    public List<Task> findAllTask() {
        return hibernateTaskRepository.findAllTask();
    }

    @Override
    public List<Task> findNewTask() {
        return hibernateTaskRepository.findNewTask();
    }

    @Override
    public List<Task> findDoneTask() {
        return hibernateTaskRepository.findDoneTask();
    }

    @Override
    public boolean update(int id, Task task) {
        return hibernateTaskRepository.update(id, task);
    }

    @Override
    public boolean deleteTaskById(int id) {
        return hibernateTaskRepository.deleteTaskById(id);
    }
}
