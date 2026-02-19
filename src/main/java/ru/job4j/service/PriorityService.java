package ru.job4j.service;

import ru.job4j.model.Priority;

import java.util.Collection;

public interface PriorityService {
    Collection<Priority> findAll();
    Priority findById(Integer id);
}
