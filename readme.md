## ToDo List by Yaroslav Dyachenko
Spring Framework, Spring Data, Hibernate, H2, Spring Security, H2, java script, jQuery, AJAX etc. JUnit, Spring-test for testing.

## Project on herokuapp.com:
dyachenkoytodolist.herokuapp.com

## Sql queries:
/*-----------------------1*/

SELECT DISTINCT status FROM tasks ORDER BY status ASC;

/*-----------------------2*/

SELECT COUNT(tasks.id) as tasks_count, projects.name
FROM tasks RIGHT JOIN projects
  ON tasks.project_id = projects.id
GROUP BY projects.id
ORDER BY tasks_count DESC;

/*-----------------------3*/

SELECT COUNT(tasks.id) as tasks_count, projects.name
FROM tasks RIGHT JOIN projects
  ON tasks.project_id = projects.id
GROUP BY projects.name
ORDER BY projects.name;

/*-----------------------4*/

SELECT tasks.name as task_name, projects.name as project_name
FROM tasks INNER JOIN projects
  ON tasks.project_id=projects.id
WHERE tasks.name LIKE 'N%';

/*-----------------------5*/

SELECT projects.name as project_name, COUNT(tasks.id) as tasks_count
FROM projects LEFT JOIN tasks
  ON tasks.project_id=projects.id
WHERE projects.name LIKE '_%a%_'
GROUP BY projects.name;

/*-----------------------6*/

SELECT name FROM (
  SELECT COUNT(name) as counted, name
FROM tasks
  GROUP BY name
) as custom
WHERE counted>1
ORDER BY name;

/*-----------------------7*/

SELECT tasks.name as task_name, tasks.status as task_status, COUNT(tasks.id) as tasks_count
FROM tasks RIGHT JOIN projects
  ON tasks.project_id=projects.id
WHERE projects.name = 'Garage'
GROUP BY tasks.name, tasks.status
HAVING COUNT(tasks_count)>1
ORDER BY tasks_count;


/*-----------------------8*/

SELECT projects.name as p_name, COUNT(tasks.status)
FROM projects RIGHT JOIN tasks
ON projects.id = tasks.project_id
WHERE status = 'COMPLETED'
GROUP BY p_name
HAVING COUNT(tasks.status)>10
ORDER BY projects.id;
