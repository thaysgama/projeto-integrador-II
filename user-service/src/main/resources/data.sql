-- password: "aaaaaA1@"
INSERT INTO users (first_name, surname, cpf, phone_number, username, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
VALUES    ('Thays','Gama','000.000.000-00', '85999999999','thays@gmail.com', '$2a$10$1.iUctStoe.qGOrzoorpAufoceAF63KOx7D9cm7tFtQfB.wut1iYG', 1, 1, 1, 1),
          ('Silas', 'Medeiros', '000.000.000-00', '21999999999','itunes.gaming123@gmail.com', '$2a$10$1.iUctStoe.qGOrzoorpAufoceAF63KOx7D9cm7tFtQfB.wut1iYG', 1, 1, 1, 1);

INSERT INTO roles VALUES
    (null, 'ROLE_ADMIN'),
    (null, 'ROLE_CLIENT');

INSERT INTO user_role VALUES
                     (1, 1),
                     (2, 2);