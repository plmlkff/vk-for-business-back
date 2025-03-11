alter table if exists card_credential
    add constraint card_credential_owner_id_fk foreign key (owner_id) references person;

alter table if exists community_group
    add constraint community_group_owner_id_fk foreign key (owner_id) references person;

alter table if exists promotion_task
    add constraint promotion_task_group_id_fk foreign key (group_id) references community_group;

alter table if exists subscription
    add constraint subscription_group_id_fk foreign key (group_id) references community_group;

alter table if exists subscription
    add constraint subscription_tariff_id_fk foreign key (tariff_id) references tariff;

alter table if exists tariff
    add constraint tariff_group_id_fk foreign key (group_id) references community_group;

alter table if exists goal
    add constraint goal_group_id_fk foreign key (group_id) references person;

alter table if exists user_authorities
    add constraint user_authorities_role_id_fk foreign key (role_id) references user_role;

alter table if exists user_to_group
    add constraint user_to_group_user_id_fk foreign key (user_id) references person;

alter table if exists user_to_group
    add constraint user_to_group_group_id_fk foreign key (group_id) references community_group;

alter table if exists user_to_role
    add constraint user_to_role_role_id_fk foreign key (role_id) references user_role;

alter table if exists user_to_role
    add constraint user_to_role_user_id_fk foreign key (user_id) references person;

