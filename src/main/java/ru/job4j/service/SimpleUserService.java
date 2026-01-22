package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Service;
import ru.job4j.handlers.DuplicateLoginException;
import ru.job4j.model.User;
import ru.job4j.repository.UserRepository;

@Service
@AllArgsConstructor
public class SimpleUserService implements UserService {
    private UserRepository userRepository;

    @Override
    public User save(User user) {
        try {
            return userRepository.save(user);
        } catch (ConstraintViolationException e) {
            throw new DuplicateLoginException("Пользователь с логином '" + user.getLogin() + "' уже существует");
        }
    }

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
