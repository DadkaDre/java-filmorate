package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserRepository {

    Collection<User> getAllUsers();

    User get(Long id);

    User create(User user);

    User update(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> mutualFriends(Long id, Long friendId);

    List<User> allFriends(Long id);

    void checkUserByID(Long id);
}
