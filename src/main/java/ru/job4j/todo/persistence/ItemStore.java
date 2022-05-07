package ru.job4j.todo.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public class ItemStore {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure()
            .build();

    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata()
            .buildSessionFactory();

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
        List items = session.createQuery("from ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return items;
    }
}
