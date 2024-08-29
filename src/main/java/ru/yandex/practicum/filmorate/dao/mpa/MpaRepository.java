package ru.yandex.practicum.filmorate.dao.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;


public interface MpaRepository {

    Collection<Mpa> getAllMpa();

    Optional<Mpa> getMpaById(Long id);
}
