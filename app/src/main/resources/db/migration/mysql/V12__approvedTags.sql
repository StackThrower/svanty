
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

ALTER TABLE tags ADD COLUMN approved enum('yes','no') NOT NULL DEFAULT 'no';