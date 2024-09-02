package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Collection<Genre> getAllGenre() {
        log.info("Получаем список жанров");
        return genreRepository.getAllGenre();
    }

    @Override
    public Genre getGenreById(Long id) {
        log.info("Получаем жанр по id: {}", id);
        try {
            return genreRepository.getGenreById(id).orElseThrow();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Нет жанра по такому id");
        }
    }

}
