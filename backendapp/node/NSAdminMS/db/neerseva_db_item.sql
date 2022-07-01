-- MySQL dump 10.13  Distrib 8.0.12, for Win64 (x86_64)
--
-- Host: localhost    Database: neerseva_db
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `code` varchar(45) DEFAULT NULL,
  `price` varchar(45) DEFAULT NULL,
  `mrp` varchar(45) DEFAULT NULL,
  `disp_price` varchar(45) DEFAULT NULL,
  `discount` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `capacity` varchar(45) DEFAULT NULL,
  `brand_id` int(11) DEFAULT NULL,
  `image_id` int(11) DEFAULT NULL,
  `date_created` varchar(45) DEFAULT NULL,
  `created_by_user` varchar(45) DEFAULT NULL,
  `is_deleted` varchar(45) DEFAULT NULL,
  `tax` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ITME_ID_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='			';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES (7,'Bisleri 20L Cane','B20L','70','90','80','10','Drinking Water','pure drinking water','20',1,NULL,NULL,NULL,NULL,'08'),(8,' kinley 5L cane','K5c','60','80','70','10','Drinking water','fresh water is good for health','5',2,NULL,NULL,NULL,NULL,'1'),(17,'Bislery 1 L','BSL1','18','20','15','3','water','Water','1',1,NULL,NULL,NULL,NULL,'2'),(18,'Bislery 2L ','BSL2L','40','35','32','03','WATER','drinking water','2',1,NULL,NULL,NULL,NULL,'02'),(19,'Bislery 5L cane','BSL5L','100','90','80','10','Neer','Water for drinking','5',1,NULL,NULL,NULL,NULL,'05'),(20,'Kinley 2L Bottle','KNL2L','30','35','30','02','water','pure neer','2',2,NULL,NULL,NULL,NULL,'01'),(21,'Kinley 1L Bottel','KNL1LC','15','18','15','01','water','Water drink','1',2,NULL,NULL,NULL,NULL,'01'),(22,'Kinley 20L Cane','KL20LC','100','90','80','10','water','Drinking water ','20',2,NULL,NULL,NULL,NULL,'10'),(23,'Bislery 1/2 L','HLBC','08','10','90','10','Drinking water','Water for drinking','1/2',1,NULL,NULL,NULL,NULL,'1'),(33,'RN20LC','12','90','100','100','00','Drinking water','water','20',4,NULL,NULL,NULL,NULL,'5'),(34,'Manikchand water bottle','MWB5L','45','50','40','05','water','Drinking water','5',27,NULL,NULL,NULL,NULL,'02'),(35,'Himalaya  water','HWC','110','120','100','20','Drinking water','water','5',29,NULL,NULL,NULL,NULL,'05'),(36,'Tata water','TW20','120','130','110','10','Drinking water','Water','20',30,NULL,NULL,NULL,NULL,'05'),(37,'Tata water water','TW20TW20','110','140','120','09','Drinking water water','Water water','10',31,NULL,NULL,NULL,'NO','09'),(42,'kinley water','KNLW','120','110','100','10','water','sfsfsfsdf','20',2,NULL,NULL,NULL,NULL,'05');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-17  1:13:49
