package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    List<User> findAllUsers();

    Optional<User> findUserById(int id);

    Optional<User> findUserByLoginAndPassword(String login, String password);

    boolean updateUser(User user);

    boolean deleteUserById(int id);
}