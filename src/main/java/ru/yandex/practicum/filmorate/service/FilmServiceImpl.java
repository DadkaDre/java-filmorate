package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmRepository;
import ru.yandex.practicum.filmorate.dao.UserRepository;
import ru.yandex.practicum.filmorate.model.Film;
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
        filmRepository.checkFilm(id);
        return filmRepository.get(id);
    }

    @Override
    public Film create(Film film) {
        return filmRepository.create(film);
    }

    @Override
    public Film update(Film film) {
        filmRepository.checkFilm(film.getId());
        return filmRepository.update(film);
    }

    @Override
    public void addLike(Long id, Long userId) {
        filmRepository.checkFilm(id);
        userRepository.checkUserByID(userId);
        filmRepository.addLike(id, userId);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        filmRepository.checkFilm(id);
        userRepository.checkUserByID(userId);
        filmRepository.deleteLike(id, userId);
    }

    @Override
    public List<Film> getPopular(Long count) {
        return filmRepository.getPopular(count);
    }

}
