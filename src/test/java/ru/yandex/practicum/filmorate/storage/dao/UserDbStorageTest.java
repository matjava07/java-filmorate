package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDbStorageTest {

    private final UserStorage userDbStorage;

    @Test
    void createUser() {
        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        User user = userDbStorage.createUser(userNew);

        assertEquals(userNew, user);

    }

    @Test
    void updateUser() {

        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userDbStorage.createUser(userNew);

        User userNew1 = new User();
        userNew1.setId(1);
        userNew1.setName("Matvey");
        userNew1.setEmail("matjava07@yandex.ru");
        userNew1.setLogin("Materiy");
        userNew1.setBirthday(LocalDate.of(2001, 5, 27));

        User user1 = userDbStorage.updateUser(userNew1);

        assertEquals(userNew1, user1);
    }

    @Test
    void getUsers() {
        User userNew = new User();
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        User user = userDbStorage.createUser(userNew);

        List<User> users = userDbStorage.getUsers();
        assertTrue(users.contains(user));

    }

    @Test
    void getUserById() {
        User userNew = new User();
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));
        userDbStorage.createUser(userNew);

        Optional<User> userOptional = userDbStorage.getUserById(1);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }
}