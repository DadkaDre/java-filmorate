package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
public interface MpaService {

    Collection<Mpa> getAllMpa();

    Mpa getMpaById(Long id);
}
