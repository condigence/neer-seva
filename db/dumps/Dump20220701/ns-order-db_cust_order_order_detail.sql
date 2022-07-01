-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: ns-order-db
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `cust_order_order_detail`
--

DROP TABLE IF EXISTS `cust_order_order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cust_order_order_detail` (
  `id` bigint NOT NULL,
  `order_detail_id` bigint NOT NULL,
  UNIQUE KEY `UK_ecjcn8xdhx3edrnkb5enwcd5` (`order_detail_id`),
  KEY `FKsp8mucikvxdasbhg33aipt1nn` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cust_order_order_detail`
--

LOCK TABLES `cust_order_order_detail` WRITE;
/*!40000 ALTER TABLE `cust_order_order_detail` DISABLE KEYS */;
INSERT INTO `cust_order_order_detail` VALUES (1,2),(3,4),(5,6),(7,8),(7,9),(10,11),(10,12),(10,13),(14,15),(16,17),(16,18),(19,20),(19,21),(22,23),(24,25),(26,27),(26,28),(26,29),(30,31),(30,32),(30,33),(34,35),(34,36),(34,37),(38,39),(40,41),(42,43),(44,45),(46,47),(46,48),(49,50),(49,51),(52,53),(52,54),(55,56),(55,57),(58,59),(58,60),(61,62),(61,63),(64,65),(64,66),(67,68),(67,69),(70,71),(70,72),(73,74),(75,76),(77,78),(77,79),(80,81),(80,82);
/*!40000 ALTER TABLE `cust_order_order_detail` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-01 12:35:54
