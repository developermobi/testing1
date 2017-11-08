/*
SQLyog Community v12.4.3 (64 bit)
MySQL - 10.1.19-MariaDB : Database - mobi_sms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mobi_sms` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `mobi_sms`;

/*Table structure for table `contact` */

DROP TABLE IF EXISTS `contact`;

CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `designation` varchar(255) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `group_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6801auo5j2qte1lbj6fa8n8hb` (`group_id`),
  CONSTRAINT `FK_6801auo5j2qte1lbj6fa8n8hb` FOREIGN KEY (`group_id`) REFERENCES `group_details` (`id`),
  CONSTRAINT `FKhwvdw93yse57p79g3xlb38tdb` FOREIGN KEY (`group_id`) REFERENCES `group_details` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

/*Data for the table `contact` */

insert  into `contact`(`id`,`created`,`designation`,`email_id`,`mobile`,`name`,`status`,`updated`,`group_id`,`user_id`) values 
(1,NULL,'software developer','sani@gmail.com','9819247327','sani',2,'2017-10-27 20:01:32',9,1),
(2,NULL,'','','9819247326','',2,'2017-10-27 20:01:30',9,1),
(3,NULL,'','','9819247321','',2,'2017-10-27 19:58:27',9,1),
(4,NULL,'','','9819247329','',2,'2017-10-27 19:58:24',9,1),
(5,NULL,'','','9819247328','',2,'2017-10-27 19:58:21',9,1),
(6,NULL,'software developer','sani@gmail.com','9819247327','sani',2,'2017-10-27 20:01:26',9,1),
(7,NULL,'','','9819247326','',2,'2017-10-27 20:01:24',9,1),
(8,NULL,'','','9819247321','',2,'2017-10-27 20:01:13',9,1),
(9,NULL,'','','9819247329','',2,'2017-10-27 20:01:17',9,1),
(10,NULL,'','','9819247328','',2,'2017-10-27 20:01:21',9,1),
(11,NULL,'software developer','sani@gmail.com','9819247327','sani',1,'2017-10-27 20:02:28',9,1),
(12,NULL,'','','9819247326','',2,'2017-10-27 20:10:57',9,1),
(13,NULL,'','','9819247321','',2,'2017-10-27 20:10:54',9,1),
(14,NULL,'','','9819247329','',2,'2017-10-27 20:10:50',9,1),
(15,NULL,'','','9819247328','',2,'2017-10-27 20:10:35',9,1),
(16,NULL,'software developer','sani@gmail.com','9819247327','sani',2,'2017-10-27 20:10:38',9,1),
(17,NULL,'','','9819247326','',2,'2017-10-27 20:10:42',9,1),
(18,NULL,'','','9819247321','',2,'2017-10-27 20:10:44',9,1),
(19,NULL,'','','9819247329','',2,'2017-10-27 20:10:47',9,1),
(20,NULL,'','','9819247328','',0,'2017-10-27 20:05:11',9,1),
(21,NULL,'software developer','sani@gmail.com','9819247327','sani',2,'2017-10-27 20:11:01',9,1),
(22,NULL,'','','9819247326','',1,'2017-10-27 20:07:31',9,1),
(23,NULL,'','','9819247321','',2,'2017-10-27 20:11:09',9,1),
(24,NULL,'','','9819247329','',2,'2017-10-27 20:11:04',9,1),
(25,NULL,'','','9819247328','',2,'2017-10-27 20:11:07',9,1),
(26,NULL,'','','9819247321','',1,'2017-10-27 20:11:37',9,1),
(27,NULL,'','','9819247329','',1,'2017-10-27 20:11:37',9,1);

/*Table structure for table `credit` */

DROP TABLE IF EXISTS `credit`;

CREATE TABLE `credit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `credit` int(11) DEFAULT NULL,
  `credit_by` int(11) DEFAULT NULL,
  `credit_type` int(11) DEFAULT NULL,
  `current_amount` int(11) DEFAULT NULL,
  `previous_amount` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `prodcut_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_le8xgnr6sq7qs72sdvcrmbbad` (`prodcut_id`),
  KEY `FK_2f7bumcbi2a28ayjjnwus42ho` (`user_id`),
  KEY `FK_35y63omvydekxphqswb5anpeu` (`product_id`),
  CONSTRAINT `FK86ldembhuf56mm7qvdsq9kqx` FOREIGN KEY (`prodcut_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_2f7bumcbi2a28ayjjnwus42ho` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_35y63omvydekxphqswb5anpeu` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_le8xgnr6sq7qs72sdvcrmbbad` FOREIGN KEY (`prodcut_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKruqa8y0rd9lldy1mcwq78shlv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `credit` */

