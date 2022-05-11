package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.function.Function;

@ThreadSafe
@Repository
public class ItemStore {

    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
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

    public void add(Item item) {
        this.tx(session -> {
            session.save(item);
            return item;
        });
    }

    public Collection<Item> findAll() {
        return this.tx(session -> session.createQuery("from Item ").list());
    }

    public Collection<Item> findByCondition(boolean condition) {
        return this.tx(session -> session.createQuery("from Item where done = :param")
                .setParameter("param", condition)
                .list());
    }

    public Item findById(int id) {
        return this.tx(session -> session.get(Item.class, id));
    }

    public void setCompleted(int id) {
        this.tx(session -> session.createQuery("update Item set done = true where id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }

    public void deleteItem(int id) {
        this.tx(session -> session.createQuery("delete from Item where id = :id")
                .setParameter("id", id)
                .executeUpdate());
    }

    public void updateItem(Item item) {
        this.tx(session -> {
            session.update(item);
            return item;
        });
    }
}
