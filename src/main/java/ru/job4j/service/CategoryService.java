package ru.job4j.service;

import ru.job4j.model.Category;

import java.util.Collection;
import java.util.List;

public interface CategoryService {
    Collection<Category> findAll();
    List<Category> findAllById(List<Integer> ids);

}
