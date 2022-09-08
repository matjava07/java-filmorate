package ru.yandex.practicum.filmorate.exception;

public class ObjectExistenceException extends RuntimeException{
    public ObjectExistenceException(final String message) {
        super(message);
    }
}
