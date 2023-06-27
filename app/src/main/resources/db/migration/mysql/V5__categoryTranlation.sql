
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE `category_translations` (
  `id` int(11) UNSIGNED NOT NULL,
  `name` varchar(255) NOT NULL,
  `category_id` int(11) UNSIGNED NOT NULL,
  `lang` enum('ar','bn', 'de',
  'en', 'es', 'fa', 'fr', 'gu', 'hi', 'it', 'iw', 'ja', 'jv', 'ko', 'mr',
  'ms', 'pt', 'ru', 'ta', 'te', 'tr', 'uk', 'ur', 'vi', 'zh') not null default 'en'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `category_translations`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `category_translations`
  MODIFY `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;

alter table category_translations add constraint FK13ljqfrfwbyvnsdhihwta8cprCT foreign key (category_id) references categories (id);