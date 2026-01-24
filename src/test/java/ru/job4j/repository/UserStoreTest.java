package ru.job4j.repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;
import ru.job4j.repository.UserStore;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserStoreTest {
    private static SessionFactory sf;

    @BeforeAll
    static void init() {
        sf = new Configuration()
                .configure("hibernate.cfg.xml") // из test/resources
                .buildSessionFactory();
    }

    @AfterAll
    static void close() {
        sf.close();
    }

    @Test
    void whenDuplicationLoginThenConstrainViolationException() {
        UserRepository userRepository = new UserStore(sf);
        User first = new User();
        first.setLogin("duplicatedLogin");
        first.setName("First");
        first.setPassword("password");

        User second = new User();
        second.setLogin("duplicatedLogin");
        second.setName("Second");
        second.setPassword("password");

        userRepository.save(first);

        ConstraintViolationException e = assertThrows(
                ConstraintViolationException.class,
                () -> userRepository.save(second)
        );

        assertThat(e.getSQLException().getErrorCode()).isEqualTo(23505);
    }
}
