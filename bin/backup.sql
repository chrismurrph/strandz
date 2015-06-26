-- MySQL dump 10.13  Distrib 5.6.19, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.19

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
-- Current Database: `test`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `test` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `test`;

--
-- Table structure for table `auto_pk_support`
--

DROP TABLE IF EXISTS `auto_pk_support`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auto_pk_support` (
  `TABLE_NAME` char(100) NOT NULL,
  `NEXT_ID` bigint(20) NOT NULL,
  UNIQUE KEY `TABLE_NAME` (`TABLE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_pk_support`
--

LOCK TABLES `auto_pk_support` WRITE;
/*!40000 ALTER TABLE `auto_pk_support` DISABLE KEYS */;
INSERT INTO `auto_pk_support` (`TABLE_NAME`, `NEXT_ID`) VALUES ('buddymanager',220),('dayinweek',220),('flexibility',220),('jdo_sequence',200),('monthinyear',220),('numdaysinterval',220),('override',220),('rosterslot',240),('seniority',220),('sex',220),('userdetails',220),('weekinmonth',220),('whichshift',220),('wombatlookups',200),('womba_dayinweeks',200),('womba_flexibilities',200),('womba_monthinyears',200),('womba_numdaysintervals',200),('womba_overrides',200),('womba_seniorities',200),('womba_sexes',200),('womba_weekinmonths',200),('womba_whichshifts',200),('worker',240),('worke_rosterslots',200);
/*!40000 ALTER TABLE `auto_pk_support` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `buddymanager`
--

DROP TABLE IF EXISTS `buddymanager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buddymanager` (
  `DAYINWEEK_PKID` int(11) DEFAULT NULL,
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `WHICHSHIFT_PKID` int(11) DEFAULT NULL,
  `WORKER_JDOID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`JDOID`),
  KEY `DAYINWEEK_PKID` (`DAYINWEEK_PKID`),
  KEY `WHICHSHIFT_PKID` (`WHICHSHIFT_PKID`),
  KEY `WORKER_JDOID` (`WORKER_JDOID`),
  CONSTRAINT `buddymanager_ibfk_1` FOREIGN KEY (`DAYINWEEK_PKID`) REFERENCES `dayinweek` (`PKID`),
  CONSTRAINT `buddymanager_ibfk_2` FOREIGN KEY (`WHICHSHIFT_PKID`) REFERENCES `whichshift` (`PKID`),
  CONSTRAINT `buddymanager_ibfk_3` FOREIGN KEY (`WORKER_JDOID`) REFERENCES `worker` (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buddymanager`
--

LOCK TABLES `buddymanager` WRITE;
/*!40000 ALTER TABLE `buddymanager` DISABLE KEYS */;
INSERT INTO `buddymanager` (`DAYINWEEK_PKID`, `JDOCLASS`, `JDOID`, `JDOVERSION`, `WHICHSHIFT_PKID`, `WORKER_JDOID`) VALUES (207,NULL,200,NULL,201,225),(205,NULL,201,NULL,201,229),(206,NULL,202,NULL,200,200),(204,NULL,203,NULL,201,206),(200,NULL,204,NULL,201,229),(201,NULL,205,NULL,200,215),(200,NULL,206,NULL,200,228),(207,NULL,207,NULL,200,216),(201,NULL,208,NULL,201,209),(204,NULL,209,NULL,200,211),(206,NULL,210,NULL,201,213),(205,NULL,211,NULL,200,228);
/*!40000 ALTER TABLE `buddymanager` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dayinweek`
--

DROP TABLE IF EXISTS `dayinweek`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dayinweek` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dayinweek`
--

LOCK TABLES `dayinweek` WRITE;
/*!40000 ALTER TABLE `dayinweek` DISABLE KEYS */;
INSERT INTO `dayinweek` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,'Wednesday',200),(NULL,NULL,'Monday',201),(NULL,NULL,'Sunday',202),(NULL,NULL,NULL,203),(NULL,NULL,'Tuesday',204),(NULL,NULL,'Thursday',205),(NULL,NULL,'Saturday',206),(NULL,NULL,'Friday',207);
/*!40000 ALTER TABLE `dayinweek` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `flexibility`
--

DROP TABLE IF EXISTS `flexibility`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `flexibility` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `flexibility`
--

LOCK TABLES `flexibility` WRITE;
/*!40000 ALTER TABLE `flexibility` DISABLE KEYS */;
INSERT INTO `flexibility` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,NULL,200),(NULL,NULL,'no overnights',201),(NULL,NULL,'no evenings',202),(NULL,NULL,'flexible',203);
/*!40000 ALTER TABLE `flexibility` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jdo_sequence`
--

DROP TABLE IF EXISTS `jdo_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jdo_sequence` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jdo_sequence`
--

LOCK TABLES `jdo_sequence` WRITE;
/*!40000 ALTER TABLE `jdo_sequence` DISABLE KEYS */;
/*!40000 ALTER TABLE `jdo_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `monthinyear`
--

DROP TABLE IF EXISTS `monthinyear`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `monthinyear` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `ORDINAL` int(11) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `monthinyear`
--

LOCK TABLES `monthinyear` WRITE;
/*!40000 ALTER TABLE `monthinyear` DISABLE KEYS */;
INSERT INTO `monthinyear` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `ORDINAL`, `PKID`) VALUES (NULL,NULL,'October',NULL,200),(NULL,NULL,'August',NULL,201),(NULL,NULL,'September',NULL,202),(NULL,NULL,'February',NULL,203),(NULL,NULL,'June',NULL,204),(NULL,NULL,'April',NULL,205),(NULL,NULL,'July',NULL,206),(NULL,NULL,'May',NULL,207),(NULL,NULL,NULL,NULL,208),(NULL,NULL,'January',NULL,209),(NULL,NULL,'December',NULL,210),(NULL,NULL,'March',NULL,211),(NULL,NULL,'November',NULL,212);
/*!40000 ALTER TABLE `monthinyear` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `numdaysinterval`
--

DROP TABLE IF EXISTS `numdaysinterval`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `numdaysinterval` (
  `DAYS` int(11) DEFAULT NULL,
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `numdaysinterval`
--

LOCK TABLES `numdaysinterval` WRITE;
/*!40000 ALTER TABLE `numdaysinterval` DISABLE KEYS */;
INSERT INTO `numdaysinterval` (`DAYS`, `JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (21,NULL,NULL,'three weekly',200),(7,NULL,NULL,'weekly',201),(28,NULL,NULL,'four weekly',202),(NULL,NULL,NULL,NULL,203),(14,NULL,NULL,'fortnightly',204);
/*!40000 ALTER TABLE `numdaysinterval` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `override`
--

DROP TABLE IF EXISTS `override`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `override` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `override`
--

LOCK TABLES `override` WRITE;
/*!40000 ALTER TABLE `override` DISABLE KEYS */;
INSERT INTO `override` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,NULL,200);
/*!40000 ALTER TABLE `override` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rosterslot`
--

DROP TABLE IF EXISTS `rosterslot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rosterslot` (
  `DAYINWEEK_PKID` int(11) DEFAULT NULL,
  `DISABLED` tinyint(4) DEFAULT NULL,
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `MONTHLYRESTART` tinyint(4) DEFAULT NULL,
  `NOTAVAILABLE` tinyint(4) DEFAULT NULL,
  `NOTINMONTH_PKID` int(11) DEFAULT NULL,
  `NUMDAYSINTERVAL_PKID` int(11) DEFAULT NULL,
  `ONLYINMONTH_PKID` int(11) DEFAULT NULL,
  `OVERRIDESOTHERS_PKID` int(11) DEFAULT NULL,
  `SPECIFICDATE` datetime DEFAULT NULL,
  `STARTDATE` datetime DEFAULT NULL,
  `WEEKINMONTH_PKID` int(11) DEFAULT NULL,
  `WHICHSHIFT_PKID` int(11) DEFAULT NULL,
  `WORKER_JDOID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`JDOID`),
  KEY `DAYINWEEK_PKID` (`DAYINWEEK_PKID`),
  KEY `ONLYINMONTH_PKID` (`ONLYINMONTH_PKID`),
  KEY `NOTINMONTH_PKID` (`NOTINMONTH_PKID`),
  KEY `NUMDAYSINTERVAL_PKID` (`NUMDAYSINTERVAL_PKID`),
  KEY `OVERRIDESOTHERS_PKID` (`OVERRIDESOTHERS_PKID`),
  KEY `WEEKINMONTH_PKID` (`WEEKINMONTH_PKID`),
  KEY `WHICHSHIFT_PKID` (`WHICHSHIFT_PKID`),
  KEY `WORKER_JDOID` (`WORKER_JDOID`),
  CONSTRAINT `rosterslot_ibfk_1` FOREIGN KEY (`DAYINWEEK_PKID`) REFERENCES `dayinweek` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_2` FOREIGN KEY (`ONLYINMONTH_PKID`) REFERENCES `monthinyear` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_3` FOREIGN KEY (`NOTINMONTH_PKID`) REFERENCES `monthinyear` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_4` FOREIGN KEY (`NUMDAYSINTERVAL_PKID`) REFERENCES `numdaysinterval` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_5` FOREIGN KEY (`OVERRIDESOTHERS_PKID`) REFERENCES `override` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_6` FOREIGN KEY (`WEEKINMONTH_PKID`) REFERENCES `weekinmonth` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_7` FOREIGN KEY (`WHICHSHIFT_PKID`) REFERENCES `whichshift` (`PKID`),
  CONSTRAINT `rosterslot_ibfk_8` FOREIGN KEY (`WORKER_JDOID`) REFERENCES `worker` (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rosterslot`
--

LOCK TABLES `rosterslot` WRITE;
/*!40000 ALTER TABLE `rosterslot` DISABLE KEYS */;
INSERT INTO `rosterslot` (`DAYINWEEK_PKID`, `DISABLED`, `JDOCLASS`, `JDOID`, `JDOVERSION`, `MONTHLYRESTART`, `NOTAVAILABLE`, `NOTINMONTH_PKID`, `NUMDAYSINTERVAL_PKID`, `ONLYINMONTH_PKID`, `OVERRIDESOTHERS_PKID`, `SPECIFICDATE`, `STARTDATE`, `WEEKINMONTH_PKID`, `WHICHSHIFT_PKID`, `WORKER_JDOID`) VALUES (207,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',200,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,216),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',201,NULL,1,NULL,208,203,208,200,NULL,NULL,205,200,226),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',202,NULL,1,NULL,208,203,208,200,NULL,NULL,205,200,227),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',203,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,225),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',204,NULL,1,NULL,208,203,208,200,NULL,NULL,202,200,217),(200,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',205,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,218),(201,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',206,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,215),(201,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',207,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,222),(207,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',208,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,224),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',209,NULL,1,NULL,208,203,208,200,NULL,NULL,204,200,203),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',210,NULL,1,NULL,208,203,208,200,NULL,NULL,202,201,212),(201,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',211,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,221),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',212,NULL,1,NULL,208,203,208,200,NULL,NULL,203,200,201),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',213,NULL,1,NULL,208,203,208,200,NULL,NULL,204,201,212),(201,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',214,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,209),(207,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',215,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,229),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',216,NULL,1,NULL,208,203,208,200,NULL,NULL,201,201,212),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',217,NULL,1,NULL,208,203,208,200,NULL,NULL,204,200,227),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',218,NULL,1,NULL,208,203,208,200,NULL,NULL,201,200,227),(200,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',219,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,223),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',220,NULL,1,NULL,208,203,208,200,NULL,NULL,205,201,212),(204,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',221,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,202),(204,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',222,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,211),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',223,NULL,1,NULL,208,203,208,200,NULL,NULL,205,201,203),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',224,NULL,1,NULL,208,203,208,200,NULL,NULL,201,201,226),(204,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',225,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,214),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',226,NULL,1,NULL,208,203,208,200,NULL,NULL,201,200,226),(200,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',227,NULL,1,NULL,208,203,208,200,NULL,NULL,203,201,203),(204,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',228,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,206),(200,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',229,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,205),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',230,NULL,1,NULL,208,203,208,200,NULL,NULL,203,201,201),(207,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',231,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,201,210),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',232,NULL,1,NULL,208,203,208,200,NULL,NULL,204,200,226),(200,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',233,NULL,NULL,NULL,208,201,208,200,NULL,NULL,200,200,228),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',234,NULL,1,NULL,208,203,208,200,NULL,NULL,201,200,203),(206,NULL,'org.strandz.data.wombatrescue.objects.RosterSlot',235,NULL,1,NULL,208,203,208,200,NULL,NULL,202,200,226);
/*!40000 ALTER TABLE `rosterslot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `seniority`
--

DROP TABLE IF EXISTS `seniority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seniority` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seniority`
--

LOCK TABLES `seniority` WRITE;
/*!40000 ALTER TABLE `seniority` DISABLE KEYS */;
INSERT INTO `seniority` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,'keyless',200),(NULL,NULL,'experienced',201),(NULL,NULL,'junior',202),(NULL,NULL,NULL,203),(NULL,NULL,'newbie',204);
/*!40000 ALTER TABLE `seniority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sex`
--

DROP TABLE IF EXISTS `sex`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sex` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sex`
--

LOCK TABLES `sex` WRITE;
/*!40000 ALTER TABLE `sex` DISABLE KEYS */;
INSERT INTO `sex` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,'female',200),(NULL,NULL,NULL,201),(NULL,NULL,'male',202);
/*!40000 ALTER TABLE `sex` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userdetails`
--

DROP TABLE IF EXISTS `userdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userdetails` (
  `DATABASEPASSWORD` varchar(255) DEFAULT NULL,
  `DATABASEUSERNAME` varchar(255) DEFAULT NULL,
  `EMAILADDRESS` varchar(255) DEFAULT NULL,
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `PASSWORD` varchar(255) DEFAULT NULL,
  `READONLY` tinyint(4) DEFAULT NULL,
  `USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userdetails`
--

LOCK TABLES `userdetails` WRITE;
/*!40000 ALTER TABLE `userdetails` DISABLE KEYS */;
INSERT INTO `userdetails` (`DATABASEPASSWORD`, `DATABASEUSERNAME`, `EMAILADDRESS`, `JDOCLASS`, `JDOID`, `JDOVERSION`, `PASSWORD`, `READONLY`, `USERNAME`) VALUES (NULL,'root',NULL,NULL,200,NULL,'Mike',NULL,'Mike');
/*!40000 ALTER TABLE `userdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `weekinmonth`
--

DROP TABLE IF EXISTS `weekinmonth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `weekinmonth` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `weekinmonth`
--

LOCK TABLES `weekinmonth` WRITE;
/*!40000 ALTER TABLE `weekinmonth` DISABLE KEYS */;
INSERT INTO `weekinmonth` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,NULL,200),(NULL,NULL,'first of month',201),(NULL,NULL,'fourth of month',202),(NULL,NULL,'second of month',203),(NULL,NULL,'third of month',204),(NULL,NULL,'fifth of month',205);
/*!40000 ALTER TABLE `weekinmonth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whichshift`
--

DROP TABLE IF EXISTS `whichshift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `whichshift` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `NAME0` varchar(255) DEFAULT NULL,
  `PKID` int(11) NOT NULL,
  PRIMARY KEY (`PKID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whichshift`
--

LOCK TABLES `whichshift` WRITE;
/*!40000 ALTER TABLE `whichshift` DISABLE KEYS */;
INSERT INTO `whichshift` (`JDOCLASS`, `JDOVERSION`, `NAME0`, `PKID`) VALUES (NULL,NULL,'dinner',200),(NULL,NULL,'overnight',201),(NULL,NULL,NULL,202);
/*!40000 ALTER TABLE `whichshift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_dayinweeks`
--

DROP TABLE IF EXISTS `womba_dayinweeks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_dayinweeks` (
  `DAYINWEEKS_ORDER` int(11) DEFAULT NULL,
  `DAYINWEEKS_PKID` int(11) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_dayinweeks`
--

LOCK TABLES `womba_dayinweeks` WRITE;
/*!40000 ALTER TABLE `womba_dayinweeks` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_dayinweeks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_flexibilities`
--

DROP TABLE IF EXISTS `womba_flexibilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_flexibilities` (
  `FLEXIBILITIES_ORDER` int(11) DEFAULT NULL,
  `FLEXIBILITIES_PKID` int(11) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_flexibilities`
--

LOCK TABLES `womba_flexibilities` WRITE;
/*!40000 ALTER TABLE `womba_flexibilities` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_flexibilities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_monthinyears`
--

DROP TABLE IF EXISTS `womba_monthinyears`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_monthinyears` (
  `JDOID` bigint(20) NOT NULL,
  `MONTHINYEARS_ORDER` int(11) DEFAULT NULL,
  `MONTHINYEARS_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_monthinyears`
--

LOCK TABLES `womba_monthinyears` WRITE;
/*!40000 ALTER TABLE `womba_monthinyears` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_monthinyears` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_numdaysintervals`
--

DROP TABLE IF EXISTS `womba_numdaysintervals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_numdaysintervals` (
  `JDOID` bigint(20) NOT NULL,
  `NUMDAYSINTERVALS_ORDER` int(11) DEFAULT NULL,
  `NUMDAYSINTERVALS_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_numdaysintervals`
--

LOCK TABLES `womba_numdaysintervals` WRITE;
/*!40000 ALTER TABLE `womba_numdaysintervals` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_numdaysintervals` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_overrides`
--

DROP TABLE IF EXISTS `womba_overrides`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_overrides` (
  `JDOID` bigint(20) NOT NULL,
  `OVERRIDES_ORDER` int(11) DEFAULT NULL,
  `OVERRIDES_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_overrides`
--

LOCK TABLES `womba_overrides` WRITE;
/*!40000 ALTER TABLE `womba_overrides` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_overrides` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_seniorities`
--

DROP TABLE IF EXISTS `womba_seniorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_seniorities` (
  `JDOID` bigint(20) NOT NULL,
  `SENIORITIES_ORDER` int(11) DEFAULT NULL,
  `SENIORITIES_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_seniorities`
--

LOCK TABLES `womba_seniorities` WRITE;
/*!40000 ALTER TABLE `womba_seniorities` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_seniorities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_sexes`
--

DROP TABLE IF EXISTS `womba_sexes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_sexes` (
  `JDOID` bigint(20) NOT NULL,
  `SEXES_ORDER` int(11) DEFAULT NULL,
  `SEXES_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_sexes`
--

LOCK TABLES `womba_sexes` WRITE;
/*!40000 ALTER TABLE `womba_sexes` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_sexes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_weekinmonths`
--

DROP TABLE IF EXISTS `womba_weekinmonths`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_weekinmonths` (
  `JDOID` bigint(20) NOT NULL,
  `WEEKINMONTHS_ORDER` int(11) DEFAULT NULL,
  `WEEKINMONTHS_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_weekinmonths`
--

LOCK TABLES `womba_weekinmonths` WRITE;
/*!40000 ALTER TABLE `womba_weekinmonths` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_weekinmonths` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `womba_whichshifts`
--

DROP TABLE IF EXISTS `womba_whichshifts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `womba_whichshifts` (
  `JDOID` bigint(20) NOT NULL,
  `WHICHSHIFTS_ORDER` int(11) DEFAULT NULL,
  `WHICHSHIFTS_PKID` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `womba_whichshifts`
--

LOCK TABLES `womba_whichshifts` WRITE;
/*!40000 ALTER TABLE `womba_whichshifts` DISABLE KEYS */;
/*!40000 ALTER TABLE `womba_whichshifts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wombatlookups`
--

DROP TABLE IF EXISTS `wombatlookups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `wombatlookups` (
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wombatlookups`
--

LOCK TABLES `wombatlookups` WRITE;
/*!40000 ALTER TABLE `wombatlookups` DISABLE KEYS */;
/*!40000 ALTER TABLE `wombatlookups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worke_rosterslots`
--

DROP TABLE IF EXISTS `worke_rosterslots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worke_rosterslots` (
  `JDOID` bigint(20) NOT NULL,
  `ROSTERSLOTS_JDOID` bigint(20) DEFAULT NULL,
  `ROSTERSLOTS_ORDER` int(11) DEFAULT NULL,
  PRIMARY KEY (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worke_rosterslots`
--

LOCK TABLES `worke_rosterslots` WRITE;
/*!40000 ALTER TABLE `worke_rosterslots` DISABLE KEYS */;
/*!40000 ALTER TABLE `worke_rosterslots` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worker`
--

DROP TABLE IF EXISTS `worker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worker` (
  `AWAY1END` datetime DEFAULT NULL,
  `AWAY1START` datetime DEFAULT NULL,
  `AWAY2END` datetime DEFAULT NULL,
  `AWAY2START` datetime DEFAULT NULL,
  `BELONGSTOGROUP_JDOID` bigint(20) DEFAULT NULL,
  `BIRTHDAY` datetime DEFAULT NULL,
  `CHRISTIANNAME` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `CONTACTNAME` varchar(255) DEFAULT NULL,
  `DUMMY` tinyint(4) DEFAULT NULL,
  `EMAIL1` varchar(255) DEFAULT NULL,
  `EMAIL2` varchar(255) DEFAULT NULL,
  `FLEXIBILITY_PKID` int(11) DEFAULT NULL,
  `GROUPCONTACTPERSON` tinyint(4) DEFAULT NULL,
  `GROUPNAME` varchar(255) DEFAULT NULL,
  `HOMEPHONE` varchar(255) DEFAULT NULL,
  `JDOCLASS` varchar(255) DEFAULT NULL,
  `JDOID` bigint(20) NOT NULL,
  `JDOVERSION` int(11) DEFAULT NULL,
  `MOBILEPHONE` varchar(255) DEFAULT NULL,
  `POSTCODE` varchar(255) DEFAULT NULL,
  `SENIORITY_PKID` int(11) DEFAULT NULL,
  `SEX_PKID` int(11) DEFAULT NULL,
  `SHIFTPREFERENCE_PKID` int(11) DEFAULT NULL,
  `STREET` varchar(255) DEFAULT NULL,
  `SUBURB` varchar(255) DEFAULT NULL,
  `SURNAME` varchar(255) DEFAULT NULL,
  `UNKNOWN0` tinyint(4) DEFAULT NULL,
  `WORKPHONE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`JDOID`),
  KEY `FLEXIBILITY_PKID` (`FLEXIBILITY_PKID`),
  KEY `SENIORITY_PKID` (`SENIORITY_PKID`),
  KEY `SEX_PKID` (`SEX_PKID`),
  KEY `SHIFTPREFERENCE_PKID` (`SHIFTPREFERENCE_PKID`),
  KEY `BELONGSTOGROUP_JDOID` (`BELONGSTOGROUP_JDOID`),
  CONSTRAINT `worker_ibfk_1` FOREIGN KEY (`FLEXIBILITY_PKID`) REFERENCES `flexibility` (`PKID`),
  CONSTRAINT `worker_ibfk_2` FOREIGN KEY (`SENIORITY_PKID`) REFERENCES `seniority` (`PKID`),
  CONSTRAINT `worker_ibfk_3` FOREIGN KEY (`SEX_PKID`) REFERENCES `sex` (`PKID`),
  CONSTRAINT `worker_ibfk_4` FOREIGN KEY (`SHIFTPREFERENCE_PKID`) REFERENCES `whichshift` (`PKID`),
  CONSTRAINT `worker_ibfk_5` FOREIGN KEY (`BELONGSTOGROUP_JDOID`) REFERENCES `worker` (`JDOID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worker`
--

LOCK TABLES `worker` WRITE;
/*!40000 ALTER TABLE `worker` DISABLE KEYS */;
INSERT INTO `worker` (`AWAY1END`, `AWAY1START`, `AWAY2END`, `AWAY2START`, `BELONGSTOGROUP_JDOID`, `BIRTHDAY`, `CHRISTIANNAME`, `COMMENTS`, `CONTACTNAME`, `DUMMY`, `EMAIL1`, `EMAIL2`, `FLEXIBILITY_PKID`, `GROUPCONTACTPERSON`, `GROUPNAME`, `HOMEPHONE`, `JDOCLASS`, `JDOID`, `JDOVERSION`, `MOBILEPHONE`, `POSTCODE`, `SENIORITY_PKID`, `SEX_PKID`, `SHIFTPREFERENCE_PKID`, `STREET`, `SUBURB`, `SURNAME`, `UNKNOWN0`, `WORKPHONE`) VALUES (NULL,NULL,NULL,NULL,207,NULL,'Jeanie','comment for Jeanie Balcom',NULL,0,'Jeanie.Balcom@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',200,NULL,NULL,NULL,201,201,202,NULL,NULL,'Balcom',1,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,NULL,'comment for Dave Wood\'s Group','Dave Wood',0,'Dave.Wood@small-town-isp.com.au',NULL,203,0,'Dave Wood\'s Group',NULL,'org.strandz.data.wombatrescue.objects.Worker',201,NULL,NULL,NULL,201,201,202,NULL,NULL,NULL,0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Raymond','comment for Raymond Cazabon',NULL,0,'Raymond.Cazabon@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',202,NULL,NULL,NULL,201,201,202,NULL,NULL,'Cazabon',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Billy','comment for Billy Smith',NULL,0,'Billy.Smith@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',203,NULL,NULL,NULL,201,201,202,NULL,NULL,'Smith',0,'9734 5500'),(NULL,NULL,NULL,NULL,201,NULL,'Con','comment for Con Polites',NULL,0,'Con.Polites@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',204,NULL,NULL,NULL,201,201,202,NULL,NULL,'Polites',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'George','comment for George Moore',NULL,0,'George.Moore@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',205,NULL,NULL,NULL,201,201,202,NULL,NULL,'Moore',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Justin','comment for Justin Craddock',NULL,0,'Justin.Craddock@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',206,NULL,NULL,NULL,201,201,202,NULL,NULL,'Craddock',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,NULL,NULL,NULL,1,NULL,NULL,NULL,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',207,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(NULL,NULL,NULL,NULL,207,NULL,'Jenny','comment for Jenny Jenkins',NULL,0,'Jenny.Jenkins@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',208,NULL,NULL,NULL,201,201,202,NULL,NULL,'Jenkins',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Derek','comment for Derek Granleese',NULL,0,'Derek.Granleese@small-town-isp.com.au',NULL,203,0,NULL,'8276 5882','org.strandz.data.wombatrescue.objects.Worker',209,NULL,'0402 456 895','',201,201,202,'17 Woodhouse Cresent','Wattle Park','Granleese',0,'8276 5882'),(NULL,NULL,NULL,NULL,207,NULL,'Joshua','comment for Joshua Bloch',NULL,0,'Joshua.Bloch@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',210,NULL,NULL,NULL,201,201,202,NULL,NULL,'Bloch',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Harry','comment for Harry Sufehmi',NULL,0,'Harry.Sufehmi@small-town-isp.com.au',NULL,203,0,NULL,'8276 5882','org.strandz.data.wombatrescue.objects.Worker',211,NULL,'0402 456 895','',201,201,202,'6 Twilight Drive','Happy Valley','Sufehmi',0,'8276 5882'),('2007-07-17 00:00:00','2007-07-13 00:00:00',NULL,NULL,207,NULL,'Clayton','comment for Clayton Fahie',NULL,0,'Clayton.Fahie@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',212,NULL,NULL,NULL,201,201,202,NULL,NULL,'Fahie',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Lilia','comment for Lilia Lavallie',NULL,0,'Lilia.Lavallie@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',213,NULL,NULL,NULL,201,201,202,NULL,NULL,'Lavallie',1,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Michael','comment for Michael Bucket',NULL,0,'Michael.Bucket@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',214,NULL,NULL,'',201,201,202,'29 Amberly Road','Loughton','Bucket',0,NULL),(NULL,NULL,NULL,NULL,207,NULL,'Alfred','comment for Alfred Nooteboom',NULL,0,'Alfred.Nooteboom@small-town-isp.com.au',NULL,203,0,NULL,'8276 5882','org.strandz.data.wombatrescue.objects.Worker',215,NULL,'0402 456 895','',201,201,202,'30 Heather Court','Paradise','Nooteboom',0,'8276 5882'),(NULL,NULL,NULL,NULL,207,NULL,'Russell','comment for Russell Hinze',NULL,0,'Russell.Hinze@small-town-isp.com.au',NULL,201,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',216,NULL,NULL,NULL,201,201,202,NULL,NULL,'Hinze',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Noreen','comment for Noreen Neve',NULL,0,'Noreen.Neve@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',217,NULL,NULL,NULL,201,201,202,NULL,NULL,'Neve',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Web','comment for Web Masters',NULL,0,'Web.Masters@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',218,NULL,NULL,NULL,201,201,202,NULL,NULL,'Masters',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Phillipa','comment for Phillipa Hayes',NULL,0,'Phillipa.Hayes@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',219,NULL,NULL,NULL,201,201,202,NULL,NULL,'Hayes',0,'9734 5500'),(NULL,NULL,NULL,NULL,201,NULL,'Dave','comment for Dave Wood',NULL,0,'Dave.Wood@small-town-isp.com.au',NULL,203,1,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',220,NULL,NULL,NULL,201,201,202,NULL,NULL,'Wood',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Pierre','comment for Pierre Poisson',NULL,0,'Pierre.Poisson@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',221,NULL,NULL,NULL,201,201,202,NULL,NULL,'Poisson',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Graham','comment for Graham Fraenkel',NULL,0,'Graham.Fraenkel@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',222,NULL,NULL,NULL,201,201,202,NULL,NULL,'Fraenkel',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Ty','comment for Ty Paywa',NULL,0,'Ty.Paywa@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',223,NULL,NULL,NULL,201,201,202,NULL,NULL,'Paywa',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Guy','comment for Guy Steele',NULL,0,'Guy.Steele@small-town-isp.com.au',NULL,202,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',224,NULL,NULL,NULL,201,201,202,NULL,NULL,'Steele',0,'9734 5500'),(NULL,NULL,NULL,NULL,201,NULL,'Naba','comment for Naba Barkakati',NULL,0,'Naba.Barkakati@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',225,NULL,NULL,NULL,201,201,202,NULL,NULL,'Barkakati',0,'9734 5500'),('2007-07-17 00:00:00','2007-07-13 00:00:00',NULL,NULL,207,NULL,'Kurt','comment for Kurt Hilario',NULL,0,'Kurt.Hilario@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',226,NULL,NULL,NULL,201,201,202,NULL,NULL,'Hilario',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Erick','comment for Erick Jones',NULL,0,'Erick.Jones@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',227,NULL,NULL,NULL,201,201,202,NULL,NULL,'Jones',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Samir','comment for Samir Noshy',NULL,0,'Samir.Noshy@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',228,NULL,NULL,NULL,201,201,202,NULL,NULL,'Noshy',0,'9734 5500'),(NULL,NULL,NULL,NULL,207,NULL,'Harvey','comment for Harvey Crumpet',NULL,0,'Harvey.Crumpet@small-town-isp.com.au',NULL,203,0,NULL,NULL,'org.strandz.data.wombatrescue.objects.Worker',229,NULL,NULL,NULL,201,201,202,NULL,NULL,'Crumpet',0,'9734 5500');
/*!40000 ALTER TABLE `worker` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-05-20 16:30:19
