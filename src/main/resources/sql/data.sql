delete from user where account = 'admin';

insert into user (account, password, nickname, gender, role, create_time, update_time, deleted)
values  ('admin', '123', 'nihao', 0, 0, '2024-07-17 12:46:02', '2024-07-17 16:53:45', 1);