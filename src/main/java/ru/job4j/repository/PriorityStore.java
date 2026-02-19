package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Priority;

import java.util.Collection;
import java.util.Map;

@Repository
@AllArgsConstructor
public class PriorityStore implements PriorityRepository {
    private final CrudRepository crudRepository;

    @Override
    public Collection<Priority> findAll() {
        return crudRepository.query("FROM Priority", Priority.class);
    }

    @Override
    public Priority findById(Integer priorityId) {
        return crudRepository.query("FROM Priority WHERE id =: fId", Priority.class, Map.of("fId", priorityId));
    }
}
