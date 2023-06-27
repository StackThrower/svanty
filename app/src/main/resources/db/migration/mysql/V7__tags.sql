
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE `tags` (
  `id` int(11) UNSIGNED NOT NULL,
  `slug` varchar(512) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `tags`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `tags`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;


CREATE TABLE `tags_translations` (
  `id` int(11) UNSIGNED NOT NULL,
  `title` varchar(512) NOT NULL,
  `description` varchar(512) NOT NULL,
  `pre_text` text  NOT NULL,
  `main_text` text  NOT NULL,
  `tag_id` int(11) UNSIGNED NOT NULL,
  `lang` enum('ar','bn', 'de',
  'en', 'es', 'fa', 'fr', 'gu', 'hi', 'it', 'iw', 'ja', 'jv', 'ko', 'mr',
  'ms', 'pt', 'ru', 'ta', 'te', 'tr', 'uk', 'ur', 'vi', 'zh') not null default 'en'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `tags_translations`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `tags_translations`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

alter table tags_translations add constraint FK13ljqfrfwbyvnsdhihwta8cprTGTR foreign key (tag_id) references tags (id);