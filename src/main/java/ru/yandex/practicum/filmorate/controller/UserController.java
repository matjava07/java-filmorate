package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private final GenerateId generateId;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        if (doValidate(user)) {
            user.setId(generateId.getId());
            users.put(user.getId(), user);
            log.debug("Пользователь " + user.getName() + " успешно добавлен");
            return user;
        } else {
            throw new ValidationException("Не удалось создать пользователя");
        }
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (doValidate(user) && user.getId() > 0) {
            if(users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            }
            log.debug("Пользователь " + user.getName() + " успешно обновлен");
            return user;
        } else {
            throw new ValidationException("Не удалось обновить пользователя");
        }
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private Boolean doValidate(User user) {
        if (!user.getLogin().contains(" ")) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            return true;
        }
        return false;
    }
}
