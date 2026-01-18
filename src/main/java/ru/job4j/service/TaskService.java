package ru.job4j.service;

import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Task create(Task task);
    Collection<Task> findAll();

    Task findById(Integer id);

    boolean update(Task task);

    boolean delete(Integer id);

    Collection<Task> getCompleted();

    Collection<Task> getNew();

    boolean markAsDone(Integer id);
}
