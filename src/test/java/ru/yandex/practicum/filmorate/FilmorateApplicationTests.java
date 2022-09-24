package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.generate.GenerateId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.storageFilm.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.storageUser.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class FilmorateApplicationTests {

	InMemoryFilmStorage filmController;
	GenerateId generateId;
	InMemoryUserStorage userController;

	@BeforeEach
	public void main() {
		generateId = new GenerateId();
		filmController = new InMemoryFilmStorage(generateId);
		userController = new InMemoryUserStorage(generateId);
	}

	@Test
	void createFilmTest() {
		Film film = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2022, 1, 13), 20, 0, new ArrayList<>());
		assertEquals(filmController.createFilm(film), film, "Фильмы не равны");
	}

	@Test
	void updateFilmWithGoodId() {
		Film film = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2022, 1, 13), 20, 0, new ArrayList<>());

		filmController.createFilm(film);

		List<Film> filmsBefore = filmController.getFilms();

		Film filmNew = new Film(1, "Фильм", "о жизни домашних животных",
				LocalDate.of(2000, 1, 13), 120, 0, new ArrayList<>());

		filmController.updateFilm(filmNew);

		assertNotEquals(filmController.getFilms(), filmsBefore, "Фильм не обновился");
	}

	@Test
	void createUserTest() {
		User user = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей",
				LocalDate.of(2001, 5, 27), new ArrayList<>());
		assertEquals(userController.createUser(user), user, "Пользователи не равны");
	}

	@Test
	void updateUserWithGoodId() {
		User user = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей",
				LocalDate.of(2001, 5, 27), new ArrayList<>());

		userController.createUser(user);

		List<User> usersBefore = userController.getUsers();

		User userNew = new User(1, "сhikibambony@yandex.ru", "сhikibambony", "Матвей Викторович",
				LocalDate.of(2001, 5, 27), new ArrayList<>());

		userController.updateUser(userNew);

		assertNotEquals(userController.getUsers(), usersBefore, "Пользователь не обновился");
	}

}