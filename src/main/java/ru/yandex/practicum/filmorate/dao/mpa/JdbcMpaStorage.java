package ru.yandex.practicum.filmorate.dao.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcMpaStorage implements MpaRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa;";
        return jdbc.query(sql, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getLong("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            return mpa;
        });
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = :mpa_id;";
        SqlParameterSource parameter = new MapSqlParameterSource()
                .addValue("mpa_id", id);

        return Optional.ofNullable(jdbc.queryForObject(sql, parameter, (rs, rowNum) -> {
            Mpa mpa = new Mpa();
            mpa.setId(rs.getLong("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            return mpa;
        }));
    }
}
