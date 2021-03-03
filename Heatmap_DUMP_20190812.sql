-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 10.10.1.43    Database: bdheatmap
-- ------------------------------------------------------
-- Server version	5.6.44-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `resultadoheatmap`
--

DROP TABLE IF EXISTS `resultadoheatmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resultadoheatmap` (
  `Idfuncionalidade` int(11) DEFAULT NULL,
  `funcionalidade` varchar(45) DEFAULT NULL,
  `data` varchar(10) DEFAULT NULL,
  `hora` varchar(7) DEFAULT NULL,
  `TotalExecutados` bigint(22) DEFAULT NULL,
  `TotalFaileds` bigint(21) DEFAULT NULL,
  `TotalPasseds` bigint(21) DEFAULT NULL,
  `percentComSinal` varchar(71) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbfuncionalidades`
--

DROP TABLE IF EXISTS `tbfuncionalidades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbfuncionalidades` (
  `idFuncionalidade` int(11) NOT NULL,
  `Funcionalidade` varchar(45) NOT NULL,
  PRIMARY KEY (`idFuncionalidade`),
  UNIQUE KEY `idFuncionalidade_UNIQUE` (`idFuncionalidade`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tbloghetmap`
--

DROP TABLE IF EXISTS `tbloghetmap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tbloghetmap` (
  `idLog` int(11) NOT NULL AUTO_INCREMENT,
  `idFuncionalidade` int(11) NOT NULL,
  `DataHoraLog` datetime NOT NULL,
  `StatusExecucao` varchar(45) NOT NULL,
  `Descricao` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idLog`),
  UNIQUE KEY `idLog_UNIQUE` (`idLog`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'bdheatmap'
--
/*!50003 DROP PROCEDURE IF EXISTS `retornoHeatMap` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `retornoHeatMap`()
begin
 
                               declare cont int;        
                               declare qtdeFuncionalidade int unsigned default 0;
        declare filtro varchar(30);
        
        drop table if exists resultadoHeatMap;
                               create table resultadoHeatMap
                               (
                                               Idfuncionalidade int(11) ,
                                               funcionalidade varchar(45) ,
                                               data varchar(10) ,
                                               hora varchar(7) ,
                                               TotalExecutados bigint(22) ,
                                               TotalFaileds bigint(21) ,
                                               TotalPasseds bigint(21) ,
                                               percentComSinal varchar(71)
                               )
                               engine=innodb;
                               
                               
        
                 select count(*) from tbFuncionalidades into @qtdeFuncionalidade;  
     start transaction;
     set cont = 1;
                WHILE(@cont < @qtdeFuncionalidade) DO
                
                                                               
            DROP VIEW if exists view_resultadoHeatMap;     
            CREATE VIEW view_resultadoHeatMap (Idfuncionalidade,Funcionalidade, DataExecucao, HoraExecucao, TotalExecutados, TotalFaileds, TotalPasseds, PercentComSinal) 
                                                                                               AS SELECT idFuncionalidade as idFuncionalidade
                                                                                                ,(select Funcionalidade from tbFuncionalidades where idFuncionalidade = tbloghetmap.idFuncionalidade) as Funcionalidade
                                                                                              ,DATE_FORMAT(datahoralog, "%Y-%m-%d") as DataExecucao
                                                                                              ,DATE_FORMAT(DataHoraLog, "%H") as HoraExecucao
                                                                                              ,(COUNT(IF(StatusExecucao='passou',1, NULL)) + COUNT(IF(StatusExecucao='falhou',1, NULL))) as TotalExecutados
                                                                                              ,COUNT(IF(StatusExecucao='falhou',1, NULL)) as TotalFaileds
                                                                                              ,COUNT(IF(StatusExecucao='passou',1, NULL)) as TotalPasseds
                                                                                              ,(COUNT(IF(StatusExecucao='passou',1, NULL)) * 100 / (COUNT(IF(StatusExecucao='passou',1, NULL)) + COUNT(IF(StatusExecucao='falhou',1, NULL)))) AS PercentComSinal
                                               FROM tbloghetmap  
                                               WHERE  idFuncionalidade = 1     
                                               AND DATE_FORMAT(datahoralog, "%Y-%m-%d") = "2019-05-10"
                                               GROUP BY idfuncionalidade, DATE_FORMAT(DataHoraLog, "%H");
                                               
            INSERT INTO resultadoHeatMap SELECT * FROM view_resultadoHeatMap;           
            
                               set cont=cont+1;
                  end while;
                  commit;
      SELECT Idfuncionalidade,
                                               funcionalidade,
                                               data ,
                                               hora ,
                                               TotalExecutados ,
                                               TotalFaileds ,
                                               TotalPasseds ,
                                               CONCAT(format(percentComSinal, '#'), ' %')       
      FROM bdheatmap.resultadoheatmap
      group by Idfuncionalidade, hora;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-08-12  8:59:16
