package ru.yandex.practicum.filmorate.dao.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcGenreStorage implements GenreRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM genres ORDER BY genre_id";
        return jdbc.query(sql, (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("genre_name")));
    }


    @Override
    public Optional<Genre> getGenreById(Long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = :genre_id;";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("genre_id", id);

        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getLong("genre_id"));
            genre.setName(rs.getString("genre_name"));
            return genre;
        }));
    }

    @Override
    public List<Genre> getGenresList(List<Long> ids) {
        String sql = "SELECT * FROM genres WHERE genre_id IN (:genre_id);";
        return jdbc.query(sql, Map.of("genre_id", ids), genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getLong("genre_id"), rs.getString("genre_name"));
    }
}
