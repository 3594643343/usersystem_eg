create table if not exists user
(
    id          int auto_increment comment '用户id'
        primary key,
    account     varchar(12)   not null comment '账号',
    password    varchar(255)  not null comment '密码',
    nickname    varchar(20)   not null comment '昵称',
    gender      int           not null comment '性别（男 1 ，女 0，未知 -1）',
    role        int           not null comment '角色（超级管理员 0，管理员 1，普通用户 2）',
    create_time datetime      not null comment '创建时间',
    update_time datetime      not null comment '更新时间',
    deleted     int default 1 not null comment '逻辑删除键（未删除 1，已删除 0）'
)
    comment '用户表';


