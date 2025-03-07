-- auto-generated definition
create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    username     varchar(256)                       null comment '用户名',
    userAccount  varchar(256)                       null comment '用户账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       not null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 not null comment '用户状态',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 null comment '0表示未删除，1表示已经删除',
    userRole     int      default 0                 not null comment '0-普通用户，1-管理员',
    planetCode   varchar(512)                       null comment '星球编号'
)
    comment '用户表';
