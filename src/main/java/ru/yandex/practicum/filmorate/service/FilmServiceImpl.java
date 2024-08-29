package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.film.FilmRepository;
import ru.yandex.practicum.filmorate.dao.user.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final ValidateService validateService;

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Отправлен запрос на получение всех фильмов");
        return filmRepository.getAllFilms();
    }

    @Override
    public Film get(Long id) {
        log.info("Отправили запрос на получение фильма с id: {}", id);
        try {
            return filmRepository.get(id).orElseThrow();
        } catch (EmptyResultDataAccessException | InvalidDataAccessApiUsageException e) {
            throw new NotFoundException("Нет пользователя с таким id: " + id);
        }
    }

    @Override
    public Film create(Film film) {
        log.info("Отправлен запрос на создание фильма с телом {}", film);
        validateService.checkMpa(film.getMpa().getId());
        Film filmAfterCheck = validateService.checkGenre(film);
        return filmRepository.create(filmAfterCheck);
    }

    @Override
    public Film update(Film film) {
        validateService.checkMpa(film.getMpa().getId());
        validateService.checkGenre(film);
        get(film.getId());
        return filmRepository.update(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        get(id);
        userRepository.get(userId);
        filmRepository.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        get(id);
        userRepository.get(userId);
        filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopular(Long count) {
        if (filmRepository.getAllFilms().isEmpty()) {
            log.warn("Ошибка при получении списка фильмов. Список фильмов пуст.");
            throw new NotFoundException("Ошибка при получении списка фильмов. Список фильмов пуст.");
        }
        if (filmRepository.getAllFilms().size() < count) {
            return filmRepository.getPopular((long) filmRepository.getAllFilms().size());
        }
        return filmRepository.getPopular(count);
    }
}
