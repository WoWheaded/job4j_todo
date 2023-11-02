package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class HibernateTaskRepository implements TaskRepository {
    private final CrudRepository crudRepository;

    public HibernateTaskRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Task createTask(Task task) {
        crudRepository.run((Consumer<Session>) session -> session.persist(task));
        return task;
    }

    @Override
    public Optional<Task> findTaskById(int id) {
        return crudRepository.optional(
                "FROM Task AS t JOIN FETCH t.priority WHERE t.id = :fId", Task.class,
                Map.of("fId", id));
    }

    @Override
    public List<Task> findAllTask() {
        return crudRepository.query("FROM Task AS t JOIN FETCH t.priority ORDER BY t.id", Task.class);
    }

    @Override
    public List<Task> findTaskByStatus(boolean status) {
        return crudRepository.query(
                "FROM Task AS t JOIN FETCH t.priority WHERE done = :fDone ORDER BY t.id", Task.class,
                Map.of("fDone", status));
    }

    @Override
    public boolean updateTask(Task task) {
        return crudRepository.run((Function<Session, Object>) session -> session.merge(task)) != null;
    }

    @Override
    public boolean updateTaskStatus(int id) {
        return crudRepository.execute(
                "UPDATE Task as t SET t.done = :fDone WHERE t.id = :fId",
                Map.of("fDone", true,
                        "fId", id)) > 0;
    }

    @Override
    public boolean deleteTaskById(int id) {
        return crudRepository.execute(
                "DELETE FROM Task WHERE id = :fId",
                Map.of("fId", id)) > 0;
    }
}
