package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.persistence.ItemStore;

import java.util.ArrayList;

@Service
public class ItemService {

    private final ItemStore itemStore;

    public ItemService(ItemStore itemStore) {
        this.itemStore = itemStore;
    }

    public void create(Item item) {
        itemStore.add(item);
    }

    public Object findAll() {
        return new ArrayList<>(itemStore.findAll());
    }
}
