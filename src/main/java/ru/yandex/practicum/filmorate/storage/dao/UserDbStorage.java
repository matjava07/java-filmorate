package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User createUser(User user) {
        String sql =
                "insert into USERS (EMAIL, LOGIN, USER_NAME, BIRTHDAY) " +
                        "values (?, ?, ?, ?)";

        int id = (int) insertUser(user, sql);
        user.setId(id);

        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql =
                "update USERS set EMAIL = ?, LOGIN = ?, USER_NAME = ?, BIRTHDAY = ? where USER_ID = ?";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public List<User> getUsers() {
        String sql =
                "select * " +
                        "from USERS";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser);
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        String sql = "select * from users where user_id = ?";

        List<User> users = jdbcTemplate.query(sql, UserDbStorage::makeUser, userId);

        if (users.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(users.get(0));
    }

    private static User makeUser(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("user_name"),
                rs.getDate("birthday").toLocalDate()
        );
    }

    private long insertUser(User user, String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
}
