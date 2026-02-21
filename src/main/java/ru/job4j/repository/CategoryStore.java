package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryStore implements CategoryRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> findAll() {
        return crudRepository.query("FROM Category ORDER BY name", Category.class);
    }

    @Override
    public Collection<Category> findAllById(List<Integer> ids) {
        return crudRepository.queryListWithParam("FROM Category WHERE id IN :ids ORDER BY name",
                Category.class,
                Map.of("ids", ids));
    }
}
