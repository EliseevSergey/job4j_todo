package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;
import java.util.Collection;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore implements TaskRepository {
    private final SessionFactory sf;

    @Override
    public Task create(Task task) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.save(task);
                transaction.commit();
                return task;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to save task", e);
            }
        }
    }

    @Override
    public Collection<Task> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task ORDER by id", Task.class).list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve tasks from data base", e);
        }
    }

    @Override
    public Optional<Task> findById(Integer taskId) {
        try (Session session = sf.openSession()) {
            return Optional.ofNullable(session.get(Task.class, taskId));
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }

    @Override
    public boolean update(Task task) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int updatedRows = session.createQuery("UPDATE Task SET description = : descr, done =: status WHERE id = : id ")
                        .setParameter("descr", task.getDescription())
                        .setParameter("id", task.getId())
                        .setParameter("status", task.isDone())
                        .executeUpdate();
                transaction.commit();
                return updatedRows > 0;
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("failed to update", e);
            }
        }
    }

    @Override
    public void delete(Integer taskId) {
        try (Session session = sf.openSession()) {
            var transaction = session.beginTransaction();
            try {
                session.createQuery("DELETE FROM Task WHERE id = :id")
                        .setParameter("id", taskId)
                        .executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw new RuntimeException("Failed to delete task", e);
            }
        }
    }

    @Override
    public Collection<Task> getCompleted() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task WHERE done =:done ORDER by id", Task.class)
                            .setParameter("done", true)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve completed tasks from data base", e);
        }
    }

    @Override
    public Collection<Task> getNew() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task WHERE done =:done ORDER by id", Task.class)
                    .setParameter("done", false)
                    .list();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve new tasks from data base", e);
        }
    }
}
