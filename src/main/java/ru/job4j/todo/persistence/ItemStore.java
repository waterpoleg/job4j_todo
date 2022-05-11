package ru.job4j.todo.persistence;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Repository
public class ItemStore {

    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public void add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
    }

    public Collection<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session
                .createQuery("from Item")
                .list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Collection<Item> findByCondition(boolean condition) {
        Session session = sf.openSession();
        session.beginTransaction();
        List items = session
                .createQuery("from Item where done = :param")
                .setParameter("param", condition)
                .list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item item = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public void setCompleted(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("update Item set done = true where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void deleteItem(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Item where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void updateItem(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }
}
