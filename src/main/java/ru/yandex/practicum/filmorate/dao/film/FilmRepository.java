package ru.yandex.practicum.filmorate.dao.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface FilmRepository {

    Optional<Film> get(Long id);

    Collection<Film> getAllFilms();

    Film create(Film film);

    Film update(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long Id, Long userId);

    List<Film> getPopular(Long count);

    List<Genre> getGenresList(List<Long> ids);

    Long getNextId();
}
