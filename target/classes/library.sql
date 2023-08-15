-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 21, 2023 at 04:39 PM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
  `au_id` int(11) NOT NULL,
  `au_fullname` varchar(100) DEFAULT NULL,
  `au_introduce` text DEFAULT NULL,
  `au_image` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `bo_id` int(11) NOT NULL,
  `bo_title` varchar(200) DEFAULT NULL,
  `bo_publish_year` int(11) DEFAULT NULL,
  `bo_quantity` int(11) DEFAULT NULL,
  `bo_description` text DEFAULT NULL,
  `bo_image` varchar(200) DEFAULT NULL,
  `bo_create_date` date DEFAULT NULL,
  `ca_id` int(11) DEFAULT NULL,
  `pu_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`bo_id`, `bo_title`, `bo_publish_year`, `bo_quantity`, `bo_description`, `bo_image`, `bo_create_date`, `ca_id`, `pu_id`) VALUES
(1, 'The Great Gatsby', 1925, 5, 'A novel by F. Scott Fitzgerald', 'thegreatgatsby.jpg', '2021-07-21', 1, 1),
(2, 'To Kill a Mockingbird', 1960, 3, 'A novel by Harper Lee', 'tokillamockingbird.jpg', '2021-07-22', 2, 2),
(3, '1984', 1949, 7, 'A dystopian novel by George Orwell', '1984.jpg', '2021-07-23', 3, 3),
(4, 'Pride and Prejudice', 1813, 2, 'A novel by Jane Austen', 'prideandprejudice.jpg', '2021-07-24', 4, 4),
(5, 'The Catcher in the Rye', 1951, 4, 'A novel by J.D. Salinger', 'thecatcherintherye.jpg', '2021-07-25', 5, 5),
(6, 'Brave New World', 1932, 6, 'A dystopian novel by Aldous Huxley', 'bravenewworld.jpg', '2021-07-26', 6, 6),
(7, 'The Hobbit', 1937, 1, 'A fantasy novel by J.R.R. Tolkien', 'thehobbit.jpg', '2021-07-27', 7, 7),
(8, 'The Lord of the Rings', 1954, 8, 'A trilogy by J.R.R. Tolkien', 'thelordoftherings.jpg', '2021-07-28', 7, 7),
(9, 'Animal Farm', 1945, 9, 'A novella by George Orwell', 'animalfarm.jpg', '2021-07-29', 3, 3),
(10, 'The Alchemist', 1988, 10, 'A novel by Paulo Coelho', 'thealchemist.jpg', '2021-07-30', 8, 8);

-- --------------------------------------------------------

--
-- Table structure for table `book_authors`
--

CREATE TABLE `book_authors` (
  `ba_id` int(11) NOT NULL,
  `bo_id` int(11) DEFAULT NULL,
  `au_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `ca_id` int(11) NOT NULL,
  `ca_name` varchar(100) DEFAULT NULL,
  `ca_description` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`ca_id`, `ca_name`, `ca_description`) VALUES
(1, 'Classic Literature', 'Novels and other works that are considered to be of high literary quality and have stood the test of time.'),
(2, 'Modern Literature', 'Novels and other works that were published in the 20th century or later.'),
(3, 'Dystopian Fiction', 'Novels that depict a fictional future society that is dehumanizing and unpleasant.'),
(4, 'Romance', 'Fictional stories about romantic love and relationships.'),
(5, 'Coming of Age', 'Novels that focus on the growth and development of young protagonists.'),
(6, 'Science Fiction', 'Novels that explore the potential consequences of scientific and other innovations.'),
(7, 'Fantasy', 'Novels that are set in imaginary worlds that are often inspired by the Middle Ages.'),
(8, 'Philosophical Fiction', 'Novels that explore philosophical ideas and themes, often through fictional narratives.');

-- --------------------------------------------------------

--
-- Table structure for table `loans`
--

