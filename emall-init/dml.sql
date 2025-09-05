USE `user-db`;

insert into t_user(`id`, `name`, `password`, `create_user`)
values (1, 'superadmin', MD5('superadmin'), 'SYSTEM');


insert into t_role(`id`, `name`, `desc`, `create_user`)
values (1, '超级管理员角色', '超级管理员角色', 'SYSTEM');


insert into t_permission(`id`, `name`, `path`, `operation`, `desc`, `create_user`)
values (1, '所有权限', '/**', 'ALL', '所有权限', 'SYSTEM');


insert into t_user_role(`user_id`, `role_id`, `create_user`)
values (1, 1, 'SYSTEM');


insert into t_role_permission(`role_id`, `permission_id`, `create_user`)
values (1, 1, 'SYSTEM');