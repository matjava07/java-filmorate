package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dal.LikeStorage;
import ru.yandex.practicum.filmorate.storage.dal.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmDbStorageTest {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    @Test
    void createFilm() {
        Film film = new Film();
        film.setId(1);
        film.setName("Gladiator");
        film.setDescription("Kill bad boys");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2005, 6, 23));
        Genre genre = new Genre(1, "Комедия");
        List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        film.setGenres(genres);
        Mpa mpa = new Mpa(1, "G");
        film.setMpa(mpa);

        Film filmNew = filmStorage.createFilm(film);

        assertEquals(film, filmNew);

    }

    @Test
    void updateFilm() {

        Film film = new Film();
        film.setId(1);
        film.setName("Gladiator");
        film.setDescription("Kill bad boys");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2005, 6, 23));
        Mpa mpa = new Mpa(1, "G");
        film.setMpa(mpa);

        filmStorage.createFilm(film);

        Film filmNew = new Film();
        filmNew.setId(1);
        filmNew.setName("Robot ubiyca");
        filmNew.setDescription("Kill bad boys");
        filmNew.setDuration(120);
        filmNew.setReleaseDate(LocalDate.of(2005, 6, 23));
        filmNew.setMpa(mpa);

        Film filmNewAfterUpdate = filmStorage.updateFilm(filmNew);

        assertEquals(filmNew, filmNewAfterUpdate);
    }

    @Test
    void getFilms() {
        Film film = new Film();
        film.setId(1);
        film.setName("Gladiator");
        film.setDescription("Kill bad boys");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2005, 6, 23));
        Mpa mpa = new Mpa(1, "G");
        film.setMpa(mpa);

        filmStorage.createFilm(film);

        List<Film> films = new ArrayList<>();
        films.add(film);

        assertEquals(films, filmStorage.getFilms());

    }

    @Test
    void getFilmById() {
        Film filmNew = new Film();
        filmNew.setId(1);
        filmNew.setName("Gladiator");
        filmNew.setDescription("Kill bad boys");
        filmNew.setDuration(120);
        filmNew.setReleaseDate(LocalDate.of(2005, 6, 23));
        Mpa mpa = new Mpa(1, "G");
        filmNew.setMpa(mpa);

        filmStorage.createFilm(filmNew);

        Optional<Film> filmOptional = filmStorage.getFilmById(1);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getTopFilms() {
        Film film = new Film();
        film.setId(1);
        film.setName("Gladiator");
        film.setDescription("Kill bad boys");
        film.setDuration(120);
        film.setReleaseDate(LocalDate.of(2005, 6, 23));
        Mpa mpa = new Mpa(1, "G");
        film.setMpa(mpa);
        film.setCountLikes(1);

        Film filmNew = new Film();
        filmNew.setId(2);
        filmNew.setName("Robot ubiyca");
        filmNew.setDescription("Kill bad boys");
        filmNew.setDuration(120);
        filmNew.setReleaseDate(LocalDate.of(2005, 6, 23));
        filmNew.setMpa(mpa);

        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(filmNew);

        filmStorage.createFilm(film);
        filmStorage.createFilm(filmNew);

        User userNew = new User();
        userNew.setId(1);
        userNew.setName("Matvey");
        userNew.setEmail("matjava07@yandex.ru");
        userNew.setLogin("Mat");
        userNew.setBirthday(LocalDate.of(2001, 5, 27));

        userStorage.createUser(userNew);
        likeStorage.addLikeToFilm(1, 1);

        assertEquals(films, filmStorage.getTopFilms(2));
    }
}