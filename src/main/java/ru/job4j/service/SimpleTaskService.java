package ru.job4j.service;

import org.springframework.stereotype.Service;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

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
    public Optional<Task> findById(Integer id) {
        return taskRepository.findById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public void delete(Integer id) {
        taskRepository.delete(id);
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
