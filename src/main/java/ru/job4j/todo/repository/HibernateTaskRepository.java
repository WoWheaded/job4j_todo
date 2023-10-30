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
                    "FROM Task AS t ORDER BY t.id", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return allTask;
    }

    @Override
    public List<Task> findTaskByStatus(boolean status) {
        List<Task> taskByStatus = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            taskByStatus = session.createQuery(
                            "FROM Task AS t WHERE done = :fDone ORDER BY t.id", Task.class)
                    .setParameter("fDone", status)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return taskByStatus;
    }

    @Override
    public boolean updateTask(Task task) {
        boolean resultUpdate = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
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
    public boolean updateTaskStatus(int id) {
        boolean resultUpdate = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE Task as t SET t.done = :fDone WHERE t.id = :fId")
                    .setParameter("fDone", true)
                    .setParameter("fId", id)
                    .executeUpdate();
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
