INSERT INTO accounts (available_amount, account_type, user_id) VALUES
                     (1000, 'DEPOSIT_ACCOUNT', 1),
                     (2000, 'DEPOSIT_ACCOUNT', 2);

INSERT INTO cards (number, name, card_type, date_expire, cvc, account_id) VALUES
                     ('2511876241369548', 'Thays S Gama', 'DEBIT', '2024-08-15T00:00:00', '596', 1);