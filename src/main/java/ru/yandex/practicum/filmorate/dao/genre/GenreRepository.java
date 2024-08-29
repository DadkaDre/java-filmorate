package ru.yandex.practicum.filmorate.dao.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Collection <Genre> getAllGenre();

    Optional<Genre> getGenreById(Long id);

    List<Genre> getGenresList(List<Long> ids);
}
