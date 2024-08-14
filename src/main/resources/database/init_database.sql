DROP TABLE IF EXISTS borrow;
DROP TABLE IF EXISTS member;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
                        id SERIAL NOT NULL,
                        first_name VARCHAR(30) NOT NULL,
                        last_name VARCHAR(30) NOT NULL,
                        PRIMARY KEY (id)
);

CREATE TABLE book (
                      id SERIAL NOT NULL,
                      title VARCHAR(100) NOT NULL,
                      amount INT NOT NULL DEFAULT 1 CHECK(amount >= 0),
                      author_id INT NOT NULL,
                      PRIMARY KEY (id),
                      FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE RESTRICT
);

CREATE TABLE member (
                        id SERIAL NOT NULL,
                        first_name VARCHAR(30) NOT NULL,
                        last_name VARCHAR(30) NOT NULL,
                        membership_date DATE,
                        PRIMARY KEY (id)
);

CREATE TABLE borrow (
                        id SERIAL NOT NULL,
                        borrow_date DATE NOT NULL,
                        book_id INT NOT NULL,
                        member_id INT NOT NULL,
                        PRIMARY KEY (id),
                        FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE RESTRICT,
                        FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE RESTRICT
);