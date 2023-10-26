package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class HibernateUserRepository implements UserRepository {

    private final SessionFactory sf;

    public HibernateUserRepository(SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public User save(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> foundedUsers = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            foundedUsers = session.createQuery(
                    "FROM User", User.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return foundedUsers;
    }

    @Override
    public Optional<User> findUserById(int id) {
        Optional<User> foundedUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            foundedUser = session.createQuery(
                            "FROM User u WHERE u.id = :fId", User.class)
                    .setParameter("fId", id).uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return foundedUser;
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        Session session = sf.openSession();
        Optional<User> foundedUser = Optional.empty();
        try {
            session.beginTransaction();
            foundedUser = session.createQuery("from User WHERE login = :fLogin AND password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return foundedUser;
    }

    @Override
    public boolean updateUser(User user) {
        boolean resultUpdateUser = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET name = :fName, login = :fLogin, password = :fPassword")
                    .setParameter("fName", user.getName())
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("fPassword", user.getPassword()).executeUpdate();
            session.getTransaction().commit();
            resultUpdateUser = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return resultUpdateUser;
    }

    @Override
    public boolean deleteUserById(int id) {
        boolean resultDeleteUser = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE FROM User WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            resultDeleteUser = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return resultDeleteUser;
    }
}
