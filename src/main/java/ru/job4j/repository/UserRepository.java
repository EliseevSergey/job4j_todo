package ru.job4j.repository;

import ru.job4j.model.User;

public interface UserRepository {
    User save(User user);

    User findByLoginAndPassword(String login, String password);

    boolean update(User user);

    boolean delete(User user);
}
