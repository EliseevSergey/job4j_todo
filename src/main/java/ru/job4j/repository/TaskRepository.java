package ru.job4j.repository;

import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {
    Task create(Task task);
    Collection<Task> findAll();
    Task findById(Integer id);

    boolean update(Task task);

    boolean markAsDone(Integer id);

    boolean delete(Integer taskId);

    Collection<Task> getCompleted();

    Collection<Task> getNew();
}
