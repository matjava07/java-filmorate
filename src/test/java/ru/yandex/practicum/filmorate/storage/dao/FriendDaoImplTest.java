package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FriendStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.sql.ClientInfoStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FriendDaoImplTest {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    @Test
    void addFriend() {
        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(userNew);

        User friend = new User();
        friend.setId(2);
        friend.setName("Matvey");
        friend.setEmail("mva07@yandex.ru");
        friend.setLogin("Materiy");
        friend.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(friend);

        friendStorage.addFriend(1, 2);

        assertEquals(friend, friendStorage.getFriends(1).get(0));
    }

    @Test
    void deleteFriend() {
        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(userNew);

        User friend = new User();
        friend.setId(2);
        friend.setName("Matvey");
        friend.setEmail("mva07@yandex.ru");
        friend.setLogin("Materiy");
        friend.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(friend);

        friendStorage.addFriend(1, 2);
        assertEquals(friend, friendStorage.getFriends(1).get(0));

        friendStorage.deleteFriend(1, 2);
        assertTrue(friendStorage.getFriends(1).isEmpty());
    }

    @Test
    void getFriends() {
        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(userNew);

        User friend = new User();
        friend.setId(2);
        friend.setName("Matvey");
        friend.setEmail("mva07@yandex.ru");
        friend.setLogin("Materiy");
        friend.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(friend);

        friendStorage.addFriend(1, 2);
        List<User> friends = new ArrayList<>();
        friends.add(friend);

        assertEquals(friends, friendStorage.getFriends(1));
    }

    @Test
    void getCommonFriends() {
        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(userNew);

        User friend = new User();
        friend.setId(2);
        friend.setName("Matvey");
        friend.setEmail("mva07@yandex.ru");
        friend.setLogin("Materiy");
        friend.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(friend);

        friendStorage.addFriend(1, 2);

        User friendNew = new User();
        friendNew.setId(3);
        friendNew.setName("Matvey");
        friendNew.setEmail("m0va07@yandex.ru");
        friendNew.setLogin("Matiy");
        friendNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(friendNew);

        friendStorage.addFriend(3, 2);

        assertEquals(friend, friendStorage.getCommonFriends(1, 3).get(0));
    }
}