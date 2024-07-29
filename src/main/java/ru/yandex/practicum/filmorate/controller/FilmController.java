package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidateService;
import java.util.Collection;



@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private static final String PATH = "/{id}/like/{userId}";
    private final ValidateService validateService;
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        validateService.checkId(id);
        return filmService.get(id);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Create film: start - " + film);
        validateService.checkValidationFilm(film);
        Film newFilm = filmService.create(film);
        log.info("Create film: end - " + film);
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Update film: start - " + film);
        validateService.checkId(film.getId());
        validateService.checkValidationFilm(film);
        Film updateFilm = filmService.update(film);
        log.info("Update film: end - " + updateFilm);
        return updateFilm;
    }

    @PutMapping(PATH)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        validateService.checkId(id);
        validateService.checkId(userId);
        filmService.addLike(id, userId);

    }

    @DeleteMapping(PATH)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        validateService.checkId(id);
        validateService.checkId(userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopular(count);
    }
}
