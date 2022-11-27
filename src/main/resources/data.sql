-- seed articles in database
INSERT INTO doctor (id, name) VALUES (1, 'J. Van Soom');
INSERT INTO doctor (id, name) VALUES (2, 'S. Nouri');

-- Create user table
CREATE TABLE users (
           `idUsers` int NOT NULL,
           `name` varchar(45) DEFAULT NULL,
           `password` varchar(100) DEFAULT NULL,
           `enabled` int DEFAULT NULL,
           `role` varchar(45) DEFAULT NULL, PRIMARY KEY (`idUsers`));

-- insert users
    INSERT INTO users (idUsers,name,password,enabled,role)
    VALUES (1, 'admin', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe', 1, 'ROLE_ADMIN');
    INSERT INTO users (idUsers,name,password,enabled,role)
    VALUES (2, 'guest', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe' ,1, 'ROLE_GUEST');
    INSERT INTO users (idUsers,name, password,enabled,role)
    VALUES (3, 'doctor', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe', 1, 'ROLE_DOCTOR');
    INSERT INTO users (idUsers,name,password,enabled,role)
    VALUES (4, 'patient', '$2a$12$xd5Q4DmSz0jJ7YIozfAz3.oc0TfN.QKk.qb/ZxBk6jKlLQtkd7eoe', 1, 'ROLE_PATIENT');