/*Table structure for table `debit` */

DROP TABLE IF EXISTS `debit`;

CREATE TABLE `debit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `current_amount` int(11) DEFAULT NULL,
  `debit` int(11) DEFAULT NULL,
  `debit_by` int(11) DEFAULT NULL,
  `debit_type` int(11) DEFAULT NULL,
  `previous_amount` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_at4wembnex5b72qd4h3anya82` (`product_id`),
  KEY `FK_snhnkbbrqawwk1kucfjxmtckq` (`user_id`),
  CONSTRAINT `FK_at4wembnex5b72qd4h3anya82` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_snhnkbbrqawwk1kucfjxmtckq` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKc6jl55gjaf4b79vr7us1ek15e` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKl1lgvkqrccxrwyu5gggf1hrkj` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `debit` */

/*Table structure for table `dlr` */

DROP TABLE IF EXISTS `dlr`;

CREATE TABLE `dlr` (
  `smsc` varchar(40) DEFAULT NULL,
  `ts` varchar(65) DEFAULT NULL,
  `destination` varchar(40) DEFAULT NULL,
  `source` varchar(40) DEFAULT NULL,
  `service` varchar(40) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `mask` int(10) DEFAULT NULL,
  `status` int(10) DEFAULT NULL,
  `boxc` varchar(40) DEFAULT NULL,
  KEY `dlr_smsc_index` (`smsc`),
  KEY `dlr_ts_index` (`ts`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dlr` */

/*Table structure for table `dlr_status` */

DROP TABLE IF EXISTS `dlr_status`;

CREATE TABLE `dlr_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(5) DEFAULT NULL,
  `logged_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `Sender` varchar(6) DEFAULT NULL,
  `coding` tinyint(2) DEFAULT NULL,
  `count` tinyint(2) DEFAULT NULL,
  `dlr_time` datetime DEFAULT NULL,
  `errorCode` int(11) DEFAULT '0',
  `length` int(5) unsigned zerofill DEFAULT NULL,
  `message` text,
  `message_id` varchar(36) DEFAULT NULL,
  `mobi_class` tinyint(1) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `provider_id` varchar(10) DEFAULT NULL,
  `status` varchar(10) DEFAULT 'SUBMITTED',
  `type` tinyint(1) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `dlr_status` */

/*Table structure for table `failed_login` */

DROP TABLE IF EXISTS `failed_login`;

CREATE TABLE `failed_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `login_date` date DEFAULT NULL,
  `login_from` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `time` time DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `failed_login` */

/*Table structure for table `group_details` */

DROP TABLE IF EXISTS `group_details`;

CREATE TABLE `group_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `group_description` varchar(255) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nb9sn6jvfbo07d4epjvom5w4` (`user_id`),
  CONSTRAINT `FK_nb9sn6jvfbo07d4epjvom5w4` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKgyod79x6mmf7rp5n7tgfcamfo` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

/*Data for the table `group_details` */

insert  into `group_details`(`id`,`created`,`name`,`status`,`updated`,`group_description`,`user_id`) values 
(1,'2017-08-31 11:20:48','ajeet',0,'2017-10-23 16:35:04','demo',1),
(2,'2017-10-12 11:22:14','DemoSani1',2,'2017-10-23 16:22:15','employee',1),
(3,'2017-10-16 17:58:05','DemoSani2',2,'2017-10-23 16:22:18','sdfs',1),
(4,'2017-10-16 17:58:58','Demo Sani2',1,'2017-10-23 16:35:02','sdfsd',1),
(5,'2017-10-27 10:35:51','test',1,'2017-10-27 10:35:51','employee',1),
(6,'2017-10-27 17:19:33','duplicatetest',1,'2017-10-27 17:19:33','diplicvate',1),
(7,'2017-10-27 18:04:02','test1234',1,'2017-10-27 18:04:02','asdasd',1),
(8,'2017-10-27 18:12:47','single',1,'2017-10-27 18:12:47','szdfads',1),
(9,'2017-10-27 18:54:09','single 2',1,'2017-10-27 18:54:09','asdsa',1);

/*Table structure for table `hibernate_sequence` */

DROP TABLE IF EXISTS `hibernate_sequence`;

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `hibernate_sequence` */

insert  into `hibernate_sequence`(`next_val`) values 
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31),
(31);

/*Table structure for table `otp` */

DROP TABLE IF EXISTS `otp`;

