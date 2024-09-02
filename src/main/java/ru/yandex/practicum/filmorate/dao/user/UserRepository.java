package ru.yandex.practicum.filmorate.dao.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAllUsers();

    Optional<User> get(Long id);

    User create(User user);

    User update(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> mutualFriends(Long id, Long otherId);

    List<User> getAllFriends(Long id);

}
