package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film createFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getFilms();

    Optional<Film> getFilmById(Integer filmId);
}
