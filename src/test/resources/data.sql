-- passwords are in the format: password<UserLetter>123
INSERT INTO local_user (email, first_name, last_name, password, username, email_verified)
VALUES ('UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', '$2a$10$k4TiyYwpe1IoVszLpLa6W.hFbWIkOweayvTVaXX44hUAoHpOd1l3e', 'UserA', TRUE),
       ('UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', '$2a$10$xnpOv4oKwwfhEnvf6fyle.sHDu8k0A2sIHkNQjxnkyeZMelX1LKJu', 'UserB', FALSE);
