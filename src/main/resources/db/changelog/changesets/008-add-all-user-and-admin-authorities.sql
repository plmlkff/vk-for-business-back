WITH role_user AS (SELECT id FROM user_role WHERE name='ROLE_USER'),
     role_admin AS (SELECT id FROM user_role WHERE name='ROLE_SUPER_ADMIN')
INSERT INTO user_authorities(role_id, authority)
VALUES ((SELECT id FROM role_user), 'PROMOTION_TASK_CREATE'),
       ((SELECT id FROM role_user), 'PROMOTION_TASK_VIEW'),
       ((SELECT id FROM role_user), 'PROMOTION_TASK_EDIT'),
       ((SELECT id FROM role_user), 'PROMOTION_TASK_DELETE'),
       ((SELECT id FROM role_admin), 'PROMOTION_TASK_ADMIN'),

       ((SELECT id FROM role_user), 'SUBSCRIPTION_CREATE'),
       ((SELECT id FROM role_user), 'SUBSCRIPTION_VIEW'),
       ((SELECT id FROM role_user), 'SUBSCRIPTION_EDIT'),
       ((SELECT id FROM role_user), 'SUBSCRIPTION_DELETE'),
       ((SELECT id FROM role_admin), 'SUBSCRIPTION_ADMIN'),

       ((SELECT id FROM role_user), 'TARIFF_CREATE'),
       ((SELECT id FROM role_user), 'TARIFF_VIEW'),
       ((SELECT id FROM role_user), 'TARIFF_EDIT'),
       ((SELECT id FROM role_user), 'TARIFF_DELETE'),
       ((SELECT id FROM role_admin), 'TARIFF_ADMIN'),

       ((SELECT id FROM role_user), 'TRANSACTION_CREATE'),
       ((SELECT id FROM role_admin), 'TRANSACTION_ADMIN'),

       ((SELECT id FROM role_user), 'USER_CREATE'),
       ((SELECT id FROM role_user), 'USER_VIEW'),
       ((SELECT id FROM role_user), 'USER_EDIT'),
       ((SELECT id FROM role_user), 'USER_DELETE'),
       ((SELECT id FROM role_admin), 'USER_ADMIN'),

       ((SELECT id FROM role_user), 'USER_ROLE_CREATE'),
       ((SELECT id FROM role_user), 'USER_ROLE_VIEW'),
       ((SELECT id FROM role_user), 'USER_ROLE_EDIT'),
       ((SELECT id FROM role_user), 'USER_ROLE_DELETE'),
       ((SELECT id FROM role_admin), 'USER_ROLE_ADMIN');