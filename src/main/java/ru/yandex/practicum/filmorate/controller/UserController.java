package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final ValidateService validateService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Get user by id: start" + id);
        return userService.get(id);
    }

    @GetMapping("{userId}/friends")
    public List<User> allFriends(@PathVariable Long userId) {
        return userService.allFriends(userId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Create user: start - " + user);
        validateService.checkValidationUser(user);
        userService.create(user);
        log.info("Create user: finish  " + user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {

        log.info("Update user: start -" + user);
        validateService.checkId(user);
        validateService.checkValidationUser(user);
        User userUpdate = userService.update(user);
        log.info("Update user: finish - " + userUpdate);
        return userUpdate;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> mutualFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long otherUserId) {
        return userService.mutualFriends(userId, otherUserId);
    }
}