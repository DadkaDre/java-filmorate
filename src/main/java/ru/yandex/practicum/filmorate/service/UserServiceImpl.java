package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserRepository;
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
        userRepository.checkUserByID(id);
        return userRepository.get(id);
    }

    @Override
    public List<User> allFriends(Long id) {
        userRepository.checkUserByID(id);
        return userRepository.allFriends(id);
    }

    @Override
    public User create(User user) {
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        userRepository.checkUserByID(user.getId());
        return userRepository.update(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        userRepository.checkUserByID(id);
        userRepository.checkUserByID(friendId);
        userRepository.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        userRepository.checkUserByID(id);
        userRepository.checkUserByID(friendId);
        userRepository.deleteFriend(id, friendId);
    }

    @Override
    public List<User> mutualFriends(Long id, Long friendId) {
        userRepository.checkUserByID(id);
        userRepository.checkUserByID(friendId);
        return userRepository.mutualFriends(id, friendId);
    }
}
