package ru.job4j.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.handlers.TaskNotFoundException;
import ru.job4j.model.Priority;
import ru.job4j.repository.PriorityRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {
    private final PriorityRepository priorityRepository;

    @Override
    public Collection<Priority> findAll() {
        return priorityRepository.findAll();
    }

    @Override
    public Priority findById(Integer priorityId) {
        Priority priority = priorityRepository.findById(priorityId);
        if (priority == null) {
            throw new TaskNotFoundException("Приоритет с id " + priorityId + " не найден");
        }
        return priority;
    }
}
