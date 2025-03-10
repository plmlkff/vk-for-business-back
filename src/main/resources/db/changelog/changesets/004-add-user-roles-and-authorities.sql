INSERT INTO user_role(id, name)
VALUES (gen_random_uuid(), 'ROLE_USER');

WITH role_user AS (SELECT id FROM user_role WHERE name='ROLE_USER')
INSERT INTO user_authorities(role_id, authority)
VALUES ((SELECT id FROM role_user), 'GROUP_CREATE'),
       ((SELECT id FROM role_user), 'GROUP_VIEW'),
       ((SELECT id FROM role_user), 'GROUP_EDIT'),
       ((SELECT id FROM role_user), 'GROUP_DELETE'),
       ((SELECT id FROM role_user), 'GROUP_ADMIN'),

       ((SELECT id FROM role_user), 'GOAL_CREATE'),
       ((SELECT id FROM role_user), 'GOAL_VIEW'),
       ((SELECT id FROM role_user), 'GOAL_EDIT'),
       ((SELECT id FROM role_user), 'GOAL_DELETE'),
       ((SELECT id FROM role_user), 'GOAL_ADMIN');