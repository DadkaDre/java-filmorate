package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class ValidateService {

    private final LocalDate filmBirthday = LocalDate.of(1895, 12, 25);

    public void checkValidationFilm(Film film) {

        if (film.getName() == null || film.getName().trim().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Превышена максимальная длина описания фильма");
        }

        if (film.getReleaseDate().equals(filmBirthday) ||
                film.getReleaseDate().isBefore(filmBirthday)) {
            throw new ValidationException("Дата релиза должна быть позже 1895-12-25");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        }
    }

    public void checkIdFilm(Film film) {

        if (film.getId() == null) {
            throw new NotFoundException("Поле id нет в запросе");
        }
    }

    public void checkValidationUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank() || !(user.getEmail().contains("@"))) {
            throw new ValidationException("почта не может быть пустой или не содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Поле login не может быть пустым или содержать пробелы");
        }
        if (user.getBirthday().isAfter(LocalDate.now()) || user.getBirthday() == null) {
            throw new ValidationException("Дата рождения введена не корректно");
        }
    }

    public void checkId(User user) {
        if (user.getId() == null) {
            throw new NotFoundException("Нет поля id ");
        }
    }
}
