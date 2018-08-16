create database hasaki;

use hasaki;

create table `user` (
id int(11) not null primary key auto_increment,
account varchar(255) not null default '',
reg_type varchar(10) not null default '',
create_time timestamp not null default current_timestamp,
update_time timestamp not null default current_timestamp on update current_timestamp
);

create table `user_token` (
id int(11) not null primary key auto_increment,
user_id int(11) not null,
token varchar(255) not null default '',
create_time timestamp not null default current_timestamp,
update_time timestamp not null default current_timestamp on update current_timestamp,
unique key `user_id_index`(`user_id`),
unique key `token_index`(`token`)
);