package ru.job4j.repository;

import ru.job4j.model.Category;
import java.util.Collection;
import java.util.List;

public interface CategoryRepository {
    Collection<Category> findAll();

    Collection<Category> findAllById(List<Integer> ids);
}
