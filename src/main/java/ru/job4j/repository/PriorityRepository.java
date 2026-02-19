package ru.job4j.repository;

import ru.job4j.model.Priority;

import java.util.Collection;

public interface PriorityRepository {
    Collection<Priority> findAll();
    Priority findById(Integer id);

}
