package ru.job4j.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.handlers.UserNotFoundException;
import ru.job4j.model.User;

import javax.persistence.PersistenceException;


@Repository
@AllArgsConstructor
@Slf4j
public class UserStore implements UserRepository {
    private final SessionFactory sf;

    @Override
    public User save(User user) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(user);
                transaction.commit();
                return user;
            } catch (Exception e) {
                transaction.rollback();
                log.error("Failed to create user: {}", user, e);
                throw e;
            }
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        try (Session session = sf.openSession()) {
            User user = session.createQuery("FROM User WHERE login =: login AND password =: password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .uniqueResult();
            if (user == null) {
                throw new UserNotFoundException("Пользователь с логином '" + login + "' не найден или пароль неверен");
            }
            return user;
        } catch (PersistenceException e) {
            log.error("Failed to retrieve user with login={}", login, e);
            throw e;
        }
    }
}