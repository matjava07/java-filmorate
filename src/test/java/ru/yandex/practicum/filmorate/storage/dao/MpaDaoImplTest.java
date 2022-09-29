package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dal.MpaStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MpaDaoImplTest {

    private final MpaStorage mpaStorage;

    @Test
    void getMpaById() {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(1);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    void getAllMpa() {
        Mpa mpa1 = new Mpa(1, "G");
        Mpa mpa2 = new Mpa(2, "PG");
        Mpa mpa3 = new Mpa(3, "PG-13");
        Mpa mpa4 = new Mpa(4, "R");
        Mpa mpa5 = new Mpa(5, "NC-17");

        List<Mpa> mpaList = new ArrayList<>();
        mpaList.add(mpa1);
        mpaList.add(mpa2);
        mpaList.add(mpa3);
        mpaList.add(mpa4);
        mpaList.add(mpa5);

        assertEquals(mpaList, mpaStorage.getAllMpa());
    }
}