package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ImportResource
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcFilmStorageTest {

    private final FilmRepository filmRepository;

    @Test
    @Order(1)
    void shouldFilmByIdTest() {
        Optional<Film> filmOptional = filmRepository.get(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "film1");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description1");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 150);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2011, 1, 1));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film.getGenres()).hasSize(1);
                            assertThat(film.getGenres()).element(0)
                                    .hasFieldOrPropertyWithValue("id", 1L);
                        }
                );
    }

    @Test
    @Order(2)
    void shouldGetAllFilmsTest() {
        Optional<Collection<Film>> filmListOptional = Optional.ofNullable(filmRepository.getAllFilms());
        assertThat(filmListOptional)
                .isPresent()
                .hasValueSatisfying(films -> {
                    assertThat(films).isNotEmpty();
                    assertThat(films).hasSize(7);
                    assertThat(films).element(0).hasFieldOrPropertyWithValue("id", 1L);
                    assertThat(films).element(0)
                            .hasFieldOrPropertyWithValue("name", "film1");
                    assertThat(films).element(1).hasFieldOrPropertyWithValue("id", 2L);
                    assertThat(films).element(1)
                            .hasFieldOrPropertyWithValue("name", "film2");
                    assertThat(films).element(2).hasFieldOrPropertyWithValue("id", 3L);
                    assertThat(films).element(2)
                            .hasFieldOrPropertyWithValue("name", "film3");
                    assertThat(films).element(3).hasFieldOrPropertyWithValue("id", 4L);
                    assertThat(films).element(3)
                            .hasFieldOrPropertyWithValue("name", "film4");
                    assertThat(films).element(4).hasFieldOrPropertyWithValue("id", 5L);
                    assertThat(films).element(4)
                            .hasFieldOrPropertyWithValue("name", "film5");
                    assertThat(films).element(5).hasFieldOrPropertyWithValue("id", 6L);
                    assertThat(films).element(5)
                            .hasFieldOrPropertyWithValue("name", "film6");
                    assertThat(films).element(6).hasFieldOrPropertyWithValue("id", 7L);
                    assertThat(films).element(6)
                            .hasFieldOrPropertyWithValue("name", "film7");
                });
    }

    @Test
    @Order(3)
    void shouldCreateFilmTest() {
        Film newFilm = new Film();
        newFilm.setName("film8");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2014, 1, 1));
        newFilm.setDuration(180);
        newFilm.setMpa(new Mpa(1L, null));

        Optional<Film> filmOptional = Optional.ofNullable(filmRepository.create(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 8L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "film8");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description8");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 180);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2014, 1, 1));
                            assertThat(film.getMpa()).hasFieldOrPropertyWithValue("id", 1L);
                        }
                );
    }

    @Test
    @Order(4)
    void shouldUpdateFilmTest() {
        Film newFilm = new Film();
        newFilm.setId(8L);
        newFilm.setName("film8");
        newFilm.setDescription("description8");
        newFilm.setReleaseDate(LocalDate.of(2024, 8, 22));
        newFilm.setDuration(170);
        newFilm.setMpa(new Mpa(1L, null));

        Optional<Film> filmOptional = Optional.ofNullable(filmRepository.update(newFilm));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 8L);
                            assertThat(film).hasFieldOrPropertyWithValue("name",
                                    "film8");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description8");
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 170);
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2024, 8, 22));
                        }
                );
    }
}