CREATE TABLE `loans` (
  `lo_id` int(11) NOT NULL,
  `lo_no_of_date` int(11) DEFAULT NULL,
  `lo_create_date` date DEFAULT NULL,
  `us_id` int(10) UNSIGNED DEFAULT NULL,
  `bo_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `loans`
--

INSERT INTO `loans` (`lo_id`, `lo_no_of_date`, `lo_create_date`, `us_id`, `bo_id`) VALUES
(1, 14, '2021-08-01', 1, 1),
(2, 7, '2021-08-02', 2, 2),
(3, 21, '2021-08-03', 3, 3),
(4, 14, '2021-08-04', 4, 4),
(5, 7, '2021-08-05', 5, 5),
(6, 21, '2021-08-06', 6, 6),
(7, 14, '2021-08-07', 7, 7),
(8, 7, '2021-08-08', 8, 8),
(9, 21, '2021-08-09', 9, 1),
(10, 14, '2021-08-10', 10, 2);

-- --------------------------------------------------------

--
-- Table structure for table `publishers`
--

CREATE TABLE `publishers` (
  `pu_id` int(11) NOT NULL,
  `pu_name` varchar(100) DEFAULT NULL,
  `pu_image` varchar(200) DEFAULT NULL,
  `pu_website` varchar(200) DEFAULT NULL,
  `pu_introduce` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `publishers`
--

INSERT INTO `publishers` (`pu_id`, `pu_name`, `pu_image`, `pu_website`, `pu_introduce`) VALUES
(1, 'Scribner', 'scribner.png', 'https://www.simonandschusterpublishing.com/scribner/', 'Scribner is a publishing imprint of Simon & Schuster. It was founded in 1846 by Charles Scribner I and has published many notable authors, including Ernest Hemingway, F. Scott Fitzgerald, and Stephen King.'),
(2, 'HarperCollins', 'harpercollins.png', 'https://www.harpercollins.com/', 'HarperCollins Publishers is one of the world’s leading publishers of English-language fiction and non-fiction, with a prestigious history that dates back to 1817. HarperCollins has a global publishing program that includes both major works of Pulitzer Prize-winning fiction and non-fiction, and commercial bestsellers.'),
(3, 'Penguin Random House', 'penguinrandomhouse.png', 'https://www.penguinrandomhouse.com/', 'Penguin Random House is an American multinational conglomerate publishing company formed in 2013 from the merger of Random House and Penguin Group. Penguin Random House is the world’s largest trade book publisher, publishing over 15,000 new titles annually across 250 imprints.'),
(4, 'Oxford University Press', 'oxforduniversitypress.png', 'https://global.oup.com/', 'Oxford University Press (OUP) is a department of the University of Oxford. It is the world’s largest university press, publishing in over 40 languages and 190 countries. OUP publishes a wide range of scholarly works in all academic disciplines, as well as dictionaries, reference works, and books for general readers.'),
(5, 'Cambridge University Press', 'cambridgeuniversitypress.png', 'https://www.cambridge.org/', 'Cambridge University Press is the publishing business of the University of Cambridge. With a global sales presence, publishing hubs, and offices in more than 40 countries, it publishes over 50,000 titles by authors from over 100 countries every year.'),
(6, 'MIT Press', 'mitpress.png', 'https://mitpress.mit.edu/', 'MIT Press is a university press affiliated with the Massachusetts Institute of Technology (MIT) in Cambridge, Massachusetts. It publishes academic titles in the fields of science, technology, and the arts and humanities.'),
(7, 'Princeton University Press', 'princetonuniversitypress.png', 'https://press.princeton.edu/', 'Princeton University Press is an independent publisher with close connections to Princeton University. It publishes books across a wide range of disciplines, from science and technology to the humanities and social sciences.'),
(8, 'Harvard University Press', 'harvarduniversitypress.png', 'https://www.hup.harvard.edu/', 'Harvard University Press is a publishing house established on January 13, 1913, as a division of Harvard University. Its current director is George Andreou and its editor-in-chief is Susan Wallace Boehmer. The press publishes books in the humanities, social sciences, and sciences, including works by many Nobel laureates and Pulitzer Prize winners.');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `us_id` int(10) UNSIGNED NOT NULL,
  `us_name` varchar(50) DEFAULT NULL,
  `us_password` varchar(50) DEFAULT NULL,
  `us_fullname` varchar(100) DEFAULT NULL,
  `us_address` varchar(200) DEFAULT NULL,
  `us_create_date` date DEFAULT NULL,
  `us_update_password` tinyint(4) DEFAULT NULL,
  `us_mobile` varchar(20) DEFAULT NULL,
  `us_status` tinyint(4) DEFAULT NULL,
  `us_group` tinyint(4) DEFAULT NULL,
  `us_email` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`us_id`, `us_name`, `us_password`, `us_fullname`, `us_address`, `us_create_date`, `us_update_password`, `us_mobile`, `us_status`, `us_group`, `us_email`) VALUES
(1, 'john_doe', 'password1', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@example.com'),
(2, 'john_doe', 'password1password1sdspassword1sds', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@exaexassexassmple.com'),
(3, 'bob_jones', 'password3', 'Bob Jones', '789 Pine Rd', '2022-03-01', 0, '555-9012', 1, 2, 'bob.jones@example.com'),
(4, 'mary_adams', 'password4', 'Mary Adams', '321 Elm St', '2022-04-01', 0, '555-3456', 1, 2, 'mary.adams@example.com'),
(5, 'jim_brown', 'password5', 'Jim Brown', '654 Cedar Ln', '2022-05-01', 0, '555-7890', 1, 3, 'jim.brown@example.com'),
(6, 'susan_smith', 'password6', 'Susan Smith', '987 Maple Dr', '2022-06-01', 0, '555-2345', 1, 3, 'susan.smith@example.com'),
(7, 'dave_johnson', 'password7', 'Dave Johnson', '246 Birch Rd', '2022-07-01', 0, '555-6789', 1, 4, 'dave.johnson@example.com'),
(8, 'jenny_lee', 'password8', 'Jenny Lee', '369 Oak Ave', '2022-08-01', 0, '555-0123', 1, 4, 'jenny.lee@example.com'),
(9, 'ted_wilson', 'password9', 'Ted Wilson', '582 Pine Rd', '2022-09-01', 0, '555-4567', 1, 5, 'ted.wilson@example.com'),
(10, 'beth_hall', 'password10', 'Beth Hall', '795 Elm St', '2022-10-01', 0, '555-8901', 1, 5, 'beth.hall@example.com'),
(11, 'john_doedoe34doe34', 'password1', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@example.com'),
(12, 'john_doedoe34doe34doe34doe34rgefdgdoe34doe34rgefdg', 'password1', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@example.com'),
(13, 'john_doedoe34doe34doe34doe34rgefdgdoe34doe34rgefdg', 'password1', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'johnsss.doe@example.com'),
(14, 'john_doe', 'password1password1sdspassword1sds', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@exaexassexassmple.com'),
(15, 'john_doe', 'password1password1sdspassword1sds', 'John Doe', '123 Main St', '2022-01-01', 0, '555-1234', 1, 1, 'john.doe@exaexasxassmple.com');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`au_id`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`bo_id`),
  ADD KEY `fk_books_categories` (`ca_id`),
  ADD KEY `fk_books_publishers` (`pu_id`);

--
-- Indexes for table `book_authors`
--
ALTER TABLE `book_authors`
  ADD PRIMARY KEY (`ba_id`),
  ADD KEY `fk_book_authors_books` (`bo_id`),
  ADD KEY `fk_book_authors_authors` (`au_id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`ca_id`);

