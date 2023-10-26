package ru.job4j.todo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.HibernateUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HibernateUserService implements UserService {

    private final HibernateUserRepository hibernateUserRepository;

    @Autowired
    public HibernateUserService(HibernateUserRepository hibernateUserRepository) {
        this.hibernateUserRepository = hibernateUserRepository;
    }

    @Override
    public User save(User user) {
        return hibernateUserRepository.save(user);
    }

    @Override
    public List<User> findAllUsers() {
        return hibernateUserRepository.findAllUsers();
    }

    @Override
    public Optional<User> findUserById(int id) {
        return hibernateUserRepository.findUserById(id);
    }

    @Override
    public Optional<User> findUserByLoginAndPassword(String login, String password) {
        return hibernateUserRepository.findUserByLoginAndPassword(login, password);
    }

    @Override
    public boolean updateUser(User user) {
        return hibernateUserRepository.updateUser(user);
    }

    @Override
    public boolean deleteUserById(int id) {
        return hibernateUserRepository.deleteUserById(id);
    }
}