CREATE TABLE `otp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `otp_data` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4mkxc1wpojj1vymcvurokktwm` (`user_id`),
  CONSTRAINT `FK_4mkxc1wpojj1vymcvurokktwm` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `otp` */

/*Table structure for table `product` */

DROP TABLE IF EXISTS `product`;

CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

/*Data for the table `product` */

insert  into `product`(`id`,`created`,`name`,`status`,`updated`) values 
(1,NULL,'Trans',1,'2017-08-09 07:52:39'),
(2,NULL,'promo',1,'2017-08-09 07:52:41'),
(3,NULL,'scrube',1,'2017-08-09 07:52:43');

/*Table structure for table `queued_sms` */

DROP TABLE IF EXISTS `queued_sms`;

CREATE TABLE `queued_sms` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(15) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `binfo` varchar(10) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `coding` varchar(2) DEFAULT NULL,
  `compress` varchar(2) DEFAULT NULL,
  `deferred` varchar(2) DEFAULT NULL,
  `dlr_mask` tinyint(2) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  `id` int(5) DEFAULT NULL,
  `mclass` varchar(2) DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `momt` varchar(10) DEFAULT NULL,
  `msgdata` text,
  `mwi` varchar(2) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `receiver` varchar(15) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `sender` varchar(6) DEFAULT NULL,
  `service` varchar(15) DEFAULT NULL,
  `sms_type` tinyint(1) DEFAULT NULL,
  `smsc_id` varchar(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `udhdata` varchar(10) DEFAULT NULL,
  `validity` varchar(2) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `queued_sms` */

/*Table structure for table `queued_sms3` */

DROP TABLE IF EXISTS `queued_sms3`;

CREATE TABLE `queued_sms3` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(15) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `binfo` varchar(10) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `coding` varchar(2) DEFAULT NULL,
  `compress` varchar(2) DEFAULT NULL,
  `deferred` varchar(2) DEFAULT NULL,
  `dlr_mask` tinyint(2) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  `id` int(5) DEFAULT NULL,
  `mclass` varchar(2) DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `momt` varchar(10) DEFAULT NULL,
  `msgdata` text,
  `mwi` varchar(2) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `receiver` varchar(15) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `sender` varchar(6) DEFAULT NULL,
  `service` varchar(15) DEFAULT NULL,
  `sms_type` tinyint(1) DEFAULT NULL,
  `smsc_id` varchar(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `udhdata` varchar(10) DEFAULT NULL,
  `validity` varchar(2) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `queued_sms3` */

/*Table structure for table `queued_sms4` */

DROP TABLE IF EXISTS `queued_sms4`;

CREATE TABLE `queued_sms4` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(15) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `binfo` varchar(10) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `coding` varchar(2) DEFAULT NULL,
  `compress` varchar(2) DEFAULT NULL,
  `deferred` varchar(2) DEFAULT NULL,
  `dlr_mask` tinyint(2) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  `id` int(5) DEFAULT NULL,
  `mclass` varchar(2) DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `momt` varchar(10) DEFAULT NULL,
  `msgdata` text,
  `mwi` varchar(2) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `receiver` varchar(15) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `sender` varchar(6) DEFAULT NULL,
  `service` varchar(15) DEFAULT NULL,
  `sms_type` tinyint(1) DEFAULT NULL,
  `smsc_id` varchar(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `udhdata` varchar(10) DEFAULT NULL,
  `validity` varchar(2) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `queued_sms4` */

/*Table structure for table `queued_sms5` */

DROP TABLE IF EXISTS `queued_sms5`;

CREATE TABLE `queued_sms5` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(15) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `binfo` varchar(10) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `coding` varchar(2) DEFAULT NULL,
  `compress` varchar(2) DEFAULT NULL,
  `deferred` varchar(2) DEFAULT NULL,
  `dlr_mask` tinyint(2) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  `id` int(5) DEFAULT NULL,
  `mclass` varchar(2) DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `momt` varchar(10) DEFAULT NULL,
  `msgdata` text,
  `mwi` varchar(2) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `receiver` varchar(15) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `sender` varchar(6) DEFAULT NULL,
  `service` varchar(15) DEFAULT NULL,
  `sms_type` tinyint(1) DEFAULT NULL,
  `smsc_id` varchar(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `udhdata` varchar(10) DEFAULT NULL,
  `validity` varchar(2) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `queued_sms5` */

/*Table structure for table `queued_sms_quick` */

DROP TABLE IF EXISTS `queued_sms_quick`;

CREATE TABLE `queued_sms_quick` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(15) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `binfo` varchar(10) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `coding` varchar(2) DEFAULT NULL,
  `compress` varchar(2) DEFAULT NULL,
  `deferred` varchar(2) DEFAULT NULL,
  `dlr_mask` tinyint(2) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  `id` int(5) DEFAULT NULL,
  `mclass` varchar(2) DEFAULT NULL,
  `meta_data` varchar(255) DEFAULT NULL,
  `momt` varchar(10) DEFAULT NULL,
  `msgdata` text,
  `mwi` varchar(2) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `receiver` varchar(15) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `sender` varchar(6) DEFAULT NULL,
  `service` varchar(15) DEFAULT NULL,
  `sms_type` tinyint(1) DEFAULT NULL,
  `smsc_id` varchar(10) DEFAULT NULL,
  `time` varchar(20) DEFAULT NULL,
  `udhdata` varchar(10) DEFAULT NULL,
  `validity` varchar(2) DEFAULT NULL,
  `priority` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `queued_sms_quick` */

/*Table structure for table `route` */

DROP TABLE IF EXISTS `route`;

CREATE TABLE `route` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account_type` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `route` int(11) DEFAULT NULL,
  `smpp_name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `route` */

insert  into `route`(`id`,`account_type`,`created`,`route`,`smpp_name`,`type`,`updated`) values 
(1,0,NULL,1,'vedant1','trans','2017-09-01 12:09:05'),
(2,0,NULL,2,'DemoP','promo','2017-08-29 09:32:26');

/*Table structure for table `sender_id` */

DROP TABLE IF EXISTS `sender_id`;

CREATE TABLE `sender_id` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `sender_id` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mpuqrn5q8sjnrjdcqucswytwy` (`user_id`),
  CONSTRAINT `FK7ngy1axe8mrm3ocvpvkeyawnp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_mpuqrn5q8sjnrjdcqucswytwy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sender_id` */

/*Table structure for table `sent_sms` */

DROP TABLE IF EXISTS `sent_sms`;

CREATE TABLE `sent_sms` (
  `sql_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `momt` enum('MO','MT','DLR') DEFAULT NULL,
  `sender` varchar(20) DEFAULT NULL,
  `receiver` varchar(20) DEFAULT NULL,
  `udhdata` blob,
  `msgdata` text,
  `time` bigint(20) DEFAULT NULL,
  `smsc_id` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  `id` bigint(20) DEFAULT NULL,
  `sms_type` bigint(20) DEFAULT NULL,
  `mclass` bigint(20) DEFAULT NULL,
  `mwi` bigint(20) DEFAULT NULL,
  `coding` bigint(20) DEFAULT NULL,
  `compress` bigint(20) DEFAULT NULL,
  `validity` bigint(20) DEFAULT NULL,
  `deferred` bigint(20) DEFAULT NULL,
  `dlr_mask` bigint(20) DEFAULT NULL,
  `dlr_url` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `alt_dcs` bigint(20) DEFAULT NULL,
  `rpi` bigint(20) DEFAULT NULL,
  `charset` varchar(255) DEFAULT NULL,
  `boxc_id` varchar(255) DEFAULT NULL,
  `binfo` varchar(255) DEFAULT NULL,
  `meta_data` text,
  `priority` bigint(20) DEFAULT NULL,
  `foreign_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`sql_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `sent_sms` */

/*Table structure for table `sms_balance` */

DROP TABLE IF EXISTS `sms_balance`;

CREATE TABLE `sms_balance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `balance` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `expiry_date` datetime DEFAULT NULL,
  `sent_sms` int(11) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_hlir5yi708pljnbgfkcm38k3a` (`product_id`),
  KEY `FK_15pdaudrhw7mgx5bm1sm3gxkc` (`user_id`),
  CONSTRAINT `FK_15pdaudrhw7mgx5bm1sm3gxkc` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_hlir5yi708pljnbgfkcm38k3a` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKg9cnkip2njcmss5o32am434bs` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKo408vohjl9bkvmsnw7oet176s` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=latin1;

/*Data for the table `sms_balance` */

insert  into `sms_balance`(`id`,`balance`,`created`,`expiry_date`,`sent_sms`,`updated`,`product_id`,`user_id`) values 
(1,494627063,NULL,NULL,0,NULL,1,1),
(2,2147464592,NULL,NULL,0,NULL,2,1);

/*Table structure for table `spam_number` */

DROP TABLE IF EXISTS `spam_number`;

CREATE TABLE `spam_number` (
  `mobile_no` varchar(20) NOT NULL,
  KEY `mobile_no` (`mobile_no`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `spam_number` */

insert  into `spam_number`(`mobile_no`) values 
('918108004545'),
('919415422104');

/*Table structure for table `temp_dnd` */

DROP TABLE IF EXISTS `temp_dnd`;

CREATE TABLE `temp_dnd` (
  `mobile` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `temp_dnd` */

/*Table structure for table `template` */

DROP TABLE IF EXISTS `template`;

CREATE TABLE `template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` datetime DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_42s91o0kit340ifv4dm0ybyiv` (`user_id`),
  CONSTRAINT `FK_42s91o0kit340ifv4dm0ybyiv` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKed5es5c9wvf5hukut176tv32w` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `template` */

insert  into `template`(`id`,`created`,`description`,`name`,`status`,`updated`,`user_id`) values 
(2,'2017-08-31 11:34:37','testing demo tdff err rtrtt rtrt rtrt    tttt','hello',0,'2017-08-31 11:34:37',1);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `reseller_id` int(11) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=latin1;

/*Data for the table `user` */

insert  into `user`(`id`,`address`,`city`,`company_name`,`country`,`created`,`email`,`mobile`,`name`,`password`,`reseller_id`,`role`,`state`,`status`,`updated`,`user_name`) values 
(1,'xxz1','xxz1','xxz','xxz','2017-07-27 15:00:27','sani07info@gmail.com','919415422104','sani verma2','123',0,1,'ddd',1,'2017-10-14 11:59:21','abc'),
(74,'xxz1','xxz1','xxz','xxz','2017-08-09 15:24:19','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','cdf8c8',1,2,'ddd',1,'2017-08-09 13:24:19','r007'),
(75,'xcvcxv','xcvxc','xcvxcv','xcvcx','2017-08-09 16:56:31','xcvxcv','vxcvcxv','xcvxcv','596176',1,2,'xcvxc',1,'2017-08-09 14:56:31','xcvxc'),
(76,'xxz1','xxz1','xxz','xxz','2017-08-24 13:22:31','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','43b521',1,2,'ddd',1,'2017-08-24 11:22:31','r008'),
(77,'xxz1','xxz1','xxz','xxz','2017-08-24 13:24:00','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','81f81d',1,2,'ddd',1,'2017-08-24 11:24:00','r009'),
(78,'xxz1','xxz1','xxz','xxz','2017-09-07 15:40:25','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','6352e8',1,1,'ddd',2,'2017-09-19 16:15:44','abc1234'),
(79,'xxz1','xxz1','xxz','xxz','2017-09-21 12:08:15','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','ba68da',1,1,'ddd',1,'2017-09-21 12:08:15','a12'),
(80,'xxz1','xxz1','xxz','xxz','2017-09-21 12:09:32','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','eaad68',1,1,'ddd',1,'2017-09-21 12:09:32','a1233'),
(81,'xxz1','xxz1','xxz','xxz','2017-09-21 12:10:03','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','245582',1,1,'ddd',1,'2017-09-21 12:10:03','a12335'),
(82,'xxz1','xxz1','xxz','xxz','2017-09-21 12:11:17','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','202986',1,1,'ddd',1,'2017-09-21 12:11:17','a123335'),
(83,'xxz1','xxz1','xxz','xxz','2017-09-21 12:19:51','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','03ff05',1,1,'ddd',1,'2017-09-21 12:19:51','a1233235'),
(84,'xxz1','xxz1','xxz','xxz','2017-09-21 12:20:36','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','033b78',1,1,'ddd',1,'2017-09-21 12:20:36','b1'),
(85,'xxz1','xxz1','xxz','xxz','2017-09-21 12:22:54','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','2cc107',1,1,'ddd',1,'2017-09-21 12:22:54','b12'),
(86,'xxz1','xxz1','xxz','xxz','2017-09-21 12:23:28','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','6b0d98',1,1,'ddd',1,'2017-09-21 12:23:28','b123'),
(87,'xxz1','xxz1','xxz','xxz','2017-09-21 12:25:59','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','8b0599',1,1,'ddd',1,'2017-09-21 12:25:59','c1'),
(88,'xxz1','xxz1','xxz','xxz','2017-09-21 12:26:59','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','69cc3a',1,1,'ddd',1,'2017-09-21 12:26:59','c2'),
(89,'xxz1','xxz1','xxz','xxz','2017-09-21 12:27:20','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','3755bb',1,1,'ddd',1,'2017-09-21 12:27:20','c4'),
(90,'xxz1','xxz1','xxz','xxz','2017-09-21 12:29:24','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','eeb117',1,1,'ddd',1,'2017-09-21 12:29:24','c6'),
(91,'xxz1','xxz1','xxz','xxz','2017-09-21 12:29:40','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','9d1587',1,1,'ddd',1,'2017-09-21 12:29:40','c8'),
(92,'xxz1','xxz1','xxz','xxz','2017-09-21 12:29:46','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','0acb74',1,1,'ddd',1,'2017-09-21 12:29:46','c89'),
(93,'xxz1','xxz1','xxz','xxz','2017-09-21 12:33:57','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','e9ed48',1,1,'ddd',1,'2017-09-21 12:33:57','c892'),
(94,'xxz1','xxz1','xxz','xxz','2017-09-21 12:34:01','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','d7d7e9',1,1,'ddd',1,'2017-09-21 12:34:01','c8925'),
(95,'xxz1','xxz1','xxz','xxz','2017-09-21 12:35:51','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','ebacd5',1,1,'ddd',1,'2017-09-21 12:35:51','c892533'),
(96,'xxz1','xxz1','xxz','xxz','2017-09-21 12:35:53','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','bf6762',1,1,'ddd',1,'2017-09-21 12:35:53','c89253344'),
(97,'xxz1','xxz1','xxz','xxz','2017-09-21 12:36:11','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','f98133',1,1,'ddd',1,'2017-09-21 12:36:11','c8925334455'),
(98,'xxz1','xxz1','xxz','xxz','2017-09-21 12:36:28','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','38c595',1,1,'ddd',1,'2017-09-21 12:36:28','c89253344556'),
(99,'xxz1','xxz1','xxz','xxz','2017-09-21 12:36:41','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','6a4389',1,1,'ddd',1,'2017-09-21 12:36:41','d'),
(100,'xxz1','xxz1','xxz','xxz','2017-09-21 12:36:49','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','674dc8',1,1,'ddd',1,'2017-09-21 12:36:49','d1'),
(101,'xxz1','xxz1','xxz','xxz','2017-09-21 12:37:15','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','d90010',1,1,'ddd',1,'2017-09-21 12:37:15','d12'),
(102,'xxz1','xxz1','xxz','xxz','2017-09-21 12:39:04','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','7fc641',1,1,'ddd',1,'2017-09-21 12:39:04','d1244'),
(103,'xxz1','xxz1','xxz','xxz','2017-09-21 12:39:31','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','65eae0',1,1,'ddd',1,'2017-09-21 12:39:31','E1'),
(104,'xxz1','xxz1','xxz','xxz','2017-09-21 12:39:37','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','38ba00',1,1,'ddd',1,'2017-09-21 12:39:37','E2'),
(105,'xxz1','xxz1','xxz','xxz','2017-09-21 12:44:31','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','46b7a2',1,1,'ddd',1,'2017-09-21 12:44:31','E3'),
(106,'xxz1','xxz1','xxz','xxz','2017-09-21 12:44:34','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','c7adca',1,1,'ddd',1,'2017-09-21 12:44:34','E4'),
(107,'xxz1','xxz1','xxz','xxz','2017-09-21 12:45:05','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','4b5e25',1,1,'ddd',1,'2017-09-21 12:45:05','E5'),
(108,'xxz1','xxz1','xxz','xxz','2017-09-21 12:45:09','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','ad5d90',1,1,'ddd',1,'2017-09-21 12:45:09','E6'),
(109,'xxz1','xxz1','xxz','xxz','2017-09-21 12:46:14','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','490a6d',1,1,'ddd',1,'2017-09-21 12:46:14','E7'),
(110,'xxz1','xxz1','xxz','xxz','2017-09-21 12:46:18','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','5bbe33',1,1,'ddd',1,'2017-09-21 12:46:18','E72'),
(111,'xxz1','xxz1','xxz','xxz','2017-09-21 12:48:05','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','583539',1,1,'ddd',1,'2017-09-21 12:48:05','E721'),
(112,'xxz1','xxz1','xxz','xxz','2017-09-21 12:48:09','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','6453e5',1,1,'ddd',1,'2017-09-21 12:48:09','E7212'),
(113,'xxz1','xxz1','xxz','xxz','2017-09-21 12:48:13','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','a18856',1,1,'ddd',1,'2017-09-21 12:48:13','E72123'),
(114,'xxz1','xxz1','xxz','xxz','2017-09-21 12:49:56','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','6c812d',1,1,'ddd',1,'2017-09-21 12:49:56','q72123'),
(115,'xxz1','xxz1','xxz','xxz','2017-09-21 12:50:03','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','0fb764',1,1,'ddd',1,'2017-09-21 12:50:03','q2123'),
(116,'xxz1','xxz1','xxz','xxz','2017-09-21 12:50:07','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','930f12',1,1,'ddd',1,'2017-09-21 12:50:07','q123'),
(117,'xxz1','xxz1','xxz','xxz','2017-09-21 13:18:07','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','7b9f00',1,1,'ddd',1,'2017-09-21 13:18:07','g123'),
(118,'xxz1','xxz1','xxz','xxz','2017-09-21 13:18:11','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','2d7aeb',1,1,'ddd',1,'2017-09-21 13:18:11','g12e3'),
(119,'xxz1','xxz1','xxz','xxz','2017-09-21 13:18:15','pramod1.v@mobisofttech.co.in','9415422104','sani verma1','81376a',1,1,'ddd',1,'2017-09-21 13:18:15','g12ee3');

/*Table structure for table `user_authrization` */

DROP TABLE IF EXISTS `user_authrization`;

CREATE TABLE `user_authrization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dnd_check` varchar(2) DEFAULT NULL,
  `percentage` varchar(10) DEFAULT NULL,
  `spam_check` varchar(2) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_r1dxd05i86sgo7qbaqcqnon1r` (`user_id`),
  CONSTRAINT `FK_r1dxd05i86sgo7qbaqcqnon1r` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=latin1;

/*Data for the table `user_authrization` */

insert  into `user_authrization`(`id`,`dnd_check`,`percentage`,`spam_check`,`product_id`,`user_id`) values 
(1,'Y','100','N',1,1),
(2,'N','100','N',2,1),
(3,'Y','100','N',2,79),
(4,'Y','100','N',2,80),
(5,'Y','100','N',2,81),
(6,'Y','100','N',2,82),
(7,'Y','100','N',2,83),
(8,'Y','100','N',2,84),
(9,'Y','100','N',2,85),
(10,'Y','100','N',2,86),
(11,'Y','100','N',2,87),
(12,'Y','100','N',2,88),
(13,'Y','100','N',2,89),
(14,'Y','100','N',2,90),
(15,'Y','100','N',2,91),
(16,'Y','100','N',2,92),
(17,'Y','100','N',2,93),
(18,'Y','100','N',2,94),
(19,'Y','100','N',2,95),
(20,'Y','100','N',2,96),
(21,'Y','100','N',2,97),
(22,'Y','100','N',2,98),
(23,'Y','100','N',2,99),
(24,'Y','100','N',2,100),
(25,'Y','100','N',2,101),
(26,'Y','100','N',2,102),
(27,'Y','100','N',2,103),
(28,'Y','100','N',2,104),
(29,'Y','100','N',2,105),
(30,'Y','100','N',2,106),
(31,'Y','100','N',2,107),
(32,'Y','100','N',2,108),
(33,'Y','100','N',2,109),
(34,'Y','100','N',2,110),
(35,'Y','100','N',2,111),
(36,'Y','100','N',2,112),
(37,'Y','100','N',2,113),
(38,'Y','100','N',2,114),
(39,'Y','100','N',2,115),
(40,'Y','100','N',2,116),
(41,'Y','100','N',2,117),
(42,'Y','100','N',2,118),
(43,'Y','100','N',2,119);

/*Table structure for table `user_jobs` */

DROP TABLE IF EXISTS `user_jobs`;

CREATE TABLE `user_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `columns` int(11) DEFAULT NULL,
  `completed_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `count` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `job_status` int(11) DEFAULT NULL COMMENT '0 - Queued; 1 - Processing; 2 - Partially Processed; 3 - Completed; 4 - Zombie',
  `job_type` int(11) DEFAULT NULL,
  `message` varchar(5000) DEFAULT NULL,
  `message_length` int(11) DEFAULT NULL,
  `message_type` int(11) DEFAULT NULL,
  `queued_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `route` varchar(255) DEFAULT NULL,
  `scheduled_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `send_now` varchar(255) DEFAULT NULL,
  `send_ratio` int(11) DEFAULT NULL,
  `sender` varchar(255) DEFAULT NULL,
  `total_numbers` int(11) DEFAULT NULL,
  `total_sent` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  `duplicate_status` int(11) DEFAULT NULL,
  `schedule_status` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

/*Data for the table `user_jobs` */

insert  into `user_jobs`(`id`,`columns`,`completed_at`,`count`,`file_name`,`job_status`,`job_type`,`message`,`message_length`,`message_type`,`queued_at`,`route`,`scheduled_at`,`send_now`,`send_ratio`,`sender`,`total_numbers`,`total_sent`,`user_id`,`duplicate_status`,`schedule_status`,`product_id`) values 
(1,0,'2017-11-07 15:45:10',4,'E:\\sms\\uploadUserTextFile\\personalized\\Personalized-11510049706694.xls',2,5,NULL,0,1,'2017-11-07 15:45:07','DemoP','2017-08-12 00:00:00',NULL,0,'MOBSFT',4,4,1,0,0,2),
(2,0,'2017-11-07 15:48:10',3,'E:\\sms\\uploadUserTextFile\\personalized\\Personalized-11510049883116.xls',2,5,NULL,0,1,'2017-11-07 15:48:03','vedant1','2017-08-12 00:00:00',NULL,0,'MOBSFT',3,3,1,0,0,1),
(3,0,'2017-11-07 16:07:37',1,'E:\\sms\\uploadUserTextFile\\1510051007956singleumber.txt',2,3,'dsas dasdas',11,1,'2017-11-07 16:06:48','DemoP','2017-08-12 00:00:00',NULL,0,'MOBSFT',24,24,1,0,0,2),
(5,0,'2017-11-07 16:21:04',1,'E:\\sms\\uploadUserTextFile\\915100518539861.txt',2,2,'test group',10,1,'2017-11-07 16:20:54','DemoP','2017-08-12 00:00:00',NULL,0,'MOBSFT',4,4,1,0,0,2),
(6,0,'2017-11-07 16:54:04',1,'E:\\sms\\uploadUserTextFile\\915100537966341.txt',2,2,'test',4,1,'2017-11-07 16:53:16','DemoP','2017-11-07 16:53:16',NULL,0,'TUSHAR',4,4,1,0,0,2),
(7,0,'2017-11-07 17:00:15',1,'E:\\sms\\uploadUserTextFile\\1510054178049singleumber.txt',2,3,'test ffgsdf',11,1,'2017-11-07 16:59:38','DemoP','2017-11-07 16:59:38',NULL,0,'AJEETM',24,24,1,0,0,2),
(8,0,'2017-11-07 17:21:16',4,'E:\\sms\\uploadUserTextFile\\personalized\\Personalized-11510055417480.xls',2,5,NULL,0,1,'2017-11-07 17:20:17','DemoP','2017-11-07 17:20:17',NULL,0,'MOBSFT',4,4,1,0,0,2),
(9,0,'2017-11-07 17:22:16',4,'E:\\sms\\uploadUserTextFile\\personalized\\Personalized-11510055450340.xls',2,5,NULL,0,1,'2017-11-07 17:20:50','DemoP','2017-11-07 17:20:50',NULL,0,'MOBSFT',4,4,1,0,0,2),
(10,0,'2017-11-07 17:22:16',1,'E:\\sms\\uploadUserTextFile\\1510055480679singleumber.txt',2,3,'FDFASWDA',8,1,'2017-11-07 17:21:20','DemoP','2017-11-07 17:21:20',NULL,0,'MOBSFT',24,24,1,0,0,2),
(11,0,'2017-11-07 18:13:01',1,'E:\\sms\\uploadUserTextFile\\1510056334697quickschedule.txt',2,1,'dfsdfsd',7,1,'2017-11-07 17:35:34','DemoP','2017-11-07 18:04:00',NULL,0,'MOBSFT',1,1,1,0,1,2);

/*Table structure for table `user_product` */

DROP TABLE IF EXISTS `user_product`;

CREATE TABLE `user_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `route_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_gbrvdyv4h0adtukip1glaft27` (`user_id`),
  KEY `FK_82vlec2t9t3uccrxec2u85h80` (`route_id`),
  KEY `FK_dc1y63v3t2xgya5tbcr0po7it` (`product_id`),
  CONSTRAINT `FK_82vlec2t9t3uccrxec2u85h80` FOREIGN KEY (`route_id`) REFERENCES `route` (`id`),
  CONSTRAINT `FK_dc1y63v3t2xgya5tbcr0po7it` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKom8m2ls4h55pfd1om6jb5l66d` FOREIGN KEY (`route_id`) REFERENCES `route` (`id`),
  CONSTRAINT `FKq5o2e33vlwpfc2k1mredtia6p` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

/*Data for the table `user_product` */

insert  into `user_product`(`id`,`user_id`,`route_id`,`product_id`) values 
(1,1,1,1),
(2,1,2,2),
(3,74,2,1),
(5,75,1,1);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
