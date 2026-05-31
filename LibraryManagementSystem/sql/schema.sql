CREATE DATABASE  IF NOT EXISTS `lms_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lms_db`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: lms_db
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `isbn` varchar(13) NOT NULL,
  `title` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `genre` varchar(20) NOT NULL,
  `published_year` smallint NOT NULL,
  `book_status` enum('AVAILABLE','BORROWED','RESERVED','LOST','UNDER_MAINTENANCE') NOT NULL,
  `book_type` enum('PHYSICAL','EBOOK','AUDIOBOOK') NOT NULL,
  `shelf_location` varchar(25) DEFAULT NULL,
  `total_copies` int DEFAULT NULL,
  `available_copies` int DEFAULT NULL,
  `download_url` varchar(2000) DEFAULT NULL,
  `file_format` varchar(30) DEFAULT NULL,
  `file_size_mb` double(8,2) DEFAULT NULL,
  `narrator` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`isbn`),
  FULLTEXT KEY `match_query_books` (`title`,`author`,`isbn`,`genre`,`shelf_location`,`narrator`,`file_format`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `member_id` char(9) NOT NULL,
  `member_name` varchar(35) NOT NULL,
  `phone` varchar(11) NOT NULL,
  `email` varchar(254) NOT NULL,
  `address` varchar(55) NOT NULL,
  `age` int NOT NULL,
  `member_status` enum('ACTIVE','SUSPENDED','EXPIRED') NOT NULL,
  PRIMARY KEY (`member_id`),
  FULLTEXT KEY `match_query_members` (`member_id`,`member_name`,`email`,`phone`),
  CONSTRAINT `check_age_members` CHECK ((`age` > 1910))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `reservations`
--

DROP TABLE IF EXISTS `reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservations` (
  `isbn` varchar(13) NOT NULL,
  `member_id` char(9) NOT NULL,
  `notified` tinyint(1) NOT NULL DEFAULT '0',
  `position` int NOT NULL,
  PRIMARY KEY (`isbn`,`member_id`),
  UNIQUE KEY `isbn` (`isbn`,`position`),
  KEY `reservation_member_fk` (`member_id`),
  CONSTRAINT `reservation_book_fk` FOREIGN KEY (`isbn`) REFERENCES `books` (`isbn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `reservation_member_fk` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `salt_n_hash`
--

DROP TABLE IF EXISTS `salt_n_hash`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `salt_n_hash` (
  `worker_id` char(9) NOT NULL,
  `password_salt` char(32) NOT NULL,
  `password_hash` char(64) NOT NULL,
  PRIMARY KEY (`worker_id`),
  CONSTRAINT `fk_worker_id_snh` FOREIGN KEY (`worker_id`) REFERENCES `workers` (`worker_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `transactions`
--

DROP TABLE IF EXISTS `transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transactions` (
  `transaction_id` char(36) NOT NULL,
  `member_id` char(9) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `borrow_date` date NOT NULL,
  `due_date` date NOT NULL,
  `return_date` date DEFAULT NULL,
  `fine_amount` decimal(5,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`transaction_id`),
  KEY `transaction_member_fk` (`member_id`),
  KEY `transaction_book_fk` (`isbn`),
  CONSTRAINT `transaction_book_fk` FOREIGN KEY (`isbn`) REFERENCES `books` (`isbn`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `transaction_member_fk` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `check_due_date` CHECK ((`due_date` > `borrow_date`)),
  CONSTRAINT `check_return_date` CHECK ((`return_date` >= `borrow_date`))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workers`
--

DROP TABLE IF EXISTS `workers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workers` (
  `worker_id` char(9) NOT NULL,
  `worker_name` varchar(35) NOT NULL,
  `phone` char(11) DEFAULT NULL,
  `email` varchar(254) NOT NULL,
  `address` varchar(55) NOT NULL,
  `age` int NOT NULL,
  PRIMARY KEY (`worker_id`),
  FULLTEXT KEY `search_workers` (`worker_id`,`worker_name`,`email`,`phone`),
  CONSTRAINT `check_age_workers` CHECK ((`age` > 1910))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-31 16:48:12
