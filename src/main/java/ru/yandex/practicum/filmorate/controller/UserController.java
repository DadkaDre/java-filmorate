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
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.Collection;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String PATH2 = "/{id}/friends/{friend-id}";
    private final UserService userService;
    private final ValidateService validateService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Get user: start" + id);
        validateService.checkId(id);
        return userService.get(id);
    }

    @GetMapping("{id}/friends")
    public List<User> getAllFriends(@PathVariable Long id) {
        validateService.checkId(id);
        return userService.getAllFriends(id);
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
        validateService.checkId(user.getId());
        validateService.checkValidationUser(user);
        User userUpdate = userService.update(user);
        log.info("Update user: finish - " + userUpdate);
        return userUpdate;
    }

    @PutMapping(PATH2)
    public void addFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        validateService.checkId(id);
        validateService.checkId(friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping(PATH2)
    public void deleteFriend(@PathVariable Long id, @PathVariable("friend-id") Long friendId) {
        validateService.checkId(id);
        validateService.checkId(friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends/common/{other-id}")
    public List<User> mutualFriends(@PathVariable Long id, @PathVariable("other-id") Long otherId) {
        validateService.checkId(id);
        validateService.checkId(otherId);
        return userService.mutualFriends(id, otherId);
    }
}