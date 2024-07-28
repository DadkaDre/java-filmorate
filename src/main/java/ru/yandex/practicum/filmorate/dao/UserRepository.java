package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    Collection<User> getAllUsers();

    User get(Long id);

    User create(User user);

    User update(User user);

    void addFriend(Long filmId, Long userId);

    void deleteFriend(Long filmId, Long userId);

    List<User> mutualFriends(Long userId, Long otherUserId);

    List<User> allFriends(Long userId);
}
