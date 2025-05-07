WITH role_user AS (SELECT id FROM user_role WHERE name='ROLE_USER'),
     role_admin AS (SELECT id FROM user_role WHERE name='ROLE_SUPER_ADMIN')
INSERT INTO user_authorities(role_id, authority)
VALUES ((SELECT id FROM role_user), 'CARD_CREDENTIAL_CREATE'),
       ((SELECT id FROM role_user), 'CARD_CREDENTIAL_VIEW'),
       ((SELECT id FROM role_user), 'CARD_CREDENTIAL_EDIT'),
       ((SELECT id FROM role_user), 'CARD_CREDENTIAL_DELETE'),
       ((SELECT id FROM role_admin), 'CARD_CREDENTIAL_ADMIN');