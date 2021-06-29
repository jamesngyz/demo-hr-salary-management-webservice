DROP TABLE IF EXISTS employee;

CREATE TABLE employee (
  id VARCHAR(250) PRIMARY KEY,
  login VARCHAR(250) NOT NULL UNIQUE,
  name VARCHAR(250) NOT NULL,
  salary DECIMAL NOT NULL,
  start_date DATE NOT NULL
);

--INSERT INTO employee (id, login, name, salary, start_date) VALUES
--  ('Aliko', 'Dangote', 'Billionaire Industrialist', 123.0, '2021-06-24'),
--  ('Bill', 'Gates', 'Billionaire Tech Entrepreneur', 456, '2021-06-24'),
--  ('Folrunsho', 'Alakija', 'Billionaire Oil Magnate', 789, '2021-06-24');
