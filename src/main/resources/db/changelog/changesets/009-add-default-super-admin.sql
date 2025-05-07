INSERT INTO person(id, first_name, password, surname, login)
VALUES (gen_random_uuid(), 'admin', '$2a$10$Y2aQ0HtzOWXqWvPDbqSlSuLXSERmK7H9qlEP1cqGm99vSR4qhwuUi', 'admin', 'admin');

WITH admin_role AS (SELECT id FROM user_role WHERE name='ROLE_SUPER_ADMIN'),
    admin AS (SELECT * FROM person WHERE login='admin')
INSERT INTO user_to_role(role_id, user_id)
VALUES ((SELECT id FROM admin_role), (SELECT id FROM admin));
