package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateService {

    private final LocalDate filmBirthday = LocalDate.of(1895, 12, 25);
    private final NamedParameterJdbcOperations jdbc;
    private final GenreRepository genreRepository;

    public void checkValidationFilm(Film film) {

        if (film.getName() == null || film.getName().trim().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Превышена максимальная длина описания фильма");
        }

        if (film.getReleaseDate().equals(filmBirthday) ||
                film.getReleaseDate().isBefore(filmBirthday)) {
            throw new ValidationException("Дата релиза должна быть позже 1895-12-25");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    public void checkId(Long id) {
        if (id == null) {
            throw new NotFoundException("Нет поля id в параметрах запроса");
        }
    }

    public void checkValidationUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("почта не может быть пустой или не содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Поле login не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Дата рождения введена не корректно");
        }
    }

    public void checkMpa(Long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = :mpa_id";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("mpa_id", id);
        try {
            jdbc.queryForObject(sql, parameter, (rs, rowNum) -> {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getLong("mpa_id"));
                mpa.setName(rs.getString("mpa_name"));
                return mpa;
            });
        } catch (EmptyResultDataAccessException ignored) {
            throw new ValidationException("Нет такого mpa");
        }
    }

    public Film checkGenre(Film film) {
        if (Objects.nonNull(film.getGenres())) {
            List<Long> idGenres = film.getGenres().stream().map(Genre::getId).toList();
            LinkedHashSet<Genre> genres = genreRepository.getGenresList(idGenres).stream()
                    .sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toCollection(LinkedHashSet::new));
            if (film.getGenres().size() == genres.size()) {
                film.getGenres().clear();
                film.setGenres(genres);
            } else {
                log.warn("Жанр введен некорректно.");
                throw new ValidationException("Жанр введен некорректно.");
            }
        }
        return film;
    }

    public List<Long> listGenreId() {
        String sql = "SELECT * FROM genres";
        return jdbc.query(sql, (rs, rowNow) ->
                        new Genre(rs.getLong("genre_id"), rs.getString("genre_name"))).stream()
                .map(Genre::getId)
                .toList();
    }
}