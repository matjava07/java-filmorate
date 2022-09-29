CREATE TABLE IF NOT EXISTS users (
                                     user_id int generated by default as identity primary key,
                                     email varchar(40) NOT NULL,
                                     login varchar(40) NOT NULL,
                                     user_name varchar(100),
                                     birthday date CHECK (birthday < CURDATE())
);

create unique index if not exists USER_EMAIL_UINDEX on USERS (email);
create unique index if not exists USER_LOGIN_UINDEX on USERS (login);

CREATE TABLE IF NOT EXISTS friends (
                                       first_user_id integer REFERENCES users(user_id),
                                       second_user_id integer REFERENCES users(user_id),
                                       PRIMARY KEY(first_user_id, second_user_id)
);

CREATE TABLE IF NOT EXISTS motion_picture_association (
                                                          mpa_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                                          mpa_name varchar(10)
);

CREATE TABLE IF NOT EXISTS films (
                                     film_id bigint NOT NULL AUTO_INCREMENT,
                                     title varchar(40) NOT NULL,
                                     description varchar(200),
                                     release_date date CHECK (release_date > CAST('1895-12-28' AS date)),
                                     duration integer CHECK (duration > 0),
                                     motion_picture_association_id integer REFERENCES motion_picture_association(mpa_id),
                                     rate integer DEFAULT 0,
                                     PRIMARY KEY (film_id)
);

CREATE TABLE IF NOT EXISTS genres (
                                      genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                      genre_name varchar(40)
);

CREATE TABLE IF NOT EXISTS film_genre (
                                          film_id integer REFERENCES films(film_id),
                                          genre_id integer REFERENCES genres(genre_id),
                                          PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS likes (
                                     film_id integer REFERENCES films(film_id),
                                     user_id integer REFERENCES users(user_id),
                                     PRIMARY KEY(film_id, user_id)
);