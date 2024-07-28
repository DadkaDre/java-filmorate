package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmRepository {

    Film get(Long id);

    List<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long filmId, Long userId);

    List<Film> getPopular(Long count);

    Long getNextId();
}
