package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.handlers.TaskNotFoundException;
import ru.job4j.handlers.TaskUpdateException;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.Collection;

@Service
public class SimpleTaskService implements TaskService {
    private final TaskRepository taskRepository;

    public SimpleTaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public Collection<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Integer id) {
        Task task = taskRepository.findById(id);
        if (task == null) {
            throw new TaskNotFoundException("Задача с id " + id + " не найдена");
        }
        return task;
    }

    @Override
    public boolean update(Task task) {
        boolean updated = taskRepository.update(task);
        if (!updated) {
            throw new TaskUpdateException("Обновление задачи с id " + task.getId() + " не выполнено");
        }
        return true;
    }

    @Override
    public boolean markAsDone(Integer id) {
        boolean updated = taskRepository.markAsDone(id);
        if (!updated) {
            throw new TaskUpdateException("Обновление статуса done задачи с id " + id + " не выполнено");
        }
        return true;
    }

    public boolean delete(Integer taskId) {
        boolean deleted = taskRepository.delete(taskId);
        if (!deleted) {
            throw new TaskNotFoundException("Задача с id " + taskId + " не найдена");
        }
        return true;
    }

    @Override
    public Collection<Task> getCompleted() {
        return taskRepository.getCompleted();
    }

    @Override
    public Collection<Task> getNew() {
        return taskRepository.getNew();
    }
}