--
-- Indexes for table `loans`
--
ALTER TABLE `loans`
  ADD PRIMARY KEY (`lo_id`),
  ADD KEY `fk_loans_books` (`bo_id`),
  ADD KEY `fk_loans_users` (`us_id`);

--
-- Indexes for table `publishers`
--
ALTER TABLE `publishers`
  ADD PRIMARY KEY (`pu_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`us_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `us_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `fk_books_categories` FOREIGN KEY (`ca_id`) REFERENCES `categories` (`ca_id`),
  ADD CONSTRAINT `fk_books_publishers` FOREIGN KEY (`pu_id`) REFERENCES `publishers` (`pu_id`);

--
-- Constraints for table `book_authors`
--
ALTER TABLE `book_authors`
  ADD CONSTRAINT `fk_book_authors_authors` FOREIGN KEY (`au_id`) REFERENCES `authors` (`au_id`),
  ADD CONSTRAINT `fk_book_authors_books` FOREIGN KEY (`bo_id`) REFERENCES `books` (`bo_id`);

--
-- Constraints for table `loans`
--
ALTER TABLE `loans`
  ADD CONSTRAINT `fk_loans_books` FOREIGN KEY (`bo_id`) REFERENCES `books` (`bo_id`),
  ADD CONSTRAINT `fk_loans_users` FOREIGN KEY (`us_id`) REFERENCES `users` (`us_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
