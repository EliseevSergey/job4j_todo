package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.User;

@Service
public interface UserService {
    User save(User user);

    User findByLoginAndPassword(String login, String password);
}
