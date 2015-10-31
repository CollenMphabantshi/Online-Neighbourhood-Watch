-- phpMyAdmin SQL Dump
-- version 4.0.10.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Oct 31, 2015 at 04:50 PM
-- Server version: 5.5.40-cll
-- PHP Version: 5.4.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `banchvyd_uwatch`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `surname` varchar(200) NOT NULL,
  `identity` varchar(200) NOT NULL,
  `email` varchar(200) NOT NULL,
  `password` text NOT NULL,
  `province` text NOT NULL,
  `user_type` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=8 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `surname`, `identity`, `email`, `password`, `province`, `user_type`) VALUES
(1, 'John', 'Doe', '82910234828', 'thrine.legodi@gmail.com', '19e0ee1f4b7c1f61235247975b5622e42b7f7123659f878833009bd68687e542', 'Gauteng', 'Digital Forensics Investigator'),
(2, 'Stacey', 'Omeleze', '82910234825', 'someleze@cs.up.ac.za', '21a35807b4ce25e7f428be3f689b9668af4960b7f5d37e3309dfd1c38ff498ff', 'Gauteng', 'Law Enforcement'),
(3, 'collen', 'Smith', '1223333333334', 'mickeybanchi@gmail.com', '2154b6aae2bae318be96f26d1de3022be12da1d37e118a0056ef9b1d4c18172d', 'Eastern Cape', 'Normal'),
(4, 'me', 'smile', '1436547777658', 'ussss@gmail.com', '574192818be17b52ff969097ff73b1eb3328db82084e02e2d46c75486bf50fd2', 'Eastern Cape', 'Normal'),
(5, 'banchi', 'banchi', '168976554', 'uu@gmail.com', 'fc7090b4b8c269cc209c158a8732eca987c6fdd8a155670b5224ebd0474a5ec0', 'Eastern Cape', 'Normal'),
(6, 'gggg', 'ghhhh', '112345', 'hhhg@gmail.com', '3c2df78bb0bfbb07d6139bd6bdfad3d039ed1c3c49622e9527d4535efa7ffc6d', 'Eastern Cape', 'Normal'),
(7, 'gghh', 'hhj', '14556', 'ttt@hh.com', '592d3f5e0349ddfc88bbea6485ecfe32285485b66f10f38d9397238bc4ff04db', 'Eastern Cape', 'Normal');

-- --------------------------------------------------------

--
-- Table structure for table `watchdata`
--

CREATE TABLE IF NOT EXISTS `watchdata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `watch_file` text NOT NULL,
  `watch_filehash` text NOT NULL,
  `watcher` varchar(200) NOT NULL,
  `watch_casetype` varchar(200) NOT NULL,
  `watch_caselocation` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=10 ;

--
-- Dumping data for table `watchdata`
--

INSERT INTO `watchdata` (`id`, `watch_file`, `watch_filehash`, `watcher`, `watch_casetype`, `watch_caselocation`) VALUES
(1, '1438785290_IMG_20150805_163136.jpg', '8bbd684676721b613ffa13f954c774f97062610648f48964ddd22b54e6259cc3', 'mickeybanchi@gmail.com', 'Select type of case:', 'Eastern Cape'),
(2, '1438790233_AUDIO_20150805_175633.3gpp', 'dbb1d3024155f987a883a676bf3a57fcbe01e1a4c1604e71e876365355297ee2', 'mickeybanchi@gmail.com', 'Select type of case:', 'Eastern Cape'),
(3, '1438847065_AUDIO_20150806_094125.3gpp', '0fadd72ed1ad2c37227fceaefc5a6f5eda2bb2e36faffb8dafbdbe025c96c4ee', 'mickeybanchi@gmail.com', 'Car hijack', 'Eastern Cape'),
(4, '1442706163_AUDIO_20150920_014155.3gpp', '9762618ee7ae60d18044e526404cc4cd69f2740d1d3429d0980d999e6f2ff673', 'mickeybanchi@gmail.com', 'Select type of case:', 'Eastern Cape'),
(5, '1442706231_AUDIO_20150920_014304.3gpp', '72c4cda74644b7dc28d6a3c3e34c975f4519426b99c1f54d3459cd08888f9124', 'mickeybanchi@gmail.com', 'Select type of case:', 'Eastern Cape'),
(6, '1446297237_IMG_20151031_151307.jpg', 'd0c59ed78996968b27af42e744725e087c7fae7c4e3724fb954ecd7207eb106e', 'uu@gmail.com', 'Gun shooting', 'Eastern Cape'),
(7, '1446297320_VID_20151031_151417.mp4', 'b6dd723a86e7c7945b5f8bd2abc90a859c36763db8a87f6247765d9a079043bc', 'uu@gmail.com', 'Gun shooting', 'Eastern Cape'),
(8, '1446297397_AUDIO_20151031_151559.3gpp', 'f214567a271905db8eb36805dd5d816153e5648d5a410f84ff51c206d2f95633', 'uu@gmail.com', 'Gun shooting', 'Eastern Cape'),
(9, '1446301809_IMG_20151031_162450.jpg', 'c44ffa0baaeba736f84648385b8640ec3b56f025cde550ea2869afe2c3aa2359', 'hhhg@gmail.com', 'House break', 'Eastern Cape');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
