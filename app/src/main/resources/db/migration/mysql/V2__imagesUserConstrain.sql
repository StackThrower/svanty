SET SQL_MODE='ALLOW_INVALID_DATES';
SET FOREIGN_KEY_CHECKS=0;
alter table images add constraint FK13ljqfrfwbyvnsdhihwta8cpr foreign key (user_id) references users (id);