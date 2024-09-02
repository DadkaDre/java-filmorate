package ru.yandex.practicum.filmorate.service.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Service
public interface UserService {
    Collection<User> getAllUsers();

    User get(Long id);

    User create(User user);

    User update(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> mutualFriends(Long id, Long otherId);

    List<User> getAllFriends(Long id);
}
