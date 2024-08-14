INSERT INTO author (first_name, last_name)
VALUES
    ('Mona', 'Kasten'),
    ('Tess', 'Gerritsen'),
    ('Rebecca', 'Yaros'),
    ('Edvard', 'Reserford'),
    ('Holly', 'Black');

INSERT INTO book (title, author_id)
VALUES
    ('Maxton Hall. Book I. Save me', 1),
    ('Garden of bones', 2),
    ('Iron flame. Book 2', 3),
    ('New York. The city of contrasts is the city of opportunities', 4),
    ('The book of the night', 5);

INSERT INTO member (first_name, last_name, membership_date)
VALUES
    ('Tom', 'Halec', '2024-08-13'),
    ('Andrew', 'Kurt', '2024-07-02'),
    ('Molly', 'Fisher', '2024-08-09');

INSERT INTO borrow (borrow_date, book_id, member_id)
VALUES
    ('2024-08-10', 2, 2),
    ('2024-08-10', 4, 2),
    ('2024-08-13', 1, 1),
    ('2024-08-13', 5, 3);