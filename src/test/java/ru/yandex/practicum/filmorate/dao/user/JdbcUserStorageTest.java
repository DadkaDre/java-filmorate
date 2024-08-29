package ru.yandex.practicum.filmorate.dao.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcUserStorageTest {

    private final UserRepository userRepository;

    @Test
    @Order(1)
    void shouldGetUserByIdTest() {
        Optional<User> userOptional = userRepository.get(1L);
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Sylya");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Sylik");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "sylik@mail.ru");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2014, 7, 7));
                        }
                );
    }

    @Test
    @Order(2)
    void shouldGetAllUsersTest() {
        Optional<Collection<User>> userList = Optional.ofNullable(userRepository.getAllUsers());
        assertThat(userList)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user).isNotEmpty();
                    assertThat(user).hasSize(4);
                    assertThat(user).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(user).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(user).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(user).element(3).hasFieldOrPropertyWithValue("id", 4L);
                });
    }

    @Test
    @Order(3)
    void shouldCreateUserTest() {
        User newUser = new User();
        newUser.setLogin("manul");
        newUser.setName("manulik");
        newUser.setEmail("manul@yangex.com");
        newUser.setBirthday(LocalDate.of(2020, 8, 19));

        Optional<User> userOptional = Optional.ofNullable(userRepository.create(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "manul");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "manulik");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "manul@yangex.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2020, 8, 19));
                        }
                );
    }

    @Test
    @Order(4)
    void shouldUpdateUserTest() {
        User newUser = new User();
        newUser.setId(5L);
        newUser.setLogin("manul");
        newUser.setName("manulik");
        newUser.setEmail("manul@yangex.com");
        newUser.setBirthday(LocalDate.of(2020, 8, 19));


        Optional<User> userOptional = Optional.ofNullable(userRepository.update(newUser));
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(user).hasFieldOrPropertyWithValue("login", "manul");
                            assertThat(user).hasFieldOrPropertyWithValue("name", "manulik");
                            assertThat(user).hasFieldOrPropertyWithValue("email",
                                    "manul@yangex.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(2020, 8, 19));
                        }
                );
    }

    @Test
    @Order(5)
    void shouldGetAllFriendsTest() {
        Optional<List<User>> usersOptional = Optional.ofNullable(userRepository.getAllFriends(1L));
        assertThat(usersOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).first().hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 3L);
                        }
                );
    }

    @Test
    @Order(6)
    void shouldGetCommonFriendsTest() {
        Optional<List<User>> commonFriendsOptional = Optional
                .ofNullable(userRepository.mutualFriends(2L, 3L));
        assertThat(commonFriendsOptional)
                .isPresent()
                .hasValueSatisfying(users -> {
                            assertThat(users).isNotEmpty();
                            assertThat(users).hasSize(2);
                            assertThat(users).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(users).element(1).hasFieldOrPropertyWithValue("id", 4L);
                        }
                );
    }
}