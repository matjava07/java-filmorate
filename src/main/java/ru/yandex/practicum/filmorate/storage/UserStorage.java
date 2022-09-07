package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User createUser(User user);
    User updateUser(User user);
    List<User> getUsers();

    Optional<User> getUserById(Integer userId);
}
