package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    private User getUser(HttpSession session) {
        var user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Guest");
        }
        return user;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("items", itemService.findAll());
        return "items";
    }

    @GetMapping("/addItem")
    public String addItem(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        return "addItem";
    }

    @PostMapping("/saveItem")
    public String saveItem(@ModelAttribute Item item, HttpSession session) {
        item.setUser(getUser(session));
        itemService.create(item);
        return "redirect:/items";
    }

    @GetMapping("/completedItems")
    public String completedItems(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("items", itemService.findByCondition(true));
        return "items";
    }

    @GetMapping("/newItems")
    public String newItems(Model model, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("items", itemService.findByCondition(false));
        return "items";
    }

    @GetMapping("/itemDetails/{itemId}")
    public String itemDetails(Model model, @PathVariable("itemId") int id, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("item", itemService.findById(id));
        return "itemDetails";
    }

    @GetMapping("/setCompleted/{itemId}")
    public String setCompleted(Model model, @PathVariable("itemId") int id, HttpSession session) {
        itemService.setCompleted(id);
        return itemDetails(model, id, session);
    }

    @GetMapping("/deleteItem/{itemId}")
    public String deleteItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        itemService.deleteItem(id);
        return items(model, session);
    }

    @GetMapping("/editItem/{itemId}")
    public String editItem(Model model, @PathVariable("itemId") int id, HttpSession session) {
        model.addAttribute("user", getUser(session));
        model.addAttribute("item", itemService.findById(id));
        return "formEditItem";
    }

    @PostMapping("/updateItem")
    public String updateItem(Model model, @ModelAttribute Item item, HttpSession session) {
        itemService.updateItem(item);
        return itemDetails(model, item.getId(), session);
    }
}
