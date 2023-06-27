
SET SQL_MODE='ALLOW_INVALID_DATES';
SET FOREIGN_KEY_CHECKS=0;

alter table images add constraint FKia8eddwnccq83tu3u38o54pqxR foreign key (categories_id) references categories (id);
