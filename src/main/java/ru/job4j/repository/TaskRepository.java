package ru.job4j.repository;

import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Task create(Task task);
    Collection<Task> findAll();
    Optional<Task> findById(Integer id);

    boolean update(Task task);

    void delete(Integer taskId);

    Collection<Task> getCompleted();

    Collection<Task> getNew();
}
