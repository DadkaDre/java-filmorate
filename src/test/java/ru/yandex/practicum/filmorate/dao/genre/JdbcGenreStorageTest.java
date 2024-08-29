package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcGenreStorageTest {
    private final GenreRepository genreRepository;

    @Test
    void shouldGetAllGenresTest() {
        Optional<Collection<Genre>> genresOptional = Optional.ofNullable(genreRepository.getAllGenre());
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres -> {
                            assertThat(genres).isNotEmpty();
                            assertThat(genres).hasSize(6);
                            assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(genres).element(0)
                                    .hasFieldOrPropertyWithValue("name", "Комедия");
                            assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2L);
                            assertThat(genres).element(1)
                                    .hasFieldOrPropertyWithValue("name", "Драма");
                            assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(genres).element(2)
                                    .hasFieldOrPropertyWithValue("name", "Мультфильм");
                            assertThat(genres).element(3).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(genres).element(3)
                                    .hasFieldOrPropertyWithValue("name", "Триллер");
                            assertThat(genres).element(4).hasFieldOrPropertyWithValue("id", 5L);
                            assertThat(genres).element(4)
                                    .hasFieldOrPropertyWithValue("name", "Документальный");
                            assertThat(genres).element(5).hasFieldOrPropertyWithValue("id", 6L);
                            assertThat(genres).element(5)
                                    .hasFieldOrPropertyWithValue("name", "Боевик");
                        }
                );
    }

    @Test
    void shouldGetGenreByIdTest() {
        Optional<Genre> genreOp = genreRepository.getGenreById(1L);
        assertThat(genreOp)
                .isPresent()
                .hasValueSatisfying(genres -> {
                    assertThat(genres).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(genres).hasFieldOrPropertyWithValue("name", "Комедия");
                });
    }

    @Test
    void shouldGetGenresListTest() {
        List<Long> listGenres = List.of(1L, 2L, 3L);
        Optional<Collection<Genre>> genresOptional = Optional.ofNullable(genreRepository.getGenresList(listGenres));
        assertThat(genresOptional)
                .isPresent()
                .hasValueSatisfying(genres -> {
                    assertThat(genres).isNotEmpty();
                    assertThat(genres).hasSize(3);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(genres).element(0).hasFieldOrPropertyWithValue("name", "Комедия");
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(genres).element(1).hasFieldOrPropertyWithValue("name", "Драма");
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(genres).element(2).hasFieldOrPropertyWithValue("name", "Мультфильм");
                });
    }
}