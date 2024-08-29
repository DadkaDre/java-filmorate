package ru.yandex.practicum.filmorate.dao.film;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;



@Component
public class FilmExtractor implements ResultSetExtractor<Film> {
    public Film extractData(final ResultSet rs) throws SQLException, DataAccessException {
        Film film = null;

        if (rs.next()) {
            film = new Film();
            film.setName(rs.getString("film_name"));
            film.setDescription(rs.getString("film_description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("film_duration"));
            film.setId(rs.getLong("film_id"));
            film.setMpa(new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name")));

            int idGenre = rs.getInt("genre_id");
            if (idGenre != 0) {
                do {
                    film.getGenres()
                            .add(new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
                } while (rs.next());
            }
        }
        return film;
    }
}