CREATE DATABASE IF NOT EXISTS `mindbot`;
USE `mindbot`;

CREATE TABLE IF NOT EXISTS `coins` (
  `iduser` varchar(100) DEFAULT NULL,
  `idguild` varchar(100) DEFAULT NULL,
  `coins` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `coins` (`iduser`, `idguild`, `coins`) VALUES
    ('296696846846394368', '889503252998131722', 1382466);

CREATE TABLE IF NOT EXISTS `fakeresponse` (
  `idquestion` int(11) DEFAULT NULL,
  `fakeanwser` varchar(250) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `fakeresponse` (`idquestion`, `fakeanwser`) VALUES
    (1, 'Most Value Player'),
    (1, 'Most Valuable Companies'),
    (1, 'Most Vaccinated Countries');

CREATE TABLE IF NOT EXISTS `questions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

INSERT INTO `questions` (`id`, `question`) VALUES
    (1, 'O que é um MVP?');
CREATE TABLE IF NOT EXISTS `response` (
  `id` int(11) NOT NULL,
  `answer` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `response` (`id`, `answer`) VALUES
    (1, 'Minimum viable product');