-- Table:users
DROP TABLE IF EXISTS users;
CREATE TABLE users(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

-- Table:roles
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(100) NOT NULL
);

-- Table for mapping user and his roles user_roles
DROP TABLE IF EXISTS user_roles;
CREATE TABLE user_roles(
  user_id INT NOT NULL ,
  role_id INT NOT NULL ,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id),

  UNIQUE (user_id, role_id)
);

-- Table: projects
DROP TABLE IF EXISTS projects;
CREATE TABLE projects(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(100) NOT NULL,
  user_id INT NOT NULL,
  CONSTRAINT projects_users_id_fk
  FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Table: tasks
DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks(
  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(100) NOT NULL ,
  status INT,
  position INT,
  project_id INT NOT NULL,
  deadline VARCHAR(20),
  CONSTRAINT tasks_projects_id_fk
  FOREIGN KEY (project_id) REFERENCES projects (id)
);

-- Insert data Стандартные данные при запуске
INSERT INTO users (id, username, password) VALUES (1, 'Yaroslav', '$2a$11$sbdl/QdAA7hsLuF6GPiUKuEciZ2JONmNEqOKWvuHq.movfQoy6P/e');

INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1,2);

INSERT INTO projects (id, name, user_id) VALUES (1, 'Мой первый проэкт', 1);
INSERT INTO tasks (id, name, status, position, project_id, deadline) VALUES (1, 'Помыть машину', 0, 1, 1, '09.01.2017');
