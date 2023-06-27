-- phpMyAdmin SQL Dump
-- version 4.8.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-05-2020 a las 16:33:57
-- Versión del servidor: 10.1.34-MariaDB
-- Versión de PHP: 7.2.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


CREATE TABLE `blog_posts` (
  `id` int(11) UNSIGNED NOT NULL,
  `post_author` bigint(20) unsigned NOT NULL DEFAULT 0,
  `post_date` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `post_date_gmt` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `post_content` longtext  NOT NULL,
  `post_title` text  NOT NULL,
  `post_excerpt` text  NOT NULL,
  `post_status` varchar(20)  NOT NULL DEFAULT 'publish',
  `comment_status` varchar(20)  NOT NULL DEFAULT 'open',
  `ping_status` varchar(20)  NOT NULL DEFAULT 'open',
  `post_password` varchar(255)  NOT NULL DEFAULT '',
  `post_name` varchar(200)  NOT NULL DEFAULT '',
  `to_ping` text  NOT NULL,
  `pinged` text  NOT NULL,
  `post_modified` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `post_modified_gmt` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `post_content_filtered` longtext  NOT NULL,
  `post_parent` bigint(20) unsigned NOT NULL DEFAULT 0,
  `guid` varchar(255)  NOT NULL DEFAULT '',
  `menu_order` int(11) NOT NULL DEFAULT 0,
  `post_type` varchar(20)  NOT NULL DEFAULT 'post',
  `post_mime_type` varchar(100)  NOT NULL DEFAULT '',
  `comment_count` bigint(20) NOT NULL DEFAULT 0,
  `post_description` varchar(300) not null default '',
  `lang` enum('ar','bn', 'de',
  'en', 'es', 'fa', 'fr', 'gu', 'hi', 'it', 'iw', 'ja', 'jv', 'ko', 'mr',
  'ms', 'pt', 'ru', 'ta', 'te', 'tr', 'uk', 'ur', 'vi', 'zh') not null default 'en'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

ALTER TABLE `blog_posts`
    ADD PRIMARY KEY (`id`);

ALTER TABLE `blog_posts`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1;
