package ru.yandex.practicum.filmorate.dao.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


@Slf4j
@Repository
@RequiredArgsConstructor
@Primary
public class JdbcFilmStorage implements FilmRepository {

    private final NamedParameterJdbcOperations jdbc;
    private final FilmExtractor filmExtractor;
    private final FilmsExtractor filmsExtractor;

    @Override //получение фильма по id
    public Optional<Film> get(Long id) {
        String sql = "SELECT * " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE f.film_id = :film_id; ";
        Film film = jdbc.query(sql, Map.of("film_id", id), filmExtractor);

        return Optional.ofNullable(film);
    }


    public Collection<Film> getAllFilms() {
        String sql = "SELECT * " +
                "FROM films f " +
                "JOIN mpa m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN film_genres fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.genre_id; ";
        Map<Integer, Film> films = jdbc.query(sql, Map.of(), filmsExtractor);
        assert films != null;

        return films.values().stream().toList();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO FILMS (film_name, film_description, release_date, film_duration, mpa_id) " +
                "VALUES (:film_name, :film_description, :release_date, :film_duration, :mpa_id); ";
        Map<String, Object> params = new HashMap<>();

        params.put("film_name", film.getName());
        params.put("film_description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("film_duration", film.getDuration());
        params.put("mpa_id", film.getMpa().getId());

        jdbc.update(sql, new MapSqlParameterSource().addValues(params), keyHolder, new String[]{"film_id"});
        film.setId((long) Objects.requireNonNull(keyHolder.getKey()).intValue());
        addGenre(film);

        return film;
    }


    @Override //для обновления данных существующего фильма.
    public Film update(final Film film) {
        String sql = "UPDATE films SET film_name = :film_name, " +
                "film_description = :film_description, " +
                "release_date = :release_date, " +
                "film_duration = :film_duration, " +
                "mpa_id = :mpa_id " +
                "WHERE film_id = :film_id; ";
        Map<String, Object> params = new HashMap<>();

        params.put("film_name", film.getName());
        params.put("film_description", film.getDescription());
        params.put("release_date", film.getReleaseDate());
        params.put("film_duration", film.getDuration());
        params.put("mpa_id", film.getMpa().getId());
        params.put("film_id", film.getId());

        jdbc.update(sql, params);
        addGenre(film);

        return film;
    }


    @Override
    public void addLike(Long id, Long userId) {
        String sql = "MERGE INTO likes(film_id, user_id) VALUES (:film_id, :user_id); ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }


    @Override
    public void deleteLike(Long id, Long userId) {
        String sql = "DELETE FROM likes WHERE film_id = :film_id AND user_id = :user_id; ";
        jdbc.update(sql, Map.of("film_id", id, "user_id", userId));
    }


    @Override
    public List<Film> getPopular(Long count) {
        String sql = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.film_duration, " +
                "f.mpa_id, m.mpa_name, " +
                "fg.genre_id, g.genre_name, " +
                "COUNT(DISTINCT l.user_id) AS like_count " +
                "FROM films AS f " +
                "LEFT JOIN film_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id, fg.genre_id " +
                "ORDER BY like_count DESC " +
                "LIMIT :count;";

        Map<Integer, Film> films = jdbc.query(sql, Map.of("count", count), filmsExtractor);
        assert films != null;

        return films.values().stream().toList();

    }

    @Override
    public List<Genre> getGenresList(List<Long> ids) {
        String sql = "SELECT * FROM genres WHERE genre_id IN (:genre_id);";
        return jdbc.query(sql, Map.of("genre_id", ids), (rs, rowNum) ->
                new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
    }

    @Override
    public Long getNextId() {
        return null;
    }

    private void addGenre(Film film) {
        Long id = film.getId();
        Set<Genre> genres = film.getGenres();
        Map<String, Object>[] batch = new HashMap[genres.size()];
        int count = 0;

        for (Genre genre : genres) {
            Map<String, Object> map = new HashMap<>();
            map.put("film_id", id);
            map.put("genre_id", genre.getId());
            batch[count++] = map;
        }

        String sqlDelete = "DELETE FROM film_genres WHERE film_id = :film_id AND genre_id = :genre_id; ";
        String sqlInsert = "INSERT INTO film_genres (film_id, genre_id) VALUES (:film_id, :genre_id); ";
        jdbc.batchUpdate(sqlDelete, batch);
        jdbc.batchUpdate(sqlInsert, batch);
    }
}

