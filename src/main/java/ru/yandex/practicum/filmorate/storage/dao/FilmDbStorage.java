package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.exception.ObjectExcistenceException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dal.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {

        String sql =
                "insert into FILMS (TITLE, DESCRIPTION, RELEASE_DATE, DURATION, MOTION_PICTURE_ASSOCIATION_ID) " +
                        "values(?, ?, ?, ?, ?)";
        Integer id = (int) insertFilm(film, sql);
        film.setId(id);
        film.setGenres(deleteDublicateGenre(film));
        insertFilmGenre(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql =
                "update FILMS set TITLE = ?," +
                        " DESCRIPTION = ?," +
                        " RELEASE_DATE = ?," +
                        " DURATION = ?," +
                        " MOTION_PICTURE_ASSOCIATION_ID = ?" +
                        " where FILM_ID = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());


        String sqlForUpdateGenre = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlForUpdateGenre, film.getId());
        film.setGenres(deleteDublicateGenre(film));
        insertFilmGenre(film);

        return getFilmById(film.getId()).orElseThrow(() -> new ObjectExcistenceException("Фильм не найден"));
    }

    @Override
    public List<Film> getFilms() {
        String sqlForFilms =
                "select * from FILMS F join MOTION_PICTURE_ASSOCIATION M on F.MOTION_PICTURE_ASSOCIATION_ID = M.MPA_ID";

        return jdbcTemplate.query(sqlForFilms, FilmDbStorage::makeFilm);
    }

    @Override
    public Optional<Film> getFilmById(Integer filmId) {
        String sql =
                "select * " +
                        "from FILMS F " +
                        "join MOTION_PICTURE_ASSOCIATION MPA on MPA.MPA_ID = F.MOTION_PICTURE_ASSOCIATION_ID " +
                        "where F.FILM_ID = ?";

        List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm, filmId);

        if (films.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(films.get(0));
    }

    @Override
    public List<Film> getTopFilms(Integer numberForTop) {
        String sql = "select * " +
                "from films " +
                "join MOTION_PICTURE_ASSOCIATION MPA on MPA.MPA_ID = FILMS.MOTION_PICTURE_ASSOCIATION_ID " +
                "order by rate desc " +
                "limit ?";

        return jdbcTemplate.query(sql, FilmDbStorage::makeFilm, numberForTop);
    }

    private static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                rs.getInt("rate"));
    }

    private void insertFilmGenre(Film film) {
        String sqlForGenre = "insert into FILM_GENRE(film_id, genre_id) values (?, ?)";

        jdbcTemplate.batchUpdate(sqlForGenre, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                Genre genre = new ArrayList<>(film.getGenres()).get(i);
                ps.setLong(1, film.getId());
                ps.setLong(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres().size();
            }
        });
    }

    private long insertFilm(Film film, String sql) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    private List<Genre> deleteDublicateGenre(Film film) {
        List<Genre> genres = new ArrayList<>();
        for (Genre newGenre: film.getGenres()){
            if (!genres.contains(newGenre)){
                genres.add(newGenre);
            }
        }
        return genres;
    }
}

