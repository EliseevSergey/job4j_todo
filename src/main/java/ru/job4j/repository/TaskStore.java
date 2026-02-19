package ru.job4j.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;

import java.util.Collection;
import java.util.Map;

@Repository
@Primary
@AllArgsConstructor
@Slf4j
public class TaskStore implements TaskRepository {
    private final CrudRepository crudRepository;

    @Override
    public Task create(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    @Override
    public Collection<Task> findAll() {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority ORDER BY t.id ASC", Task.class);
    }

    @Override
    public Task findById(Integer taskId) {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority WHERE t.id = :fId", Task.class, Map.of("fId", taskId));
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.updateAndReturnBoolean("UPDATE Task SET description =: description,"
                        + " done =:done , created =:created WHERE id =:id",
                Map.of("description", task.getDescription(),
                        "done", task.isDone(),
                        "created", task.getCreated(),
                        "id", task.getId()));
    }

    @Override
    public boolean markAsDone(Integer taskId) {
        return crudRepository.updateAndReturnBoolean("UPDATE Task SET done = true WHERE id =:fid",
                Map.of("fid", taskId));
    }

    @Override
    public boolean delete(Integer taskId) {
        return crudRepository.updateAndReturnBoolean("DELETE Task WHERE id =:fid",
                Map.of("fid", taskId));
    }

    @Override
    public Collection<Task> getCompleted() {
        return crudRepository.query("FROM Task WHERE done = true ORDER BY id ASC",
                Task.class);
    }

    @Override
    public Collection<Task> getNew() {
        return crudRepository.query("FROM Task WHERE done = false ORDER BY id ASC",
                Task.class);
    }
}
