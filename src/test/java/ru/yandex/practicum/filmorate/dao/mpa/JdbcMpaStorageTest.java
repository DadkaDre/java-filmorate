package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcMpaStorageTest {
    private final MpaRepository mpaRepository;

    @Test
    void shouldGetAllMpaTest() {
        Optional<Collection<Mpa>> ratingMPAOptional = Optional.ofNullable(mpaRepository.getAllMpa());
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                    assertThat(mpa).isNotEmpty();
                    assertThat(mpa).hasSize(5);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(mpa).element(0).hasFieldOrPropertyWithValue("name", "G");
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(mpa).element(1).hasFieldOrPropertyWithValue("name", "PG");
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(mpa).element(2).hasFieldOrPropertyWithValue("name", "PG-13");
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(mpa).element(3).hasFieldOrPropertyWithValue("name", "R");
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(mpa).element(4).hasFieldOrPropertyWithValue("name", "NC-17");
                });
    }

    @Test
    void shouldGetMpaByIdTest() {
        Optional<Mpa> ratingMPAOptional = mpaRepository.getMpaById(1L);
        assertThat(ratingMPAOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }
}