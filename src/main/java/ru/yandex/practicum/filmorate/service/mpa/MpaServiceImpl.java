package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.mpa.MpaRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaServiceImpl implements MpaService {

    private final MpaRepository mpaRepository;

    @Override
    public Collection<Mpa> getAllMpa() {
        log.info("Получаем список рейтингов");
        return mpaRepository.getAllMpa();
    }

    @Override
    public Mpa getMpaById(Long id) {
        log.info("Получаем рейтинг по айди {}", id);
        try {
            return mpaRepository.getMpaById(id).orElseThrow();
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Нет жанра по такому id");
        }
    }
}
