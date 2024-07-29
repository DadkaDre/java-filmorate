package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;

@Service
public interface FilmService {
    Collection<Film> getAllFilms();

    Film get(Long id);

    Film create(Film film);

    Film update(Film film);

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> getPopular(Long count);
}
