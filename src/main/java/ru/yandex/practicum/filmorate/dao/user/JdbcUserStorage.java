package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class JdbcUserStorage implements UserRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final UsersExtractor usersExtractor;
    private final UserExtractor userExtractor;

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM users;";
        return jdbc.query(sql, usersExtractor);

    }

    @Override
    public Optional<User> get(Long id) {
        log.info("Пришли данные в хранилище");
        String sql = "SELECT * FROM users WHERE user_id = :user_id;";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id);
        User user = jdbc.query(sql, parameter, userExtractor);
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO users (user_name, user_login, user_birthday, user_email) VALUES" +
                "(:user_name, :user_login, :user_birthday, :user_email);";

        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_name", user.getName())
                .addValue("user_login", user.getLogin())
                .addValue("user_birthday", user.getBirthday())
                .addValue("user_email", user.getEmail());
        jdbc.update(sql, parameter, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE users SET user_name =:user_name, " +
                "user_login = :user_login," +
                "user_birthday = :user_birthday," +
                "user_email = :user_email " +
                "WHERE user_id = :user_id;";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("user_name", user.getName())
                .addValue("user_login", user.getLogin())
                .addValue("user_birthday", user.getBirthday())
                .addValue("user_email", user.getEmail());
        jdbc.update(sql, parameter);

        return user;
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        String sql = "MERGE INTO friends (user_id, friend_id) VALUES (:user_id, :friend_id);";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("friend_id", friendId);
        jdbc.update(sql, parameter);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = :user_id AND friend_id = :friend_id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("friend_id", friendId);
        jdbc.update(sql, parameter);
    }

    @Override
    public List<User> mutualFriends(Long id, Long otherId) {
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id IN (SELECT f.friend_id " +
                "FROM users AS u " +
                "LEFT JOIN friends AS f ON u.user_id = f.user_id " +
                "WHERE u.user_id = :user_id AND f.friend_id IN (SELECT fr.friend_id " +
                "FROM users AS us " +
                "LEFT JOIN friends fr ON us.user_id = fr.user_id " +
                "WHERE us.user_id = :other_id));";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id)
                .addValue("other_id", otherId);
        return jdbc.query(sql, parameter, usersExtractor);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        String sql = "SELECT * FROM users WHERE user_id IN (SELECT friend_id FROM friends WHERE user_id = :user_id);";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("user_id", id);
        return jdbc.query(sql, parameter, usersExtractor);
    }

}
