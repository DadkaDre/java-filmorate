package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {
	@Autowired
	private FilmController filmController;

	@Test
	@DisplayName("Проверяем валидацию на пустой id у фильма")
	void shouldValidationForEmptyIdForFilm() {
		Film film = new Film();
		film.setId(null);
		film.setName("Название");
		film.setDescription("Описание");
		film.setDuration(120);
		film.setReleaseDate(LocalDate.now());
		assertThrows(NotFoundException.class, () -> {
			Film film1 = filmController.update(film);
		});
	}

	@Test
	@DisplayName("Проверяем валидацию на пустое название фильма")
	void shouldValidationForEmptyNameForFilm() {
		Film film = new Film();
		film.setName(" ");
		film.setDescription("Про врага Бэтмена");
		film.setDuration(120);
		film.setReleaseDate(LocalDate.now());
		assertThrows(ValidationException.class, () -> {
			Film film1 = filmController.create(film);
		});
	}

	@Test
	@DisplayName("Проверяем валидацию на максимальную длину описания фильма")
	void shouldValidationForMaximumLengthForFilmDescription() {
		Film film = new Film();
		film.setName("Джокер");
		film.setDescription("Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена" +
				"Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена " +
				"Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена" +
				"Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена Про врага Бэтмена");
		film.setDuration(120);
		film.setReleaseDate(LocalDate.of(1895, 12, 25));
		assertThrows(ValidationException.class, () -> {
			Film film1 = filmController.create(film);
		});
	}

	@Test
	@DisplayName("Проверяем валидацию дату релиза фильма")
	void shouldValidationOfTheFilmsReleaseDate() {
		Film film = new Film();
		film.setName("Джокер");
		film.setDescription("Про врага Бэтмена");
		film.setDuration(45);
		film.setReleaseDate(LocalDate.of(1765, 12, 10));
		assertThrows(ValidationException.class, () -> {
			Film film1 = filmController.create(film);
		});
	}

	@Test
	@DisplayName("Проверяем валидацию продолжительности фильма")
	void shouldValidationDurationFilm() {
		Film film = new Film();
		film.setName("Джокер");
		film.setDescription("Про врага Бэтмена");
		film.setDuration(-120);
		film.setReleaseDate(LocalDate.now());
		assertThrows(ValidationException.class, () -> {
			Film film1 = filmController.create(film);
		});
	}
}