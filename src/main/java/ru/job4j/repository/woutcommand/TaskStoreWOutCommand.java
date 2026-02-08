package ru.job4j.repository.woutcommand;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Task;
import ru.job4j.repository.TaskRepository;
import java.util.Collection;

@Repository
@AllArgsConstructor
@Slf4j
public class TaskStoreWOutCommand implements TaskRepository {
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
                log.error("Failed to save task: {}", task, e);
                throw new RuntimeException("Failed to save task", e);
            }
        }
    }

    @Override
    public Collection<Task> findAll() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task ORDER by id", Task.class).list();
        } catch (Exception e) {
            log.error("Failed to retrieve all tasks", e);
            throw new RuntimeException("Failed to retrieve tasks from data base", e);
        }
    }

    @Override
    public Task findById(Integer taskId) {
        try (Session session = sf.openSession()) {
            return session.get(Task.class, taskId);
        } catch (Exception e) {
            log.error("Failed to retrieve task with id={}", taskId, e);
            throw new RuntimeException("Failed to retrieve from database", e);
        }
    }

    @Override
    public boolean update(Task task) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int updatedRows = session.createQuery("UPDATE Task SET title = :title, description = :descr, done =: status WHERE id = :id ")
                        .setParameter("title", task.getTitle())
                        .setParameter("descr", task.getDescription())
                        .setParameter("id", task.getId())
                        .setParameter("status", task.isDone())
                        .executeUpdate();
                transaction.commit();
                return updatedRows > 0;
            } catch (Exception e) {
                transaction.rollback();
                log.error("Failed to update task {}", task, e);
                throw new RuntimeException("failed to update", e);
            }
        }
    }

    @Override
    public boolean markAsDone(Integer taskId) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int updateRows = session.createQuery("UPDATE Task SET done = true WHERE id = :id")
                        .setParameter("id", taskId)
                        .executeUpdate();
                transaction.commit();
                return updateRows > 0;
            } catch (Exception e) {
                transaction.rollback();
                log.error("Failed to set Done status", e);
                throw new RuntimeException("failed to update", e);
            }
        }
    }

    @Override
    public boolean delete(Integer taskId) {
        try (Session session = sf.openSession()) {
            var transaction = session.beginTransaction();
            try {
                int deletedRows = session.createQuery("DELETE FROM Task WHERE id = :id")
                        .setParameter("id", taskId)
                        .executeUpdate();
                transaction.commit();
                return deletedRows > 0;
            } catch (Exception e) {
                transaction.rollback();
                log.error("Failed to delete task id={}", taskId, e);
                throw new RuntimeException("Failed to delete task", e);
            }
        }
    }

    @Override
    public Collection<Task> getCompleted() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task WHERE done = :done ORDER by id", Task.class)
                    .setParameter("done", true)
                    .list();
        } catch (Exception e) {
            log.error("Failed to get Completed task", e);
            throw new RuntimeException("Failed to retrieve completed tasks from data base", e);
        }
    }

    @Override
    public Collection<Task> getNew() {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Task WHERE done = :done ORDER by id", Task.class)
                    .setParameter("done", false)
                    .list();
        } catch (Exception e) {
            log.error("Failed to retrieve new tasks", e);
            throw new RuntimeException("Failed to retrieve new tasks from data base", e);
        }
    }
}