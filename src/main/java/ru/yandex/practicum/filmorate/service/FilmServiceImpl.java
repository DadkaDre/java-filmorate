package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Film> getAllFilms() {
        return filmRepository.getAllFilms();
    }

    @Override
    public Film get(Long id) {
        if (id == null) {
            throw new ValidationException(" Отсутствует параметр id");
        }
        Film film = filmRepository.get(id);

        if (film == null) {
            throw new NotFoundException("Фильм по id:" + id + " не найден");
        }

        return film;
    }

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        if (filmRepository.get(film.getId()) == null) {
            throw new NotFoundException("Нет фильма по id: " + film.getId());
        }
        return filmRepository.update(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        checkIdFields(id, userId);
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("Нет пользователя с id: " + userId);
        }
        if (filmRepository.get(id) == null) {
            throw new NotFoundException("Нет фильма по id " + get(id));
        }
        User user = userRepository.get(userId);
        filmRepository.addLike(id, user.getId());
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        checkIdFields(id, userId);
        if (userRepository.get(userId) == null) {
            throw new NotFoundException("Нет пользователя с id: " + userId);
        }
        if (filmRepository.get(id) == null) {
            throw new NotFoundException("Нет пользователя с id: " + userId);
        }

        filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopular(Long count) {
        return filmRepository.getPopular(count);
    }


    private void checkIdFields(Long id, Long userId) {
        if (id == null) {
            throw new NotFoundException("Параметр id фильма не задан");
        }
        if (userId == null) {
            throw new ValidationException("Параметр id фильма не задан");
        }
    }
}
