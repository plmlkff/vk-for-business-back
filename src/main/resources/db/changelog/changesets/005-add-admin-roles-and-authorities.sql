INSERT INTO user_role(id, name)
VALUES (gen_random_uuid(), 'ROLE_SUPER_ADMIN');

WITH role_user AS (SELECT id FROM user_role WHERE name = 'ROLE_SUPER_ADMIN')
INSERT INTO user_authorities(role_id, authority)
VALUES ((SELECT id FROM role_user), 'GROUP_ADMIN'),

       ((SELECT id FROM role_user), 'GOAL_ADMIN');