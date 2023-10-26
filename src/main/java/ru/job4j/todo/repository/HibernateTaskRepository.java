package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateTaskRepository implements TaskRepository {
    private final SessionFactory sf;

    public HibernateTaskRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public Task createTask(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    @Override
    public Optional<Task> findTaskById(int id) {
        Optional<Task> foundedTask = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            foundedTask = session.createQuery(
                            "FROM Task as t WHERE t.id = :fId", Task.class)
                    .setParameter("fId", id).uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return foundedTask;
    }

    @Override
    public List<Task> findAllTask() {
        List<Task> allTask = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            allTask = session.createQuery(
                    "FROM Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return allTask;
    }

    @Override
    public List<Task> findNewTask() {
        List<Task> allDoneTask = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            allDoneTask = session.createQuery(
                            "FROM Task WHERE done = false ", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return allDoneTask;
    }

    @Override
    public List<Task> findDoneTask() {
        List<Task> allDoneTask = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            allDoneTask = session.createQuery(
                    "FROM Task WHERE done = true", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return allDoneTask;
    }

    @Override
    public boolean updateTask(int id, Task task) {
        boolean resultUpdate = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Task SET title = :fTitle, description = :fDescription, done = :fDone "
                                    + "WHERE id = :fId")
                    .setParameter("fId", id)
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fDone", task.isDone()).executeUpdate();
            session.getTransaction().commit();
            resultUpdate = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return resultUpdate;
    }

    @Override
    public boolean deleteTaskById(int id) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE FROM Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }
}
