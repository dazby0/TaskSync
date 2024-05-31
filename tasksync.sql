-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 31 Maj 2024, 10:53
-- Wersja serwera: 10.4.10-MariaDB
-- Wersja PHP: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `tasksync`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tasks`
--

CREATE TABLE `tasks` (
  `task_id` int(11) NOT NULL,
  `status` varchar(40) DEFAULT NULL,
  `assigned_to` varchar(40) DEFAULT NULL,
  `assigned_date` datetime DEFAULT NULL,
  `time_spent` varchar(8) DEFAULT NULL,
  `task_title` varchar(40) DEFAULT NULL,
  `assigned_by` varchar(40) DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `tasks`
--

INSERT INTO `tasks` (`task_id`, `status`, `assigned_to`, `assigned_date`, `time_spent`, `task_title`, `assigned_by`, `finish_date`, `start_date`) VALUES
(1, 'In Progress', 'steveluki', '2024-05-31 10:28:52', NULL, 'Create Login/Register pages', 'jonsnow', NULL, '2024-05-31 10:36:38'),
(2, 'Finished', 'oscarbed', '2024-05-31 10:29:19', '00:09:46', 'Create DB structure ', 'jonsnow', '2024-05-31 10:47:00', '2024-05-31 10:37:14'),
(3, 'In Progress', 'doejohn', '2024-05-31 10:30:21', NULL, 'Config Stripe to project', 'jonsnow', NULL, '2024-05-31 10:47:32'),
(4, 'New', 'merryjerry', '2024-05-31 10:30:43', NULL, 'Code Review', 'jonsnow', NULL, NULL),
(5, 'New', 'steveluki', '2024-05-31 10:31:15', NULL, 'Design Reports Page', 'jonsnow', NULL, NULL),
(6, 'Finished', 'doejohn', '2024-05-31 10:31:42', '00:11:39', 'Switch Firebase to Mongo', 'jonsnow', '2024-05-31 10:47:38', '2024-05-31 10:35:59'),
(7, 'In Progress', 'merryjerry', '2024-05-31 10:31:59', NULL, 'Finish Figma Design', 'jonsnow', NULL, '2024-05-31 10:36:55'),
(8, 'New', 'doejohn', '2024-05-31 10:32:19', NULL, 'Do benchmarks', 'jonsnow', NULL, NULL),
(9, 'In Progress', 'oscarbed', '2024-05-31 10:33:03', NULL, 'Create admin pages', 'jonsnow', NULL, '2024-05-31 10:37:20');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `teams`
--

CREATE TABLE `teams` (
  `team_id` int(11) NOT NULL,
  `manager` varchar(40) DEFAULT NULL,
  `workers` varchar(100) DEFAULT NULL,
  `team_name` varchar(40) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `teams`
--

INSERT INTO `teams` (`team_id`, `manager`, `workers`, `team_name`) VALUES
(1, 'jonsnow', 'oscarbed, doejohn', 'Backend'),
(2, 'jonsnow', 'oscarbed, steveluki, doejohn', 'Frontend'),
(3, 'jonsnow', 'merryjerry, steveluki', 'UI/UX');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `role` varchar(45) DEFAULT NULL,
  `team_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `role`, `team_id`) VALUES
(1, 'jonsnow', 'jonny123', 'Manager', NULL),
(2, 'doejohn', '123doe', 'Worker', NULL),
(3, 'merryjerry', 'merry123', 'Worker', NULL),
(4, 'steveluki', 'luki123', 'Worker', NULL),
(5, 'oscarbed', 'oscar123', 'Worker', NULL);

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `tasks`
--
ALTER TABLE `tasks`
  ADD PRIMARY KEY (`task_id`);

--
-- Indeksy dla tabeli `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`team_id`);

--
-- Indeksy dla tabeli `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD KEY `idx_team_id` (`team_id`);

--
-- AUTO_INCREMENT dla tabel zrzutów
--

--
-- AUTO_INCREMENT dla tabeli `tasks`
--
ALTER TABLE `tasks`
  MODIFY `task_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT dla tabeli `teams`
--
ALTER TABLE `teams`
  MODIFY `team_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT dla tabeli `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
