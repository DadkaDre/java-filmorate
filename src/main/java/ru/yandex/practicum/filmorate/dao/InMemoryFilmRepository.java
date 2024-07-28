package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Component
public class InMemoryFilmRepository implements FilmRepository {
    Long currentId = 0L;

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(Long id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        return film;
    }

    public Film update(Film film) {
        Film updateFilm = checkUpdateFieldsFilm(film);
        films.put(updateFilm.getId(), updateFilm);
        return updateFilm;
    }

    @Override
    public void addLike(Long id, Long userId) {
        Set<Long> filmLikes = likes.computeIfAbsent(id, k -> new HashSet<>());
        filmLikes.add(userId);

    }

    @Override
    public void deleteLike(Long id, Long userId) {
        Set<Long> filmLikes = likes.computeIfAbsent(id, k -> new HashSet<>());
        filmLikes.remove(userId);
    }

    @Override
    public List<Film> getPopular(Long count) {
        List<Map.Entry<Long, Integer>> sortedMovies = new ArrayList<>();

        for (Map.Entry<Long, Set<Long>> entry : likes.entrySet()) {
            Long movieId = entry.getKey();
            Set<Long> userId = entry.getValue();
            sortedMovies.add(new AbstractMap.SimpleEntry<>(movieId, userId.size()));
        }
        sortedMovies.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        List<Film> sortedFilms = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry : sortedMovies) {
            sortedFilms.add(films.get(entry.getKey()));
        }
        return sortedFilms;
    }

    @Override
    public Long getNextId() {
        return ++currentId;
    }

    private Film checkUpdateFieldsFilm(Film film) {
        Film oldFilm = films.get(film.getId());
        oldFilm.setName(film.getName());
        if (film.getDescription() != null) {
            oldFilm.setDescription(film.getDescription());
        }
        if (film.getDuration() != null) {
            oldFilm.setDuration(film.getDuration());
        }
        if (film.getReleaseDate() != null) {
            oldFilm.setReleaseDate(film.getReleaseDate());
        }
        return oldFilm;
    }

    @Override
    public void checkFilm(Long id) {
        if (films.get(id) == null) {
            throw new NotFoundException("Фильма по id: " + id + " не существует");
        }
    }
}
