package ru.job4j.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class CrudRepository {
    private final SessionFactory sf;

    public <R> R tx(Function<Session, R> command) {
        try (Session session = sf.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                R rsl = command.apply(session);
                transaction.commit();
                return rsl;
            } catch (RuntimeException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public void run(Consumer<Session> command) {
        tx(session -> {
            command.accept(session);
            return null;
        });
    }

    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            Query sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            sq.executeUpdate();
        };
        run(command);
    }

    public int update(String query, Map<String, Object> args) {
        return tx(session -> {
            Query sq = session.createQuery(query);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.executeUpdate();
        });
    }

    public boolean updateAndReturnBoolean(String query, Map<String, Object> args) {
        int updatedRows = update(query, args);
        return updatedRows > 0;
    }

    public <R> R query(String query, Class<R> cl, Map<String, Object> args) {
        Function<Session, R> command = session -> {
            Query<R> sq = session.createQuery(query, cl);
            for (Map.Entry<String, Object> arg : args.entrySet()) {
                sq.setParameter(arg.getKey(), arg.getValue());
            }
            return sq.getSingleResult();
        };
        return tx(command);
    }

    public <R> List<R> query(String query, Class<R> cl) {
        Function<Session, List<R>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }
}
