package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	FilmController filmController;
	GenerateId generateId;
	UserController userController;

	@BeforeEach
	public void main() {
		generateId = new GenerateId();
		filmController = new FilmController(generateId);
		userController = new UserController(generateId);
	}

	@Test
	void createFilmTest() {
		Film film = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2022, 1, 13), 20);
		assertEquals(filmController.createFilm(film), film, "Фильмы не равны");
	}

	@Test
	void createFilmWithBadData() {
		Film film = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(1800, 1, 13), 20);

		ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmController.createFilm(film)
		);

		assertEquals("Не удалось добавить фильм", exception.getMessage());
	}

	@Test
	void updateFilmWithBadId() {
		Film film = new Film(-1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2022, 1, 13), 20);

		ValidationException exception = assertThrows(
				ValidationException.class,
				() -> filmController.updateFilm(film)
		);

		assertEquals("Не удалось обновить фильм", exception.getMessage());
	}

	@Test
	void updateFilmWithGoodId() {
		Film film = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2022, 1, 13), 20);

		filmController.createFilm(film);

		List<Film> filmsBefore = filmController.getFilms();

		Film filmNew = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2000, 1, 13), 120);

		filmController.updateFilm(filmNew);

		assertNotEquals(filmController.getFilms(), filmsBefore, "Фильм не обновился");
	}

	@Test
	void createUserTest() {
		User user = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей",
				LocalDate.of(2001, 5, 27));
		assertEquals(userController.createUser(user), user, "Пользователи не равны");
	}

	@Test
	void updateUserWithBadId() {
		User user = new User(-1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей",
				LocalDate.of(2023, 5, 27));

		ValidationException exception = assertThrows(
				ValidationException.class,
				() -> userController.updateUser(user)
		);

		assertEquals("Не удалось обновить пользователя", exception.getMessage());
	}

	@Test
	void updateUserWithGoodId() {
		User user = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей",
				LocalDate.of(2001, 5, 27));

		userController.createUser(user);

		List<User> usersBefore = userController.getUsers();

		User userNew = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей Викторович",
				LocalDate.of(2001, 5, 27));

		userController.updateUser(userNew);

		assertNotEquals(userController.getUsers(), usersBefore, "Пользователь не обновился");
	}

}
