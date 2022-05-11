package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

@ThreadSafe
@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public String items(Model model) {
        model.addAttribute("items", itemService.findAll());
        return "items";
    }

    @GetMapping("/addItem")
    public String addItem(Model model) {
        return "addItem";
    }

    @PostMapping("/saveItem")
    public String saveItem(@ModelAttribute Item item) {
        itemService.create(item);
        return "redirect:/items";
    }

    @GetMapping("/completedItems")
    public String completedItems(Model model) {
        model.addAttribute("items", itemService.findByCondition(true));
        return "items";
    }

    @GetMapping("/newItems")
    public String newItems(Model model) {
        model.addAttribute("items", itemService.findByCondition(false));
        return "items";
    }

    @GetMapping("/itemDetails/{itemId}")
    public String itemDetails(Model model, @PathVariable("itemId") int id) {
        model.addAttribute("item", itemService.findById(id));
        return "itemDetails";
    }

    @GetMapping("/setCompleted/{itemId}")
    public String setCompleted(Model model, @PathVariable("itemId") int id) {
        itemService.setCompleted(id);
        return itemDetails(model, id);
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(Model model, @PathVariable("itemId") int id) {
        itemService.deleteItem(id);
        return items(model);
    }
}
