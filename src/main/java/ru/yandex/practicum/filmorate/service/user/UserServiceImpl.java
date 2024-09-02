package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Override
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на вывод списка пользователей");
        return userRepository.getAllUsers();
    }

    @Override
    public User get(Long id) {
        log.info("Отправлен запрос на получение пользователя по id: {}", id);
        try {
            return userRepository.get(id).orElseThrow(() -> new NotFoundException("нет пользователя с таким id:" + id));
        } catch (EmptyResultDataAccessException | InvalidDataAccessApiUsageException e) {
            throw new NotFoundException("Нет пользователя с таким id: " + id);
        }
    }

    @Override
    public List<User> getAllFriends(Long id) {
        log.info("Запрос друзей пользователя с id: {}", id);
        get(id);
        log.info("Пользователь с id: {} найден в базе", id);
        return userRepository.getAllFriends(id);
    }

    @Override
    public User create(User user) {
        log.info("Отправили команду на создание пользователя: " + user);
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        log.info("Обновляем данные пользователя по id: {} на {} ", user.getId(), user);
        get(user.getId());
        log.info("Пользователь с id: {} найден в базе", user.getId());
        return userRepository.update(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        log.info("Запрос на добавление в друзья от пользователя с id: {} к пользователю с id: {}", id, friendId);
        get(id);
        log.info("Пользователь с id: {} найден в базе", id);
        get(friendId);
        log.info("Пользователь с id: {} найден в базе", friendId);
        userRepository.addFriend(id, friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        log.info("Удаляем пользователя с id: {} из друзей пользователя с id: {}", friendId, id);
        get(id);
        log.info("Пользователь с id: {} найден в базе", id);
        get(friendId);
        log.info("Пользователь с id: {} найден в базе", friendId);
        userRepository.deleteFriend(id, friendId);
    }

    @Override
    public List<User> mutualFriends(Long id, Long otherId) {
        log.info("Делаем запрос на поиск общих друзей пользователя с id: {} и {}", id, otherId);
        get(id);
        log.info("Пользователь с id: {} найден в базе", id);
        get(otherId);
        log.info("Пользователь с id: {} найден в базе", otherId);
        return userRepository.mutualFriends(id, otherId);
    }
}
