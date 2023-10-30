package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    public HibernateUserRepository(CrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<User> save(User user) {
        Optional<User> resultSaveUser = Optional.empty();
        try {
            crudRepository.run((Consumer<Session>) session -> session.persist(user));
            resultSaveUser = Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSaveUser;
    }

    @Override
    public List<User> findAllUsers() {
        return crudRepository.query("FROM User AS u ORDER BY u.id", User.class);
    }

    @Override
    public Optional<User> findUserById(int id) {
        return crudRepository.optional(
                "FROM User WHERE id = :fId", User.class,
                Map.of("fId", id));
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return crudRepository.optional(
                "from User WHERE login = :fLogin AND password = :fPassword", User.class,
                Map.of("fLogin", login,
                        "fPassword", password));
    }

    @Override
    public boolean updateUser(User user) {
        return crudRepository.run((Function<Session, Object>) session -> session.merge(user)) != null;
    }

    @Override
    public boolean deleteUserById(int id) {
        return crudRepository.execute(
                "DELETE FROM User WHERE id = :fId",
                Map.of("fId", id)) > 0;
    }
}
