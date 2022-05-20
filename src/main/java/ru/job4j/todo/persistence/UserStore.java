package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
import java.util.function.Function;

@ThreadSafe
@Repository
public class UserStore {

    private final SessionFactory sf;

    public UserStore(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Optional<User> add(User user) {
        this.tx(session -> {
            session.save(user);
            return Optional.empty();
        });
        return Optional.ofNullable(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return tx(session ->
                session.createQuery("from User where email = :email and password = :password")
                        .setParameter("email", email)
                        .setParameter("password", password)
                        .uniqueResultOptional());
    }
}
