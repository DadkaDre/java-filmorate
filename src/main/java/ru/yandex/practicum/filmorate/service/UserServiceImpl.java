package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User get(Long id) {
        if (id == null) {
            throw new NotFoundException("Параметр id отсутствует");
        }
        return userRepository.get(id);
    }

    @Override
    public List<User> allFriends(Long userId) {
        if (userId == null) {
            throw new ValidationException("Нет параметра id в запросе");
        }
        return userRepository.allFriends(userId);
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        if (userRepository.get(user.getId()) == null) {
            throw new NotFoundException("Фильм по id: " + user.getId() + " не найден.");
        }
        return userRepository.update(user);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkIdFields(userId, friendId);
        userRepository.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        checkIdFields(userId, friendId);
        userRepository.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> mutualFriends(Long userId, Long otherUserId) {
        checkIdFields(userId, otherUserId);
        return userRepository.mutualFriends(userId, otherUserId);
    }

    public void checkIdFields(Long userId, Long friendId) {
        if (userId == null) {
            throw new ValidationException("Параметр id фильма не задан");
        }
        if (friendId == null) {
            throw new ValidationException("Параметр id фильма не задан");
        }
    }
}
