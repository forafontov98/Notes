/*Главная таблица*/
CREATE TABLE NOTES (
ID INT PRIMARY KEY AUTO_INCREMENT,
NAME VARCHAR(30),
Type TINYINT,
CreatedAt DATE NOT NULL DEFAULT NOW(),
);

CREATE TABLE IMAGE_NOTE (
ID INT PRIMARY KEY AUTO_INCREMENT,
WAY VARCHAR (3500),
NOTES_ID INT,
FOREIGN KEY (NOTES_ID) REFERENCES USERS(ID) ON DELETE CASCADE
);


CREATE TABLE TEXT_NOTE (
ID INT PRIMARY KEY AUTO_INCREMENT,
BODY VARCHAR(300),
NOTES_ID INT,
FOREIGN KEY (NOTES_ID) REFERENCES USERS(ID) ON DELETE CASCADE
);


CREATE TABLE TODO_NOTE (
ID INT PRIMARY KEY AUTO_INCREMENT,
TASK_NAME varchar (30),
label BOOL,
NOTES_ID INT,
FOREIGN KEY (NOTES_ID) REFERENCES USERS(ID) ON DELETE CASCADE
);


INSERT INTO NOTES (NAME, Type)
VALUES ('ЗАметка 1', 1);

INSERT INTO NOTES (NAME, Type)
VALUES ('ЗАметка 2', 2);

INSERT INTO NOTES (NAME, Type)
VALUES ('ЗАметка 3', 3);


INSERT INTO IMAGE_NOTE (WAY, NOTES_ID)
values ('D:\6 семестр\Web\Screenshot_1.png', 1);

INSERT INTO TEXT_NOTE (BODY, NOTES_ID)
values ('Какой-то текст', 2);

INSERT INTO TODO_NOTE (TASK_NAME, label, NOTES_ID)
values ('Задача 1', 1 , 3);

INSERT INTO TODO_NOTE (TASK_NAME, label, NOTES_ID)
values ('Задача 2', 1 , 3);

INSERT INTO TODO_NOTE (TASK_NAME, label, NOTES_ID)
values ('Задача 3', 1 , 3);

