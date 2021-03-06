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
-- Table structure for table `order_detail`
--

DROP TABLE IF EXISTS `order_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_detail` (
  `order_detail_id` bigint NOT NULL,
  `order_discount` int DEFAULT NULL,
  `order_gst` int DEFAULT NULL,
  `order_tem_id` bigint DEFAULT NULL,
  `order_item_price` int DEFAULT NULL,
  `order_item_quantity` int DEFAULT NULL,
  `order_service_charge` int DEFAULT NULL,
  `order_sub_total` int DEFAULT NULL,
  `order_total_amount` int DEFAULT NULL,
  PRIMARY KEY (`order_detail_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_detail`
--

LOCK TABLES `order_detail` WRITE;
/*!40000 ALTER TABLE `order_detail` DISABLE KEYS */;
INSERT INTO `order_detail` VALUES (2,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(4,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(6,NULL,NULL,7,NULL,3,NULL,NULL,NULL),(8,NULL,NULL,7,NULL,36,NULL,NULL,NULL),(9,NULL,NULL,8,NULL,5,NULL,NULL,NULL),(11,NULL,NULL,18,NULL,1,NULL,NULL,NULL),(12,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(13,NULL,NULL,14,NULL,1,NULL,NULL,NULL),(15,NULL,NULL,15,NULL,1,NULL,NULL,NULL),(17,NULL,NULL,18,NULL,1,NULL,NULL,NULL),(18,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(20,NULL,NULL,7,NULL,3,NULL,NULL,NULL),(21,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(23,NULL,NULL,20,NULL,3,NULL,NULL,NULL),(25,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(27,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(28,NULL,NULL,18,NULL,1,NULL,NULL,NULL),(29,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(31,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(32,NULL,NULL,18,NULL,1,NULL,NULL,NULL),(33,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(35,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(36,NULL,NULL,18,NULL,1,NULL,NULL,NULL),(37,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(39,NULL,NULL,14,NULL,1,NULL,NULL,NULL),(41,NULL,NULL,15,NULL,1,NULL,NULL,NULL),(43,NULL,NULL,15,NULL,1,NULL,NULL,NULL),(45,NULL,NULL,15,NULL,1,NULL,NULL,NULL),(47,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(48,NULL,NULL,15,NULL,1,NULL,NULL,NULL),(50,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(51,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(53,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(54,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(56,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(57,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(59,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(60,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(62,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(63,NULL,NULL,14,NULL,6,NULL,NULL,NULL),(65,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(66,NULL,NULL,14,NULL,6,NULL,NULL,NULL),(68,NULL,NULL,8,NULL,2,NULL,NULL,NULL),(69,NULL,NULL,14,NULL,11,NULL,NULL,NULL),(71,NULL,NULL,7,NULL,1,NULL,NULL,NULL),(72,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(74,NULL,NULL,15,NULL,2,NULL,NULL,NULL),(76,NULL,NULL,14,NULL,3,NULL,NULL,NULL),(78,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(79,NULL,NULL,14,NULL,3,NULL,NULL,NULL),(81,NULL,NULL,8,NULL,1,NULL,NULL,NULL),(82,NULL,NULL,14,NULL,5,NULL,NULL,NULL);
/*!40000 ALTER TABLE `order_detail` ENABLE KEYS */;
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
